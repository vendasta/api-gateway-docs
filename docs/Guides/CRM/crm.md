# CRM External API Developer Guide


## Overview

The Vendasta CRM External API provides a RESTful interface for managing customer relationship data including contacts, companies, activities, and custom objects.

**Base URL (env-specific):**

* Production: `https://prod.apigateway.co/org`
* Demo: `https://demo.apigateway.co/org`

**Canonical request URL format:**
`https://{env}.apigateway.co/org/{namespace}/{resourceTypeCode}/...`

**Canonical list URL format:**
`https://{env}.apigateway.co/org/list/{namespace}/{resourceTypeCode}`

**API Status:** `Trusted Tester`

---

## Authentication

Authenticate using a Service Account access token:

```
Authorization: Bearer YOUR_ACCESS_TOKEN
```
> For details on creating an access token see the [Authorization guide](../Authorization/Authorization.md).

---

## Understanding Namespaces

The same CRM infrastructure serves both **Partners (agencies)** and the **SMB/SME clients** they manage. The namespace determines *data ownership, isolation, and permissions*.

### Partner namespace

**ID format:** Partner ID (alphanumeric 2–4 character ID, e.g., `ABC1`)

**Use when:**

- Managing prospects and clients across your agency’s book of business
- Agency-level pipelines, proposals, and sales operations
- Partner administrative and operational data

### SMB namespace

**ID format:** Account Group ID (e.g., `AG-XYZ789`)

**Use when:**

- Managing an individual client’s customers and opportunities
- Creating records tied to a specific business account
- Enabling SMB self-service CRM usage

### Schema differences

While the core schema is shared, namespaces differ in:

- Custom objects and custom fields
- Dropdown options
- Permissions and access control
- Data isolation boundaries

> Always retrieve schemas dynamically using **Get field schema** to ensure correctness.

---

## Core Endpoints

### Resource types

| Code            | Description                   |
| --------------- | ----------------------------- |
| `contacts`      | Individual people             |
| `companies`     | Organizations                 |
| `activities`    | Tasks, meetings, calls, notes |
| `customobjects` | User-defined objects          |
| `accounts`      | Platform business accounts    |

### Endpoint patterns

Standard operations:

```
/{namespace}/{resourceTypeCode}/...
```
List operation (authoritative):

```
/list/{namespace}/{resourceTypeCode}
```
---

## Working with Field Schemas

Before reading or writing data, retrieve the field schema for your namespace.

### Get field schema

**Endpoint:** `GET /{namespace}/{resourceTypeCode}/meta/fields`

