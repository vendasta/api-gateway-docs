---
tags: [ai-employees, knowledge, assistants, embeddings]
---

# Manage AI Employee Knowledge

An **AI Employee** is an AI assistant that works inside a business's account &mdash; answering questions, drafting replies, and helping staff and customers on that business's behalf. An AI Employee is only as good as what it knows. You can teach it by giving it **knowledge sources**: pieces of reference material such as a product FAQ, a returns policy, a price list, or a company handbook.

This guide is for teams who already keep that reference material somewhere else &mdash; a CMS, a help desk, a shared drive, your own product database &mdash; and want to keep the AI Employees in Vendasta in sync with it automatically, without anyone copying and pasting into the dashboard. By the end you will be able to:

- discover the businesses and AI Employees in your Vendasta account,
- push knowledge to an AI Employee as plain text or as an uploaded file,
- check whether that knowledge finished processing,
- update or remove knowledge you previously pushed, and
- cleanly remove everything you added when a customer offboards.

> **Before you start:** This is a developer integration guide. You will be making HTTP requests to Vendasta's API. You do not need to have used the Vendasta platform before, but you should be comfortable sending authenticated HTTP requests (for example with `curl`, Postman, or a small script). Every request in this guide can also be run straight from the documentation using the **Generate Token** button at the top of the Developer Center.

---

## The one rule that keeps this integration safe

Before the steps, read this once. It is the single most important idea in this guide.

A business's knowledge base is a **shared space**. It can contain:

- knowledge **your integration** created,
- knowledge a human created by hand in the Vendasta dashboard,
- a built-in **Business Profile** source the platform maintains automatically, and
- knowledge **shared in** to the account by a partner (see [Knowledge you cannot manage](#knowledge-you-cannot-manage)).

When you create a knowledge source, the API returns a `knowledgeSourceId`. **That id is your only reliable handle on the thing you created.** The rules that follow all come from that one fact:

- **Store every `knowledgeSourceId` you get back**, alongside whatever it maps to in your own system (the help-desk article id, the file path, etc.).
- **Updating knowledge &mdash; text or file &mdash; requires the stored id.** There is no "find the one I made last time" on the server. If you push again *without* the id, you always create a **brand-new, duplicate** source. This is true even if the name or file name is identical.
- **Only touch ids you created.** Never update or remove an id you do not recognize. The human-created and platform-created sources are not yours to manage, and clobbering them will break the AI Employee for the business.
- If you ever **lose an id** (a crashed script, a dropped response) or **accidentally create a duplicate**, you recover by [listing the knowledge base and matching by name or file name](#step-8-recover-a-lost-id-or-clean-up-a-duplicate) &mdash; then update-or-delete by id.
- **Offboarding is just the reverse:** list the knowledge base and remove the ids your integration created. Nothing else.

Keep a small mapping table in your own database from the start &mdash; `your_record_id → vendasta_knowledge_source_id` &mdash; and this integration stays simple. Skip it, and you will eventually create duplicates you cannot cleanly find.

---

## Step 1: Get an access token

Every request is authenticated with a **service-account access token**. A service account belongs to your organization (not to a person), which is what you want for an unattended integration.

Follow the platform guide to create the service account and exchange its credentials for a token: **[Obtaining an access token for a service account](https://developers.vendasta.com/platform/fe38b02978deb-obtaining-an-access-token-for-a-service-account)**.

When you request the token, ask for the **scopes** this guide needs. Scopes are the permissions attached to the token:

| Scope | Lets you |
| --- | --- |
| `business` | List the businesses (account groups) in your account |
| `ai-assistant` | Discover the AI Employees on an account |
| `knowledge` | Read **and** write knowledge (list, upsert, status, upload, remove) |

You send the token on every request in an `Authorization` header:

```
Authorization: Bearer <your access token>
```

Throughout this guide, wherever you see `Bearer <Access Token ...>`, substitute the token you obtained here.

> Tokens expire. A long-running integration should request a fresh token when the current one is close to expiry rather than reusing one indefinitely.

---

## Step 2: Find the businesses in your account

Knowledge is always scoped to one business. In Vendasta a business is called an **account group**, and its id looks like `AG-XXXXXXXXXX`. You will need that id for every knowledge request.

If your system already stores the account group ids, you can skip this step. Otherwise, list them with the **List Business Locations** API. Supply your **partner id** (your organization's short identifier in Vendasta, for example `ABC`) in the required `filter[businessPartner.id]` query parameter.

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/businessLocations",
  "query": {
    "filter[businessPartner.id]": "ABC",
    "page[limit]": "25"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>"
  }
}
```

The response holds a page of businesses in `data`. Each entry's `id` is the account group id you will use later.

```json
{
  "links": {
    "first": "https://prod.apigateway.co/platform/businessLocations?filter[businessPartner.id]=ABC&page[cursor]=&page[limit]=25",
    "next": "https://prod.apigateway.co/platform/businessLocations?filter[businessPartner.id]=ABC&page[cursor]=MjU=&page[limit]=25"
  },
  "data": [
    {
      "type": "businessLocations",
      "id": "AG-1234567",
      "attributes": {
        "name": "Company Example"
      }
    }
  ]
}
```

If your account has more businesses than fit in one page, the response includes a `links.next` URL. Make a `GET` request to that URL (with your `Authorization` header) to get the next page, and repeat until the response no longer contains a `links.next`. See the [platform paging docs](https://developers.vendasta.com/platform/ZG9jOjEwMTkzMDg0-overview#paging) for details.

---

## Step 3: Find the AI Employees on an account

An account can have more than one AI Employee, and knowledge is attached to a **specific** AI Employee. Each AI Employee is identified by an **assistant id** in the form `ASSISTANT-XXXX`.

List the AI Employees on an account group with **List Assistants**. From here on you are calling the gRPC gateway at `https://prod.apigateway.co/grpc`; these endpoints use `POST` with a JSON body.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/assistants/list",
  "headers": {
    "Authorization": "Bearer <Access Token with 'ai-assistant' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567"
  }
}
```

The response lists the AI Employees on the account. Note the `assistantId` &mdash; that is the value you pass when attaching knowledge.

```json
{
  "assistants": [
    {
      "assistantId": "ASSISTANT-A1B2",
      "assistantType": "receptionist",
      "name": "Front Desk"
    }
  ],
  "pagingMetadata": {
    "hasMore": false,
    "nextCursor": "",
    "totalResults": "1"
  }
}
```

> **Use the id exactly as returned.** The Knowledge API accepts the canonical `ASSISTANT-XXXX` spelling (and, for convenience, the bare id without the `ASSISTANT-` prefix). Internal application ids &mdash; anything starting with `APP-` &mdash; are **not** part of this API and will be rejected. Do not construct assistant ids yourself; always take them from this endpoint.

This endpoint is paged the same way as the knowledge endpoints. See [Paging through long lists](#paging-through-long-lists) below &mdash; the exact same loop applies here.

---

## Step 4: Add knowledge as text

The simplest knowledge source is a piece of text &mdash; an FAQ answer, a policy paragraph, a set of talking points. Create one with **Upsert Knowledge**, setting the `text` content and giving the source a human-readable `name`. Set `assistantId` to link it to the AI Employee that should be able to use it.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/upsert",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567",
    "assistantId": "ASSISTANT-A1B2",
    "name": "Returns policy",
    "description": "Our 30-day returns policy, synced from the help center.",
    "content": {
      "text": {
        "text": "Customers may return unused items within 30 days for a full refund..."
      }
    }
  }
}
```

Because you did **not** send a `knowledgeSourceId`, this call **creates** a new source. The response gives you its id:

```json
{
  "knowledgeSourceId": "KS-7f3c1a90"
}
```

**Store `KS-7f3c1a90` now**, mapped to whatever this text represents in your own system. You will need it to update or remove this source later, and there is no other way to reliably find it again. (See [the one rule](#the-one-rule-that-keeps-this-integration-safe).)

**How linking to an AI Employee works.** Setting `assistantId` on an upsert *adds* a link between this source and that AI Employee. Linking is **additive and idempotent**: sending the same link again changes nothing, and omitting `assistantId` on a later update leaves the source's existing links untouched. Upsert never *removes* a link &mdash; to detach a source from an AI Employee, use [Remove Knowledge in unlink mode](#step-9-remove-knowledge-and-offboard).

---

## Step 5: Add knowledge as a file

Most reference material starts life as a document &mdash; a PDF handbook, a spreadsheet price list, a Word policy. Files are not sent inline. There are **three steps**, and it helps to understand up front why:

1. **Ask Vendasta for a place to put the file.** You call **Create Knowledge File Upload** and get back a temporary, secure upload URL and an **upload handle**.
2. **Upload the file's bytes directly to that URL** with an HTTP `PUT`. The bytes go straight to storage, not through the API.
3. **Tell the AI Employee about the uploaded file.** You call **Upsert Knowledge** again, this time referencing the upload handle from step 1.

The file only becomes knowledge at step 3. Steps 1 and 2 just get the bytes into place.

### 5a. Request an upload URL

Provide the file name (with its extension) and its MIME type. The supported formats are PDF, Word (`.docx`), PowerPoint (`.pptx`), Excel (`.xlsx`/`.xls`), images (`.jpg`, `.png`, processed with OCR), HTML, plain text, Markdown, CSV, and JSON/JSONL.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/file-upload",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567",
    "fileName": "handbook.pdf",
    "contentType": "application/pdf"
  }
}
```

The response contains everything you need for the next step:

```json
{
  "uploadUrl": "https://storage.googleapis.com/...&X-Goog-Signature=...",
  "uploadHandle": "uh_0c1ffbf9...",
  "requiredHeaders": {
    "Content-Type": "application/pdf",
    "X-Goog-Content-Length-Range": "0,52428800"
  },
  "expiresAt": "2026-07-23T19:30:00Z"
}
```

- `uploadUrl` &mdash; where you `PUT` the bytes in step 5b.
- `uploadHandle` &mdash; the token you reference in step 5c. It is tied to this account group and to your credentials, and it **expires** at `expiresAt`.
- `requiredHeaders` &mdash; headers you must send with the `PUT`, **verbatim**. They are baked into the signed URL; a `PUT` whose headers don't match is rejected by storage.

### 5b. Upload the bytes

`PUT` the raw file to `uploadUrl`, sending each header from `requiredHeaders` exactly as given. This request goes to Google Cloud Storage, not to `prod.apigateway.co`, and it carries **no** `Authorization` header &mdash; the signed URL is the authorization.

```bash
curl -X PUT \
  -H "Content-Type: application/pdf" \
  -H "X-Goog-Content-Length-Range: 0,52428800" \
  --data-binary @handbook.pdf \
  "https://storage.googleapis.com/...&X-Goog-Signature=..."
```

A successful upload returns `200 OK` with an empty body. Files are capped at **50 MB**. Do the `PUT` before `expiresAt`; an expired URL is rejected and you must request a new one.

### 5c. Turn the uploaded file into knowledge

Call **Upsert Knowledge** with a `file` content block referencing the `uploadHandle`. As in Step 4, omitting `knowledgeSourceId` **creates** a new source.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/upsert",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567",
    "assistantId": "ASSISTANT-A1B2",
    "name": "Employee handbook",
    "content": {
      "file": {
        "uploadHandle": "uh_0c1ffbf9...",
        "fileName": "handbook.pdf",
        "mimeType": "application/pdf"
      }
    }
  }
}
```

The response returns a `knowledgeSourceId`, just like the text case. **Store it.** The handle is single-use for this ingest and expires; the `knowledgeSourceId` is the durable handle you keep.

---

## Step 6: Check whether the knowledge is ready

Creating a source starts processing in the background; the content is **not** available to the AI Employee until processing finishes. Check progress with **Get Knowledge Status**, using the `knowledgeSourceId` you stored.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/status",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567",
    "knowledgeSourceId": "KS-7f3c1a90"
  }
}
```

```json
{
  "status": {
    "state": "TRAINING_STATE_IN_PROGRESS",
    "totalUnits": "12",
    "completedUnits": "5",
    "errorCode": "TRAINING_ERROR_CODE_INVALID"
  }
}
```

`state` is the field to branch on:

| `state` | Meaning |
| --- | --- |
| `TRAINING_STATE_QUEUED` | Accepted, not started yet. |
| `TRAINING_STATE_IN_PROGRESS` | Processing; `completedUnits` / `totalUnits` show progress. |
| `TRAINING_STATE_DONE` | Ready &mdash; the AI Employee can use it. |
| `TRAINING_STATE_ERRORED` | Failed &mdash; read `errorCode` for why. |

`errorCode` is only meaningful when `state` is `TRAINING_STATE_ERRORED`; ignore it otherwise. The two file-processing failures you may see are `TRAINING_ERROR_CODE_LARGE_FILE_CONTENT` (the content is too large to process) and `TRAINING_ERROR_CODE_UNPROCESSABLE_CONTENT` (the content couldn't be parsed &mdash; for example a corrupt or empty file).

To wait for readiness, **poll**: call this endpoint every few seconds until `state` is `TRAINING_STATE_DONE` or `TRAINING_STATE_ERRORED`. Don't poll in a tight loop &mdash; a short delay between checks is plenty.

---

## Step 7: Update existing knowledge

When your source material changes, push the change to the **same** source by sending its stored `knowledgeSourceId`. This updates the existing source in place instead of creating a duplicate.

Updating text:

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/upsert",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567",
    "knowledgeSourceId": "KS-7f3c1a90",
    "content": {
      "text": {
        "text": "Customers may return unused items within 45 days for a full refund..."
      }
    }
  }
}
```

Updating a file works the same way: run the [file upload flow](#step-5-add-knowledge-as-a-file) again to get a fresh `uploadHandle`, then upsert with the stored `knowledgeSourceId` set. The new file replaces the source's current file.

A few things to know when updating:

- **The id is mandatory for an update.** An upsert *without* `knowledgeSourceId` always creates a new source &mdash; even if the `name` or file name is identical to one that already exists. There is no server-side de-duplication. An id-less re-push is how accidental duplicates happen.
- **A source's kind is fixed.** A source created as text stays text; one created from a file stays a file. The `content` you send on update must match the kind it was created with.
- **Omitted fields are left unchanged.** Leave `name` empty to keep the current name; omit `content` to change only, say, the links (see below). Omit `assistantId` to leave the existing AI Employee links untouched.

---

## Step 8: Recover a lost id, or clean up a duplicate

If a script crashes between creating a source and storing its id &mdash; or you discover a duplicate &mdash; you recover through **List Knowledge**, which returns every knowledge source in the account's knowledge base.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/list",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567"
  }
}
```

```json
{
  "knowledgeSources": [
    {
      "knowledgeSourceId": "KS-7f3c1a90",
      "name": "Returns policy",
      "fileName": "",
      "configType": "KNOWLEDGE_CONFIG_TYPE_CUSTOM_DATA",
      "status": { "state": "TRAINING_STATE_DONE" },
      "created": "2026-07-23T18:00:00Z",
      "updated": "2026-07-23T18:05:00Z"
    },
    {
      "knowledgeSourceId": "KS-11aa22bb",
      "name": "Employee handbook",
      "fileName": "handbook.pdf",
      "configType": "KNOWLEDGE_CONFIG_TYPE_FILE",
      "status": { "state": "TRAINING_STATE_DONE" },
      "created": "2026-07-23T18:10:00Z",
      "updated": "2026-07-23T18:12:00Z"
    }
  ],
  "pagingMetadata": {
    "hasMore": false,
    "nextCursor": "",
    "totalResults": "2"
  }
}
```

To find a source you lost the id for, match on the `name` you gave it (text sources) or the `fileName` (file sources). Once you have the `knowledgeSourceId`, you can [update](#step-7-update-existing-knowledge) it or [remove](#step-9-remove-knowledge-and-offboard) the duplicate.

You can also narrow the list to a single AI Employee by adding an `assistantId` to the request body &mdash; only sources linked to that AI Employee are returned.

> **`configType`** tells you what kind of source each entry is: `KNOWLEDGE_CONFIG_TYPE_CUSTOM_DATA` (text you supplied), `KNOWLEDGE_CONFIG_TYPE_FILE` (an uploaded file), `KNOWLEDGE_CONFIG_TYPE_WEBSITE` (scraped from a website), and `KNOWLEDGE_CONFIG_TYPE_BUSINESS_PROFILE` (the platform-managed Business Profile). The list returns **all** of these, including sources created by hand in the dashboard and the built-in Business Profile. Reconcile against your own stored ids &mdash; **manage only the ones you created.**

---

## Step 9: Remove knowledge, and offboard

**Remove Knowledge** does one of two distinct things, and you must say which by choosing a mode. It never guesses.

**Unlink** detaches a source from one or more AI Employees but keeps the source itself. Use this when a piece of knowledge should stop being used by a particular AI Employee but still exists for others.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/remove",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567",
    "knowledgeSourceId": "KS-7f3c1a90",
    "unlink": {
      "assistantIds": ["ASSISTANT-A1B2"]
    }
  }
}
```

