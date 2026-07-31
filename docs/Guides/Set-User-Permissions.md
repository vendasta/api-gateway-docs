---
tags: [permissions, users, crm, scopes, serviceAccount]
---
# Set a User's CRM Permissions

This guide shows how a **partner service account** can set a platform user's CRM
permission scopes programmatically, instead of a person setting them on the Partner
Center "My Team" page.

A common use case: after you create a user for an agent, restrict that agent to only
the records assigned to them by setting their contacts and companies scope to
"entities assigned to the user".

> For how to obtain an access token, see the [Authorization guide](../Authorization/Authorization.md).

## Permission scopes

Each CRM object type (contacts, companies, opportunities, custom objects, activities)
has three independent scopes — `readScope`, `writeScope`, and `manageScope` — and each
is set to one of the following values:

| Value | Name          | Meaning                                                  |
|-------|---------------|----------------------------------------------------------|
| `1`   | None          | No access                                                |
| `2`   | Own           | Only records assigned to the user                        |
| `3`   | Own + Unowned | Records assigned to the user, plus unassigned records    |
| `4`   | All           | Every record                                             |

"Entities assigned to the user" is `2` (Own).

## Step 1 — Wait for the user's permissions to exist (retry on 404)

When a user is first created, they are synced into the permissions service
**asynchronously**. Immediately after creating a user, `GetPermissions` can return
`404 Not Found` because the record does not exist yet.

Poll `GetPermissions` until it returns a permissions object. Treat a `404` as
"not ready yet — wait and retry"; any other error is a real error.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/platformusers.v1.PermissionsService/GetPermissions",
  "headers": {
    "Authorization": "Bearer <partner service account access token>",
    "Content-Type": "application/json"
  },
  "body": {
    "partnerId": "VUNI",
    "userId": "U-2a7aa9dd-e435-46c4-b3b8-d4c53b664ed6"
  }
}
```

## Step 2 — Set the scopes (always send a field mask)

Call `UpdatePermissions` with the scopes you want to change **and a `fieldMask`**.

The field mask is required. The permissions entity also stores the user's
`namespaces` (which businesses they belong to). An update sent **without** a field
mask overwrites the entire entity and erases that list. The field mask limits the
write to only the fields you name, leaving everything else untouched.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/grpc/platformusers.v1.PermissionsService/UpdatePermissions",
  "headers": {
    "Authorization": "Bearer <partner service account access token>",
    "Content-Type": "application/json"
  },
  "body": {
    "permissions": {
      "partnerId": "VUNI",
      "userId": "U-2a7aa9dd-e435-46c4-b3b8-d4c53b664ed6",
      "contacts": {
        "readScope": 2,
        "writeScope": 2,
        "manageScope": 1
      }
    },
    "fieldMask": {
      "paths": ["contact_permissions"]
    }
  }
}
```

This sets the user's contacts to read and write only their own assigned records, and
leaves everything else on the user — including their namespaces — untouched.

### Field mask paths

Field mask paths use the stored field names, which differ from the request field names:

| To change     | Field mask path              |
|---------------|------------------------------|
| contacts      | `contact_permissions`        |
| companies     | `company_permissions`        |
| opportunities | `opportunity_permissions`    |
| custom objects| `custom_object_permissions`  |
| activities    | `activity_permissions`       |

## Notes

- The user must already hold the appropriate role in the target account groups for
  these permissions to take effect.
- `readScope`, `writeScope`, and `manageScope` are independent — set each one explicitly.