```bash
curl -X GET "https://prod.apigateway.co/org/P-ABC123/contacts/meta/fields" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Field ID rules

**Always use the field** `id` **returned by Get field schema.**

- **System, standard, and extension fields** have *locked* field IDs managed by Vendasta
- **Custom fields** use the field ID defined at creation time by the user
- Field *namespace* (`system__`, `standard__`, `custom__`, etc.) does not change how the API is called

> The schema response is the source of truth for all writable and searchable field IDs.

### Field naming prefixes

- `standard__` — Standard CRM fields
- `system__` — System-managed fields
- `custom__` — User-defined fields
- `platform__` — Platform-specific fields
- `{integration}__` — Integration-specific fields



### **Field Types**

The API supports various field types:

| **TypeDescriptionExample Value** |                        |                           |
| -------------------------------- | ---------------------- | ------------------------- |
| `string`                         | Text values            | `"John Doe"`              |
| `integer`                        | Whole numbers          | `42`                      |
| `boolean`                        | True/false             | `true`                    |
| `date`                           | Date only              | `"2024-01-15"`            |
| `date-time`                      | Date and time          | `"2024-01-15T14:30:00Z"`  |
| `email`                          | Email address          | `"user@example.com"`      |
| `phone`                          | Phone number           | `"+1-555-123-4567"`       |
| `currency`                       | Monetary value         | See currency format below |
| `array`                          | List of values         | `["tag1", "tag2"]`        |
| `dropdown`                       | Enumerated values      | `"option1"`               |
| `geopoint`                       | Geographic coordinates | See geopoint format below |

**Currency Format:**

```json
{
  "id": "standard__company_annual_revenue",
  "value": {
    "currencyCode": "USD",
    "amount": 50000
  }
}
```
**Geopoint Format:**

```json
{
  "id": "standard__company_primary_location_lat_long",
  "value": "49.8951,-97.1384"
}
```
---

## Upserting Records (Create + Update)

Upsert (`PATCH`) is the primary write operation. It creates a record if none is found, or updates an existing record if a match is found.

### Upsert endpoint

`PATCH /{namespace}/{resourceTypeCode}`

### searchExisting

`searchExisting` controls **record matching, de-duplication, and update targeting**.

#### How matching works

When multiple fields are provided in `searchExisting`, the system evaluates them **sequentially, not as a logical AND**:

1. Fields are evaluated **in the order listed**.
2. The system searches for an existing record matching the **first field**.
3. If a match is found, that record is updated and the search stops.
4. If no match is found, the system proceeds to the **next field**, and so on.
5. If none of the fields match, a **new record is created**.

**Effectively:** `searchExisting` behaves like a **logical OR with priority order**. The *first match wins*.

This allows you to "stitch" records together safely (for example: *try external ID first, fall back to email*).

#### Field requirements

- Any field listed in `searchExisting` **must also appear in the** `fields` **array** with a value.
- Fields not present in `fields` are ignored during matching.

#### Automatic default fields

If you do not include system defaults, the API appends them **after your provided fields**:

- **Contacts:** `standard__email`
- **Companies:** `external_id`, `standard__company_name`
- **Custom objects:** `external_id`, `standard__customobject_name`

The final evaluation order is:

1. Your provided `searchExisting` fields (in order)
2. System default fields (appended)

#### Practical example

If you send:

```json 
{
  "fields": [
    { "id": "external_id", "value": "12345" },
    { "id": "standard__email", "value": "user@example.com" }
  ],
  "searchExisting": ["external_id", "standard__email"]
}
```
The system will:

1. Search by `external_id = 12345`
2. If not found, search by `standard__email = user@example.com`
3. If still not found, create a new record

#### Merge strategies

| Operation            | Behaviour                |
| -------------------- | ------------------------ |
| `always_overwrite`   | Replace existing value   |
| `overwrite_if_empty` | Set only if empty        |
| `overwrite_if_newer` | Replace if newer (dates) |
| `combine`            | Append/merge lists       |

---

## Two-Way Sync Strategies

Two-way sync requires **stable identity, deterministic matching, and clear source-of-truth rules**.

### 1. Identity: external\_id as the anchor

- Always set `system__*_external_id` to your system's identifier
- Always include `searchExisting: ["external_id"]`
- Store the Vendasta system ID in your external system for fast lookups

This guarantees idempotent writes and prevents record drift.

### 2. Directional ownership

Define field-level ownership rules:

- **External system owns:** name, email, phone → use `always_overwrite`
- **CRM owns:** notes, lifecycle stage → use `overwrite_if_empty`

This prevents sync loops and unintentional overwrites.

### 3. Change detection

Recommended approaches:

- Track `system__*_updated` timestamps
- Compare hash/signature of last-synced payload
- Sync only changed fields

### 4. Conflict resolution

When both systems change the same field:

- Prefer latest timestamp
- Or designate a system of record per field
- Log and surface conflicts instead of silently overwriting

### 5. Deletion handling

Avoid hard deletes in two-way sync:

- Use soft-delete flags where possible
- Or mark records as inactive
- Explicit deletes should be one-directional and intentional

---

## Listing Records

**Endpoint:** `POST /list/{namespace}/{resourceTypeCode}`

Filtering uses the same `fields` structure as upsert, with filter operations instead of merge strategies.

Pagination is cursor-based and required for large datasets.

---

## Deleting Records

**Endpoint:** `DELETE /{namespace}/{resourceTypeCode}/{id}`

### ID rules

- `{id}` refers to the **record identifier**, not a field ID
- Use the system ID (e.g., `CON-123`) or a supported external ID value
- Field schemas indicate which identifiers are accepted

**Important:** Deletes are permanent and cannot be recovered.

---

## Associations

Associations link related records (contacts, companies, activities, custom objects).

Best practices:

- Validate referenced records exist
- Use `combine` to avoid overwriting existing associations
- Prefer external IDs for consistency

### **Association Types**

| **Association FieldUsed ForLinks** |                |                                                          |
| ---------------------------------- | -------------- | -------------------------------------------------------- |
| `contact_associations`             | Contacts       | Contact → Company, Contact → Activity                    |
| `company_associations`             | Companies      | Company → Contact, Company → Company, Company → Activity |
| `activity_associations`            | Activities     | Activity → Contact, Activity → Company                   |
| `custom_object_associations`       | Custom Objects | Custom Object → any type                                 |

### **Association Structure**

```json
{
  "associations": [
    {
      "id": "record_id",
      "type": "record_type",
      "subtype": "subtype_if_applicable"
    }
  ]
}
```
**Fields:**

- `id`: The external\_id or system ID of the record to associate
-  `type`: The type of record (`Contact`, `Company`, `Activity`, `CustomObject`)
-  `subtype`: For activities, the specific type (Task, Meeting, Call, etc.)

### **Company Primary Contact**

The primary contact is a special association that identifies the main point of contact for a company.

#### **Setting a Primary Contact**

When creating or updating a company, associate a contact:

```json
{
  "fields": [
    {
      "id": "standard__company_name",
      "value": "Acme Corporation",
      "operation": "always_overwrite"
    }
  ],
  "associations": [
    {
      "id": "CON-123456",
      "type": "Contact"
    }
  ]
}
```
---

#### **Retrieving Primary Contact**

When listing or fetching companies, include the association field:

```json
{
  "returnFields": [
    "standard__company_name",
    "standard__contact_primary_company_id"
  ]
}
```
### **Company-to-Company Relationships**

Create hierarchical or related company structures (parent companies, subsidiaries, partners, etc.).

#### **Example: Parent-Subsidiary Relationship**

```json
{
  "fields": [
    {
      "id": "standard__company_name",
      "value": "Acme Subsidiary Inc",
      "operation": "always_overwrite"
    }
  ],
  "associations": [
    {
      "id": "COM-PARENT-123",
      "type": "Company"
    }
  ]
}
```
**Use Cases:**

- **Corporate Hierarchies:** Parent company → subsidiaries
- **Partner Networks:** Companies that work together
- **Vendor Relationships:** Companies and their suppliers
- **Referral Sources:** Track which company referred which

### **Contact-to-Company Associations**

Link contacts to multiple companies:

```json
{
  "fields": [
    {
      "id": "standard__first_name",
      "value": "John",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__last_name",
      "value": "Doe",
      "operation": "always_overwrite"
    }
  ],
  "associations": [
    {
      "id": "COM-123",
      "type": "Company"
    },
    {
      "id": "COM-456",
      "type": "Company"
    }
  ]
}
```
### **Activity Associations**

Link activities to contacts and/or companies:

```json
{
  "subtype": "Task",
  "fields": [
    {
      "id": "standard__activity_name",
      "value": "Follow up call",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__task_due_date",
      "value": "2024-12-31T17:00:00Z",
      "operation": "always_overwrite"
    }
  ],
  "associations": [
    {
      "id": "CON-123",
      "type": "Contact"
    },
    {
      "id": "COM-456",
      "type": "Company"
    }
  ]
}
```
### **Custom Object Associations**

Link custom objects to standard objects:

```json
{
  "fields": [
    {
      "id": "standard__customobject_name",
      "value": "Project Alpha",
      "operation": "always_overwrite"
    }
  ],
  "associations": [
    {
      "id": "COM-789",
      "type": "Company"
    },
    {
      "id": "CON-456",
      "type": "Contact"
    }
  ]
}
```
### **Association Best Practices**

1. **Validate IDs:** Ensure associated records exist before creating associations
2. **Use External IDs:** Reference records by external ID for consistency
3. **Combine Operations:** Use `combine` merge strategy to add associations without removing existing ones
4. **Bi-directional Links:** Consider if you need associations in both directions
5. **Cleanup:** Remove associations when relationships end

---

## Custom Fields and Custom Objects

Custom fields extend standard objects. Custom objects define entirely new record types.

- Custom fields appear in schema after creation
- Custom objects behave like first-class resources
- Both support associations, search, list, and external IDs

---

## External IDs

External IDs are **foundational** for integrations.

### Why they matter

- Enable idempotent upserts
- Power two way sync
- Prevent duplicates
- Preserve relationships across systems

### Best practices

- Always set and search by external ID
- Treat external IDs as case sensitive strings
- Namespace IDs by source system
- Never overwrite external IDs unintentionally

---

## Source Tracking

Vendasta CRM maintains **two distinct source concepts** for each record to support both **attribution** and **auditing**.


### **Record Source (Latest Update Mechanism)**

The **record source** indicates *how the record was most recently created or updated*. It answers the question:

*“What tool or integration last touched this record?”*

Typical record source values include:
* `CRM UI`
* `Bulk Import`
* `Form`
* `Web Chat`
* `External API`
* Integration-specific sources (e.g., `Salesforce Sync`)

This field is updated **on every write** and is primarily used for:
* Change auditing
* Debugging integrations
* Understanding which system is actively modifying data


### **Original Source (First Entry Point)**

The **original source** represents the *very first way the record entered the CRM*. It answers the question:

*“Where did this record originally come from?”*

Examples:
* A lead created via a website form
* A contact created through web chat
* An initial bulk import
* A first-time integration sync

Once set, the original source **does not change**, even if the record is later updated by other tools or integrations.

This makes it ideal for:
* Lead attribution
* Campaign analysis
* Channel performance reporting


### **Drill-down Fields (Additional Context)**

**Drill-down fields** provide *extra detail* about the source.

Examples:
* For `Web Chat`: specific widget, campaign, or chat flow
* For `Bulk Import`: file name, import job ID, or user
* For integrations: process name, version, or sync identifier

Drill-downs add context without changing the high-level source classification.


### **Default Behavior for External API Writes**

When records are created or updated via the External API and no source fields are explicitly provided:
* **Record source** is set to `External API`
* **Original source** is set to `External API` *only if it was previously empty*
* **Drill-down** fields are populated with the authenticated user ID


### **When to Override Source Fields**

Override source fields intentionally when:
* Performing bulk imports and want clear attribution
* Syncing from a named external system
* Tracking campaigns, widgets, or ingestion pipelines

Use merge strategies appropriately:
* **Original source:** `overwrite_if_empty`
* **Record source:** `always_overwrite`

---

## Error Handling

Errors return a consistent structure:

```json
{ "error": "Detailed error message" }
```
Validate namespaces, resource types, field IDs, and authentication tokens when errors occur.