**Delete** removes the source entirely.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/remove",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567",
    "knowledgeSourceId": "KS-7f3c1a90",
    "deleteSource": {}
  }
}
```

`deleteSource` is an empty object &mdash; its **presence** is the instruction to delete. Send exactly one of `unlink` or `deleteSource` per request.

> Deletion happens in more than one step internally. If a remove call fails partway, don't blindly retry &mdash; [list the knowledge base](#step-8-recover-a-lost-id-or-clean-up-a-duplicate) first to see the current state, then act on what's actually there.

### Offboarding a customer

When you stop managing a business, remove **only what your integration created**:

1. Call **List Knowledge** for the account group (paging through every page &mdash; see below).
2. For each returned source whose `knowledgeSourceId` is in your own mapping table, call **Remove Knowledge** with `deleteSource`.
3. Ignore every id you don't recognise. Human-created sources and the platform's Business Profile are not yours to delete.

That is the whole offboarding loop: list, then remove your own ids.

---

## Paging through long lists

**List Knowledge**, **List Assistants**, and any other gRPC-gateway list share one paging contract. Read this carefully, because the intuitive stopping condition is **wrong**.

Every list response carries `pagingMetadata`:

```json
"pagingMetadata": {
  "hasMore": true,
  "nextCursor": "eyJvZmZzZXQiOjEwMH0=",
  "totalResults": "0"
}
```

To read every page:

1. Make the first request with no cursor.
2. If `hasMore` is `true`, make the next request with `pagingOptions.cursor` set to the `nextCursor` from the response.
3. Repeat until `hasMore` is `false`. The final page carries **no** `nextCursor`.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/v1/knowledge/list",
  "headers": {
    "Authorization": "Bearer <Access Token with 'knowledge' scope>",
    "Content-Type": "application/json"
  },
  "body": {
    "accountGroupId": "AG-1234567",
    "pagingOptions": {
      "cursor": "eyJvZmZzZXQiOjEwMH0=",
      "pageSize": "50"
    }
  }
}
```

> **Stop on `hasMore`, not on page size.** A page may come back with fewer items than you asked for and *still* have `hasMore: true`. If you stop as soon as a short page arrives, you will silently miss knowledge sources. The only correct signal that you've reached the end is `hasMore: false`. Likewise, don't treat `totalResults` as authoritative &mdash; it is `0` on APIs that don't compute a total.

---

## Knowledge you cannot manage

One kind of knowledge is deliberately **not** reachable through this API: knowledge that was **shared in** to the account by a partner (rather than created on the account itself). This is by design &mdash; an integration can never reach across and change content another party owns.

Concretely:

- **List Knowledge omits shared-in sources.** They won't appear in the list, so the offboarding loop never sees an id it isn't allowed to touch.
- **Trying to mutate one returns `NotFound`.** If you somehow reference a shared-in source's id in an upsert, status, or remove call, you'll get a `NotFound` error. That is expected &mdash; it is not a bug and not a sign the source is missing.

Everything else in this guide &mdash; text, files, updates, removal &mdash; applies only to sources that live directly on the account, which are the ones your integration creates and the ones List Knowledge returns.

---

## Quick reference

| Task | Endpoint | Scope |
| --- | --- | --- |
| List businesses (account groups) | `GET /platform/businessLocations` | `business` |
| List AI Employees on an account | `POST /grpc/v1/assistants/list` | `ai-assistant` |
| Create or update knowledge | `POST /grpc/v1/knowledge/upsert` | `knowledge` |
| Request a file upload URL | `POST /grpc/v1/knowledge/file-upload` | `knowledge` |
| Check processing status | `POST /grpc/v1/knowledge/status` | `knowledge` |
| List knowledge sources | `POST /grpc/v1/knowledge/list` | `knowledge` |
| Unlink or delete knowledge | `POST /grpc/v1/knowledge/remove` | `knowledge` |

All gRPC-gateway endpoints are `POST` with a JSON body, are based at `https://prod.apigateway.co/grpc`, and take the `Authorization: Bearer <token>` header. Full field-by-field references are in the [AI Employees](../../../openapi/openapi_external_docs/ai_assistants.openapi.json) and [AI Knowledge](../../../openapi/openapi_external_docs/embeddings.openapi.json) API specifications.
