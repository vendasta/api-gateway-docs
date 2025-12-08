# CRM External API Developer Guide

## Overview

The Vendasta CRM External API provides a RESTful interface for managing customer relationship data including contacts, companies, activities, and custom objects. 

**Base URL:**
- Production: `https://prod.apigateway.co/org`
- Demo: `https://demo.apigateway.co/org`

**API Status:** `Trusted Tester`

---

## Authentication

All API requests must include proper authentication headers. The API uses Service Account based authentication. Ensure you have valid credentials and include them in your requests.

```http
Authorization: Bearer YOUR_ACCESS_TOKEN
```
> For info on creating an access token see the [Authorization guide](../Authorization/Authorization.md). *For quick tests use the 'Generate Token' button at the top of the Developer Center, and when exploring the openapi contracts send test requests directly from the documentation.*

---

## Understanding Namespaces

The same CRM infrastructure serves both Partners and the SMB/SME clients that they serve.

There are some schema differences between the Partner CRM, and the Business App CRM

To determine where your data is stored and accessed the namespace must be appended to the base url for all operations: `env.apigateway.co/org/{namespace}`

### Partner Namespace (Partner Center)

**Format:** Partner ID (Alpha-numeric 2-4 character ID: e.g., `ABC1` - find this in the top left when in the Partner Center)

**Use Cases:**
- Data accessible across the partner organization
- Partner-level contacts and companies
- Partner administrative records
- Data shared across all account groups under the partner

**When to Use:**
- Storing partner sales team contacts
- Managing partner-level company information
- Creating shared resources accessible to all partner users
- Administrative and operational data

### SMB Namespace (Business App)

**Format:** Account Group ID (e.g., `AG-XYZ789`)

**Use Cases:**
- Data specific to individual small/medium business accounts
- End-customer business information
- Account-specific contacts and relationships
- Business application data

**When to Use:**
- Storing end-customer contacts for a specific business
- Managing business-specific company relationships
- Creating records tied to individual business accounts
- Customer-facing application data

### Schema Differences

While the core field structure is similar across namespaces, there are some differences:

1. **Custom Fields:** Each namespace can have its own custom field definitions
2. **Dropdown Options:** Dropdown field values may differ between namespaces
3. **Permissions:** Access control varies based on namespace type
4. **Data Isolation:** Partner namespace data is separate from SMB namespace data

### Determining the Correct Namespace

```javascript
// Partner-level operation (accessible across organization)
const partnerNamespace = "ABC0";

// Business-specific operation (isolated to single account group)
const smbNamespace = "AG-XYZ789";
```

**Decision Tree:**
- Is this data for your sales team or partner operations? → Use Partner Namespace
- Is this data for a specific end-customer business? → Use SMB Namespace
- Should this be shared across all businesses? → Use Partner Namespace
- Is this customer-facing data? → Use SMB Namespace

---

## Core Endpoints

### Resource Types

The API supports the following resource types:

| Resource Type Code | Description |
|-------------------|-------------|
| `contacts` | Individual people/contacts |
| `companies` | Organizations/businesses |
| `activities` | Tasks, meetings, calls, notes, emails |
| `customobjects` | User-defined object types |
| `accounts` | Business accounts (special handling) |

### Endpoint Pattern

All endpoints follow this consistent pattern:

```
/{namespace}/{resourceTypeCode}/...
```

Example:
```
/P-ABC123/contacts/meta/fields
/AG-XYZ789/companies
```

---

## Working with Field Schemas

Before working with CRM records, you need to understand the available fields for your namespace.

### Get Field Schema

Retrieves all available fields for a resource type in a specific namespace.

**Endpoint:** `GET /{namespace}/{resourceTypeCode}/meta/fields`

**Example Request:**

```bash
curl -X GET "https://prod.apigateway.co/org/P-ABC123/contacts/meta/fields" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Example Response:**

```json
{
  "type": "object",
  "title": "CRM FieldSchema of Contact",
  "description": "CRM Field Schema of resourceType Contact",
  "properties": {
    "standard__first_name": {
      "type": "string",
      "title": "First Name",
      "description": "The given name of the contact"
    },
    "standard__last_name": {
      "type": "string",
      "title": "Last Name"
    },
    "standard__email": {
      "type": "string",
      "format": "email",
      "title": "Email Address"
    },
    "system__contact_id": {
      "type": "string",
      "title": "Contact ID",
      "readOnly": true
    },
    "system__contact_external_id": {
      "type": "string",
      "title": "External ID",
      "description": "External identifier for the record"
    },
    "custom__my_custom_field": {
      "type": "string",
      "title": "My Custom Field"
    }
  }
}
```

### Field Naming Conventions

Fields follow a standardized naming pattern with prefixes:

- **`standard__`** - Standard CRM fields (cross-system)
- **`system__`** - System-managed fields (often read-only)
- **`custom__`** - Custom fields defined for your namespace
- **`platform__`** - Platform-specific fields
- **`{integration}__`** - Integration-specific fields (e.g., `yesware__`)

### Get Field Options

For dropdown fields, retrieve the available options:

**Endpoint:** `GET /{namespace}/{resourceTypeCode}/meta/fields/{fieldId}/options`

**Example Request:**

```bash
curl -X GET "https://prod.apigateway.co/org/P-ABC123/contacts/meta/fields/standard__contact_lifecycle_stage/options" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Example Response:**

```json
{
  "options": [
    {
      "name": "Lead",
      "value": "Lead"
    },
    {
      "name": "Prospect",
      "value": "Prospect"
    },
    {
      "name": "Customer",
      "value": "Customer"
    }
  ]
}
```

### Field Types

The API supports various field types:

| Type | Description | Example Value |
|------|-------------|---------------|
| `string` | Text values | `"John Doe"` |
| `integer` | Whole numbers | `42` |
| `boolean` | True/false | `true` |
| `date` | Date only | `"2024-01-15"` |
| `date-time` | Date and time | `"2024-01-15T14:30:00Z"` |
| `email` | Email address | `"user@example.com"` |
| `phone` | Phone number | `"+1-555-123-4567"` |
| `currency` | Monetary value | See currency format below |
| `array` | List of values | `["tag1", "tag2"]` |
| `dropdown` | Enumerated values | `"option1"` |
| `geopoint` | Geographic coordinates | See geopoint format below |

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

## Upserting Records

The upsert operation (PATCH) creates a new record if it doesn't exist, or updates an existing record if found. This is the recommended way to create and update records.

### Upsert Endpoint

**Endpoint:** `PATCH /{namespace}/{resourceTypeCode}`

### Basic Upsert Example

```bash
curl -X PATCH "https://prod.apigateway.co/org/P-ABC123/contacts" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": [
      {
        "id": "standard__first_name",
        "value": "Jane",
        "operation": "always_overwrite"
      },
      {
        "id": "standard__last_name",
        "value": "Smith",
        "operation": "always_overwrite"
      },
      {
        "id": "standard__email",
        "value": "jane.smith@example.com",
        "operation": "always_overwrite"
      }
    ],
    "searchExisting": [
      "standard__email"
    ],
    "returnFields": [
      "system__contact_id",
      "standard__first_name",
      "standard__email"
    ]
  }'
```

### Request Body Structure

```json
{
  "subtype": "",
  "fields": [
    {
      "id": "field_id",
      "value": "field_value",
      "operation": "merge_strategy"
    }
  ],
  "searchExisting": ["field_ids_to_search"],
  "returnFields": ["field_ids_to_return"],
  "associations": []
}
```

**Fields:**

- **`subtype`** (optional): For activities, specifies the type (Task, Meeting, Call, etc.)
- **`fields`** (required): Array of field objects to set
  - **`id`**: Field identifier (use external_id from schema)
  - **`value`**: The value to set
  - **`operation`**: Merge strategy (see below)
- **`searchExisting`** (optional): Fields to use for finding existing records
- **`returnFields`** (optional): Fields to include in response
- **`associations`** (optional): Related records to link

### Merge Strategies (Operations)

The `operation` field determines how values are merged when updating existing records:

| Operation | Behavior |
|-----------|----------|
| `always_overwrite` | Always replace existing value with new value |
| `overwrite_if_empty` | Only set value if field is currently empty |
| `overwrite_if_newer` | Replace if new value is more recent (for date fields) |
| `combine` | Merge values (for arrays/lists, adds to existing) |

**Examples:**

```json
// Always update the name, even if one exists
{
  "id": "standard__first_name",
  "value": "Jane",
  "operation": "always_overwrite"
}

// Only set email if contact doesn't have one
{
  "id": "standard__email",
  "value": "jane@example.com",
  "operation": "overwrite_if_empty"
}

// Add tags without removing existing ones
{
  "id": "standard__tags",
  "value": ["new-tag", "another-tag"],
  "operation": "combine"
}
```

### Using SearchExisting for Updates

The `searchExisting` array specifies which fields to use when looking for existing records to update.

**Default Search Fields:**

If you don't specify `searchExisting`, the system uses defaults:

- **Contacts:** `standard__email`
- **Companies:** `standard__company_name`, `external_id`
- **Custom Objects:** `standard__customobject_name`, `external_id`

**Custom Search Fields:**

```json
{
  "fields": [
    {
      "id": "standard__email",
      "value": "john@example.com",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__phone_number",
      "value": "+1-555-0100",
      "operation": "always_overwrite"
    }
  ],
  "searchExisting": [
    "standard__email",
    "standard__phone_number"
  ]
}
```

**Important:** Fields listed in `searchExisting` must also be included in the `fields` array. The system only searches by fields that you're providing values for.

### Response Format

```json
{
  "fields": [
    {
      "id": "system__contact_id",
      "value": "CON-123456"
    },
    {
      "id": "standard__first_name",
      "value": "Jane"
    },
    {
      "id": "standard__email",
      "value": "jane.smith@example.com"
    }
  ]
}
```

---

## De-duplication Strategies

De-duplication prevents creating duplicate records by matching on specific field values. The CRM API provides flexible de-duplication through the `searchExisting` parameter.

### How De-duplication Works

1. **You provide data** in the `fields` array
2. **You specify match criteria** in `searchExisting`
3. **System searches** for existing records matching ALL criteria
4. **If found:** Updates existing record using merge strategies
5. **If not found:** Creates new record

### Common De-duplication Patterns

#### Pattern 1: Email-based De-duplication (Contacts)

The most common pattern for contacts - match by email address.

```json
{
  "fields": [
    {
      "id": "standard__first_name",
      "value": "John",
      "operation": "overwrite_if_empty"
    },
    {
      "id": "standard__last_name",
      "value": "Doe",
      "operation": "overwrite_if_empty"
    },
    {
      "id": "standard__email",
      "value": "john.doe@example.com",
      "operation": "always_overwrite"
    }
  ],
  "searchExisting": ["standard__email"]
}
```

**Use Case:** Importing contacts from multiple sources where email is the unique identifier.

#### Pattern 2: External ID De-duplication

Ideal when syncing from external systems that have their own IDs.

```json
{
  "fields": [
    {
      "id": "system__contact_external_id",
      "value": "SALESFORCE-12345",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__first_name",
      "value": "Jane",
      "operation": "always_overwrite"
    }
  ],
  "searchExisting": ["external_id"]
}
```

**Use Case:** Syncing data from Salesforce, HubSpot, or other CRM systems.

#### Pattern 3: Company Name De-duplication

For companies, match by name (and optionally other fields).

```json
{
  "fields": [
    {
      "id": "standard__company_name",
      "value": "Acme Corporation",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__company_website",
      "value": "https://acme.com",
      "operation": "overwrite_if_empty"
    }
  ],
  "searchExisting": ["standard__company_name"]
}
```

**Use Case:** Importing company data where company name is the primary identifier.

#### Pattern 4: Multi-field De-duplication

Combine multiple fields for more precise matching.

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
      "value": "Smith",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__company_name",
      "value": "Tech Solutions Inc",
      "operation": "always_overwrite"
    }
  ],
  "searchExisting": [
    "standard__first_name",
    "standard__last_name",
    "standard__company_name"
  ]
}
```

**Use Case:** When email isn't available but you want to avoid duplicates for "John Smith at Tech Solutions Inc."

#### Pattern 5: Phone Number De-duplication

Match contacts by phone number when email isn't available.

```json
{
  "fields": [
    {
      "id": "standard__phone_number",
      "value": "+1-555-0123",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__first_name",
      "value": "Bob",
      "operation": "overwrite_if_empty"
    }
  ],
  "searchExisting": ["standard__phone_number"]
}
```

### De-duplication Best Practices

1. **Choose Stable Identifiers:** Use fields that don't change often (email, external_id)
2. **Consider Data Quality:** Poor data quality reduces matching effectiveness
3. **Use External IDs:** When integrating with other systems, always set and search by external_id
4. **Test Matching Logic:** Verify your de-duplication works before bulk imports
5. **Handle Edge Cases:** Consider what happens with:
   - Missing values in match fields
   - Multiple records matching criteria
   - Case sensitivity in string matches
6. **Combine with Merge Strategies:** Use `overwrite_if_empty` to preserve existing data

### De-duplication vs. Explicit Updates

**Use De-duplication When:**
- You don't know if the record exists
- Importing data from external sources
- Running regular syncs
- You want the system to handle matching

**Use Explicit ID-based Updates When:**
- You already have the system__contact_id or system__company_id
- Performing targeted updates to known records
- Building real-time integrations with state management

---

## Listing Records

The list operation allows you to query and retrieve CRM records based on filter criteria.

### List Endpoint

**Endpoint:** `POST /list/{namespace}/{resourceTypeCode}`

### Basic List Example

```bash
curl -X POST "https://prod.apigateway.co/org/P-ABC123/contacts" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": [
      {
        "id": "standard__last_name",
        "value": "Smith",
        "operation": "IS"
      }
    ],
    "returnFields": [
      "system__contact_id",
      "standard__first_name",
      "standard__last_name",
      "standard__email"
    ],
    "page": {
      "cursor": "",
      "limit": 20
    }
  }'
```

### Request Body Structure

```json
{
  "subtype": "",
  "fields": [
    {
      "id": "field_id",
      "value": "filter_value",
      "operation": "FILTER_OPERATION"
    }
  ],
  "returnFields": ["field_ids"],
  "page": {
    "cursor": "",
    "limit": 20
  }
}
```

### Filter Operations

Different field types support different filter operations:

#### String Fields

| Operation | Description | Example |
|-----------|-------------|---------|
| `IS` | Exact match | `"Smith"` |
| `IS_NOT` | Not equal to | `"Smith"` |
| `IS_EMPTY` | Field has no value | N/A |
| `IS_NOT_EMPTY` | Field has a value | N/A |
| `IS_ANY` | Matches any of values | `["Smith", "Jones"]` |
| `IS_NOT_ANY` | Doesn't match any | `["Smith", "Jones"]` |
| `CONTAINS` | Contains substring | `"mit"` (matches "Smith") |
| `DOES_NOT_CONTAIN` | Doesn't contain | `"xyz"` |

#### Integer/Number Fields

| Operation | Description |
|-----------|-------------|
| `IS_EQUAL_TO` | Equal to value |
| `IS_NOT_EQUAL_TO` | Not equal to value |
| `IS_GREATER_THAN` | Greater than value |
| `IS_GREATER_THAN_OR_EQUAL_TO` | Greater than or equal |
| `IS_LESS_THAN` | Less than value |
| `IS_LESS_THAN_OR_EQUAL_TO` | Less than or equal |
| `IS_BETWEEN` | Between two values |
| `IS_NOT_BETWEEN` | Not between two values |
| `IS_EMPTY` | No value set |
| `IS_NOT_EMPTY` | Has a value |

#### Date/DateTime Fields

| Operation | Description |
|-----------|-------------|
| `IS` | Exact date match |
| `IS_NOT` | Not this date |
| `IS_BEFORE` | Before date |
| `IS_BEFORE_OR_ON` | Before or on date |
| `IS_AFTER` | After date |
| `IS_AFTER_OR_ON` | After or on date |
| `IS_BETWEEN` | Between two dates |
| `IS_NOT_BETWEEN` | Not between dates |
| `IS_EMPTY` | No date set |
| `IS_NOT_EMPTY` | Has a date |

#### Boolean Fields

| Operation | Description |
|-----------|-------------|
| `IS` | Exact boolean match |
| `IS_EMPTY` | No value set |
| `IS_NOT_EMPTY` | Has a value |

#### Array/List Fields (string_list, tags)

| Operation | Description |
|-----------|-------------|
| `IS_EMPTY` | Array is empty |
| `IS_NOT_EMPTY` | Array has values |
| `IS_ANY` | Contains any of the values |
| `IS_NOT_ANY` | Doesn't contain any values |
| `IS_ALL` | Contains all values |
| `IS_NOT_ALL` | Doesn't contain all values |

#### Phone Fields

| Operation | Description |
|-----------|-------------|
| `IS` | Exact match |
| `IS_NOT` | Not equal to |
| `IS_EMPTY` | No phone number |
| `IS_NOT_EMPTY` | Has phone number |
| `IS_VALID` | Valid phone format |
| `IS_NOT_VALID` | Invalid format |

### List Examples

#### Example 1: Find All Contacts with Last Name "Smith"

```json
{
  "fields": [
    {
      "id": "standard__last_name",
      "value": "Smith",
      "operation": "IS"
    }
  ],
  "returnFields": [
    "standard__first_name",
    "standard__last_name",
    "standard__email"
  ],
  "page": {
    "limit": 50
  }
}
```

#### Example 2: Find Companies Created After Specific Date

```json
{
  "fields": [
    {
      "id": "system__company_created",
      "value": "2024-01-01T00:00:00Z",
      "operation": "IS_AFTER"
    }
  ],
  "returnFields": [
    "standard__company_name",
    "system__company_created"
  ],
  "page": {
    "limit": 100
  }
}
```

#### Example 3: Find Contacts with Specific Tags

```json
{
  "fields": [
    {
      "id": "standard__tags",
      "value": ["vip", "customer"],
      "operation": "IS_ANY"
    }
  ],
  "returnFields": [
    "standard__first_name",
    "standard__email",
    "standard__tags"
  ]
}
```

#### Example 4: Find Contacts Missing Email Address

```json
{
  "fields": [
    {
      "id": "standard__email",
      "operation": "IS_EMPTY"
    }
  ],
  "returnFields": [
    "system__contact_id",
    "standard__first_name",
    "standard__phone_number"
  ]
}
```

### Pagination

The list endpoint supports cursor-based pagination:

```json
{
  "page": {
    "cursor": "",
    "limit": 20
  }
}
```

**Initial Request:** Leave `cursor` empty or omit it

**Subsequent Requests:** Use the `next_cursor` from the previous response

### Response Format

```json
{
  "objects": [
    {
      "id": "CON-123456",
      "fields": [
        {
          "id": "standard__first_name",
          "value": "John"
        },
        {
          "id": "standard__last_name",
          "value": "Smith"
        },
        {
          "id": "standard__email",
          "value": "john.smith@example.com"
        }
      ]
    }
  ],
  "next_cursor": "eyJvZmZzZXQiOjIwfQ==",
  "total_objects": 145,
  "has_more": true
}
```

**Response Fields:**

- **`objects`**: Array of matching records
- **`next_cursor`**: Cursor for next page (use in subsequent request)
- **`total_objects`**: Total number of matching records
- **`has_more`**: Boolean indicating if more results exist

---

## Deleting Records

Delete existing CRM records by their ID.

### Delete Endpoint

**Endpoint:** `DELETE /{namespace}/{resourceTypeCode}/{id}`

### Example Request

```bash
curl -X DELETE "https://prod.apigateway.co/org/P-ABC123/contacts/CON-123456" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Response

```json
{}
```

**Success:** HTTP 200 with empty JSON object

### Important Notes

1. **Use External IDs:** The `{id}` parameter accepts the external_id you assigned to the record
2. **Permanent Deletion:** Deleted records cannot be recovered
3. **Cascade Behavior:** Check if deleting records affects associations
4. **Not Supported for Activities:** Some activity types may have deletion restrictions

---

## Associations

Associations link related CRM records together. The API supports various association types including contacts, companies, activities, and custom objects.

### Association Types

| Association Field | Used For | Links |
|------------------|----------|-------|
| `contact_associations` | Contacts | Contact → Company, Contact → Activity |
| `company_associations` | Companies | Company → Contact, Company → Company, Company → Activity |
| `activity_associations` | Activities | Activity → Contact, Activity → Company |
| `custom_object_associations` | Custom Objects | Custom Object → any type |

### Association Structure

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

- **`id`**: The external_id or system ID of the record to associate
- **`type`**: The type of record (`Contact`, `Company`, `Activity`, `CustomObject`)
- **`subtype`**: For activities, the specific type (Task, Meeting, Call, etc.)

### Company Primary Contact

The primary contact is a special association that identifies the main point of contact for a company.

#### Setting a Primary Contact

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

**Using the standard field:**

```json
{
  "fields": [
    {
      "id": "standard__company_name",
      "value": "Acme Corporation",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__contact_primary_company_id",
      "value": "COM-789012",
      "operation": "always_overwrite"
    }
  ]
}
```

#### Retrieving Primary Contact

When listing or fetching companies, include the association field:

```json
{
  "returnFields": [
    "standard__company_name",
    "standard__contact_primary_company_id"
  ]
}
```

### Company-to-Company Relationships

Create hierarchical or related company structures (parent companies, subsidiaries, partners, etc.).

#### Example: Parent-Subsidiary Relationship

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

- **Corporate Hierarchies:** Parent company → subsidiaries
- **Partner Networks:** Companies that work together
- **Vendor Relationships:** Companies and their suppliers
- **Referral Sources:** Track which company referred which

### Contact-to-Company Associations

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

### Activity Associations

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

### Custom Object Associations

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

### Association Best Practices

1. **Validate IDs:** Ensure associated records exist before creating associations
2. **Use External IDs:** Reference records by external_id for consistency
3. **Combine Operations:** Use `combine` merge strategy to add associations without removing existing ones
4. **Bi-directional Links:** Consider if you need associations in both directions
5. **Cleanup:** Remove associations when relationships end

---

## Custom Objects and Fields

The CRM API supports extensibility through custom objects and custom fields, allowing you to model domain-specific data.

### Understanding Custom Fields

Custom fields extend standard objects (Contacts, Companies, Activities) with additional properties specific to your business needs.

#### Custom Field Naming

Custom fields follow the naming convention: `custom__[field_name]`

Example:
```
custom__customer_tier
custom__account_manager_name
custom__renewal_date
```

#### Creating and Using Custom Fields

Custom fields are typically created through the Vendasta admin interface. Once created, they appear in the field schema:

```bash
curl -X GET "https://prod.apigateway.co/org/P-ABC123/contacts/meta/fields"
```

Response includes custom fields:

```json
{
  "properties": {
    "custom__customer_tier": {
      "type": "string",
      "title": "Customer Tier",
      "format": "dropdown"
    },
    "custom__lifetime_value": {
      "type": "integer",
      "title": "Lifetime Value"
    }
  }
}
```

#### Using Custom Fields in Upsert

```json
{
  "fields": [
    {
      "id": "standard__first_name",
      "value": "Jane",
      "operation": "always_overwrite"
    },
    {
      "id": "custom__customer_tier",
      "value": "Gold",
      "operation": "always_overwrite"
    },
    {
      "id": "custom__lifetime_value",
      "value": 50000,
      "operation": "always_overwrite"
    }
  ]
}
```

### Custom Objects

Custom objects are entirely user-defined object types with custom fields. They work alongside Contacts, Companies, and Activities.

#### Common Custom Object Use Cases

- **Projects:** Track project information tied to companies
- **Assets:** Equipment, software licenses, or inventory
- **Locations:** Physical locations for multi-location businesses
- **Opportunities:** Sales opportunities with custom stages
- **Contracts:** Service agreements or contracts
- **Tickets:** Support or service tickets

#### Custom Object Structure

Custom objects have standard system fields plus your custom fields:

**System Fields:**
- `system__customobject_id` - Unique identifier (read-only)
- `system__customobject_external_id` - Your external identifier
- `system__customobject_owner_id` - Owner of the record
- `system__customobject_created` - Creation timestamp (read-only)
- `system__customobject_updated` - Last update timestamp (read-only)
- `standard__customobject_name` - Display name for the object

#### Retrieving Custom Object Schema

```bash
curl -X GET "https://prod.apigateway.co/org/P-ABC123/customobjects/meta/fields" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

#### Creating a Custom Object Record

```bash
curl -X PATCH "https://prod.apigateway.co/org/P-ABC123/customobjects" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": [
      {
        "id": "standard__customobject_name",
        "value": "Website Redesign Project",
        "operation": "always_overwrite"
      },
      {
        "id": "system__customobject_external_id",
        "value": "PROJ-2024-001",
        "operation": "always_overwrite"
      },
      {
        "id": "custom__project_status",
        "value": "In Progress",
        "operation": "always_overwrite"
      },
      {
        "id": "custom__project_budget",
        "value": 75000,
        "operation": "always_overwrite"
      },
      {
        "id": "custom__start_date",
        "value": "2024-01-15",
        "operation": "always_overwrite"
      }
    ],
    "searchExisting": ["external_id"],
    "associations": [
      {
        "id": "COM-123456",
        "type": "Company"
      },
      {
        "id": "CON-789012",
        "type": "Contact"
      }
    ]
  }'
```

#### Listing Custom Objects

```bash
curl -X POST "https://prod.apigateway.co/org/P-ABC123/list/customobjects" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": [
      {
        "id": "custom__project_status",
        "value": "In Progress",
        "operation": "IS"
      }
    ],
    "returnFields": [
      "standard__customobject_name",
      "custom__project_status",
      "custom__project_budget"
    ]
  }'
```

### Custom Field Types

When defining custom fields (via admin interface), choose appropriate types:

| Type | Best For | Example |
|------|----------|---------|
| String | Text, names, descriptions | Customer ID, Notes |
| Integer | Counts, quantities | Number of employees |
| Date | Dates without time | Contract start date |
| DateTime | Dates with time | Last contact date |
| Dropdown | Fixed set of options | Status, Priority, Category |
| Currency | Monetary values | Deal value, Budget |
| Boolean | Yes/no, true/false | Is VIP, Active status |
| Email | Email addresses | Secondary email |
| Phone | Phone numbers | Mobile number |
| Tag | Multiple selections | Skills, Categories |
| String List | Multiple text values | Product interests |

### Best Practices for Custom Fields and Objects

1. **Plan Your Schema:** Design your custom fields before implementation
2. **Use Descriptive Names:** `custom__renewal_date` is better than `custom__date1`
3. **Choose Appropriate Types:** Use the most specific type (date vs string)
4. **Limit Custom Fields:** Too many fields impact performance and usability
5. **Document Custom Fields:** Maintain documentation of what each field represents
6. **Use Dropdowns:** Prefer dropdowns over free text for consistency
7. **Consider Search:** Include custom fields you'll query in `searchExisting`
8. **Namespace Awareness:** Custom fields may differ between partner and SMB namespaces

---

## Using External IDs

External IDs are crucial for integrating with external systems and maintaining data consistency across platforms.

### What is an External ID?

An external ID is a unique identifier from your source system that you assign to CRM records. It enables:

- **Bi-directional Sync:** Match records between systems
- **De-duplication:** Prevent duplicate imports
- **Update Tracking:** Know which CRM record corresponds to external records
- **Data Integrity:** Maintain relationships across systems

### External ID Fields

Each object type has its own external ID field:

| Object Type | External ID Field |
|-------------|------------------|
| Contact | `system__contact_external_id` |
| Company | `system__company_external_id` |
| Activity | `system__activity_external_id` |
| Custom Object | `system__customobject_external_id` |

### Setting External IDs

#### During Upsert

```json
{
  "fields": [
    {
      "id": "system__contact_external_id",
      "value": "SALESFORCE-003K000001qwertyIAA",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__first_name",
      "value": "John",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__email",
      "value": "john@example.com",
      "operation": "always_overwrite"
    }
  ],
  "searchExisting": ["external_id"]
}
```

**Note:** Use the shorthand `"external_id"` in `searchExisting` instead of the full field name.

### External ID Best Practices

#### 1. Use Consistent Format

Choose a format and stick with it across all records:

```
SYSTEM-TYPE-ID
```

Examples:
- `SALESFORCE-CON-003K000001qwertyIAA`
- `HUBSPOT-COMPANY-12345`
- `QUICKBOOKS-CUSTOMER-67890`

#### 2. Include Source System Identifier

Prefix with the source system to prevent collisions:

```json
{
  "id": "system__contact_external_id",
  "value": "HUBSPOT-12345"
}
```

#### 3. Always Search by External ID

When syncing from external systems:

```json
{
  "fields": [
    {
      "id": "system__contact_external_id",
      "value": "HUBSPOT-12345",
      "operation": "always_overwrite"
    },
    // ... other fields
  ],
  "searchExisting": ["external_id"]
}
```

#### 4. Store Bidirectional References

In your external system, store the Vendasta CRM ID:

```javascript
// Your system's database
{
  salesforceId: "003K000001qwertyIAA",
  vendastaContactId: "CON-123456",
  syncStatus: "synced",
  lastSyncDate: "2024-01-15T10:30:00Z"
}
```

#### 5. Handle External ID Collisions

If you're syncing multiple source systems, namespace your external IDs:

```json
{
  "id": "system__contact_external_id",
  "value": "SALESFORCE:003K000001qwertyIAA",
  "operation": "always_overwrite"
}

{
  "id": "system__contact_external_id",
  "value": "HUBSPOT:12345",
  "operation": "always_overwrite"
}
```

### Common Integration Patterns

#### Pattern 1: Initial Import

```javascript
// Pseudo-code for importing from external system
async function importContact(externalContact) {
  const response = await fetch(
    'https://prod.apigateway.co/org/P-ABC123/contacts',
    {
      method: 'PATCH',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        fields: [
          {
            id: 'system__contact_external_id',
            value: `EXTERNAL-${externalContact.id}`,
            operation: 'always_overwrite'
          },
          {
            id: 'standard__first_name',
            value: externalContact.firstName,
            operation: 'always_overwrite'
          },
          {
            id: 'standard__email',
            value: externalContact.email,
            operation: 'always_overwrite'
          }
        ],
        searchExisting: ['external_id'],
        returnFields: ['system__contact_id']
      })
    }
  );

  const result = await response.json();
  
  // Store the Vendasta ID in your system
  await saveMapping(externalContact.id, result.fields.find(f => f.id === 'system__contact_id').value);
}
```

#### Pattern 2: Ongoing Sync

```javascript
async function syncContact(externalContact) {
  // Check last sync timestamp
  const lastSync = await getLastSyncTime(externalContact.id);
  
  if (externalContact.updatedAt > lastSync) {
    await fetch('https://prod.apigateway.co/org/P-ABC123/contacts', {
      method: 'PATCH',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        fields: [
          {
            id: 'system__contact_external_id',
            value: `EXTERNAL-${externalContact.id}`,
            operation: 'always_overwrite'
          },
          // ... updated fields
        ],
        searchExisting: ['external_id']
      })
    });
    
    await updateLastSyncTime(externalContact.id, new Date());
  }
}
```

#### Pattern 3: Querying by External ID

```javascript
async function findByExternalId(externalId) {
  const response = await fetch(
    'https://prod.apigateway.co/org/P-ABC123/list/contacts',
    {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        fields: [
          {
            id: 'system__contact_external_id',
            value: externalId,
            operation: 'IS'
          }
        ],
        returnFields: [
          'system__contact_id',
          'standard__first_name',
          'standard__email'
        ]
      })
    }
  );

  const result = await response.json();
  return result.objects[0]; // Should be unique
}
```

### External ID Gotchas

1. **Not Enforced as Unique:** The API doesn't enforce uniqueness - you must manage this
2. **Case Sensitive:** External IDs are case-sensitive
3. **String Field:** Always provide as string, even if your source uses integers
4. **Length Limits:** Check field schema for max length
5. **Empty Values:** Don't set external_id to empty string - omit it instead

---

## Record Source Tracking

The CRM API automatically tracks the source of record creation and updates, providing audit trails and data lineage.

### Source Fields

Each object type has source tracking fields:

#### Original Source

Tracks where the record was first created:

| Object | Field |
|--------|-------|
| Contact | `standard__contact_original_source` |
| Company | `standard__company_original_source` |
| Custom Object | `standard__customobject_original_source` |

**Original Source Drill-down:**
- `standard__contact_original_source_drill1`
- `standard__company_original_source_drill1`
- `standard__customobject_original_source_drill1`

#### Record Source

Tracks the most recent source that modified the record:

| Object | Field |
|--------|-------|
| Contact | `standard__contact_source_name` |
| Company | `standard__company_source_name` |
| Custom Object | `standard__customobject_record_source` |

**Record Source Drill-down:**
- `standard__contact_record_source_drill1`
- `standard__company_record_source_drill1`
- `standard__customobject_record_source_drill1`

### Default Values

When you use the External API, the system automatically sets:

**Original Source Fields (if empty):**
- **Source:** `"External API"`
- **Drill-down 1:** Your user ID (authenticated user making the API call)

**Record Source Fields (on every update):**
- **Source:** `"External API"`
- **Drill-down 1:** Your user ID

### Providing Custom Source Information

You can override the default source tracking by explicitly setting these fields:

#### Example: Track Import Source

```json
{
  "fields": [
    {
      "id": "standard__first_name",
      "value": "John",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__contact_original_source",
      "value": "CSV Import - Q4 2024",
      "operation": "overwrite_if_empty"
    },
    {
      "id": "standard__contact_original_source_drill1",
      "value": "marketing-team@example.com",
      "operation": "overwrite_if_empty"
    },
    {
      "id": "standard__contact_source_name",
      "value": "Salesforce Sync",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__contact_record_source_drill1",
      "value": "sync-process-v2.1",
      "operation": "always_overwrite"
    }
  ]
}
```

### Merge Strategies for Source Fields

Use appropriate merge strategies to control source tracking behavior:

**For Original Source Fields:**
```json
{
  "id": "standard__contact_original_source",
  "value": "Partner Referral",
  "operation": "overwrite_if_empty"  // Only set if not already set
}
```

**For Record Source Fields:**
```json
{
  "id": "standard__contact_source_name",
  "value": "Integration Sync",
  "operation": "always_overwrite"  // Update on every sync
}
```

### Source Tracking Use Cases

#### Use Case 1: Attribution Tracking

Track where leads originate:

```json
{
  "fields": [
    {
      "id": "standard__first_name",
      "value": "Jane",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__contact_original_source",
      "value": "Trade Show 2024",
      "operation": "overwrite_if_empty"
    },
    {
      "id": "standard__contact_original_source_drill1",
      "value": "Booth 42 - John Smith",
      "operation": "overwrite_if_empty"
    }
  ]
}
```

#### Use Case 2: Integration Audit Trail

Track which system last updated the record:

```json
{
  "fields": [
    {
      "id": "standard__company_name",
      "value": "Acme Corp",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__company_source_name",
      "value": "Quickbooks Integration",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__company_record_source_drill1",
      "value": "Nightly Sync - 2024-01-15",
      "operation": "always_overwrite"
    }
  ]
}
```

#### Use Case 3: Campaign Tracking

Track marketing campaigns:

```json
{
  "fields": [
    {
      "id": "standard__email",
      "value": "prospect@example.com",
      "operation": "always_overwrite"
    },
    {
      "id": "standard__contact_original_source",
      "value": "Google Ads",
      "operation": "overwrite_if_empty"
    },
    {
      "id": "standard__contact_original_source_drill1",
      "value": "Campaign: Summer Sale 2024",
      "operation": "overwrite_if_empty"
    }
  ]
}
```

### Retrieving Source Information

Include source fields in your queries:

```json
{
  "returnFields": [
    "standard__first_name",
    "standard__email",
    "standard__contact_original_source",
    "standard__contact_original_source_drill1",
    "standard__contact_source_name",
    "standard__contact_record_source_drill1",
    "system__contact_created",
    "system__contact_updated"
  ]
}
```

### Source Tracking Best Practices

1. **Use Descriptive Names:** Make source values meaningful
   - Good: `"Salesforce Sync - Daily Job"`
   - Bad: `"Sync1"`

2. **Leverage Drill-down Fields:** Add context in drill-down fields
   - Source: `"Partner Portal"`
   - Drill-down 1: `"partner@example.com - Bulk Import"`

3. **Preserve Original Source:** Use `overwrite_if_empty` for original source fields

4. **Update Record Source:** Use `always_overwrite` for record source fields to track latest changes

5. **Document Your Sources:** Maintain a list of source values you use

6. **Include Versions:** Add version information for integrations
   - `"MyApp Integration v2.3.1"`

7. **Timestamps in Drill-down:** Consider adding timestamps
   - `"2024-01-15 10:30 UTC - Scheduled Sync"`

---

## Best Practices

### General API Usage

1. **Use HTTPS:** All API calls must use HTTPS
2. **Handle Rate Limits:** Implement exponential backoff for rate limit errors
3. **Validate Data:** Validate data before sending to reduce errors
4. **Use Pagination:** Don't try to fetch all records at once
5. **Cache Field Schemas:** Field schemas change infrequently; cache them

### Data Management

1. **Set External IDs:** Always set external_id when integrating with other systems
2. **Use Appropriate Merge Strategies:** Choose the right operation for your use case
3. **Validate Associations:** Ensure associated records exist before creating links
4. **Clean Your Data:** Remove invalid characters, normalize formats
5. **Handle Duplicates:** Use searchExisting to prevent duplicates

### Performance

1. **Batch Operations:** Group multiple record operations when possible
2. **Limit Return Fields:** Only request fields you need
3. **Use Specific Filters:** Make list queries as specific as possible
4. **Monitor API Usage:** Track your API usage and optimize
5. **Async Processing:** Use async patterns for large data imports

### Security

1. **Protect API Keys:** Never expose authentication tokens
2. **Use Environment Variables:** Store credentials securely
3. **Implement Logging:** Log API interactions for audit trails
4. **Validate Inputs:** Sanitize user inputs before sending to API
5. **Follow Least Privilege:** Use appropriate permission scopes

### Error Handling

1. **Handle All Error Codes:** Implement comprehensive error handling
2. **Log Errors:** Capture error details for debugging
3. **Retry Transient Errors:** Retry on network or temporary failures
4. **User-Friendly Messages:** Translate API errors to user-friendly messages
5. **Monitor Failures:** Track failure rates and investigate anomalies

### Testing

1. **Use Demo Environment:** Test thoroughly in demo before production
2. **Test Edge Cases:** Test with empty values, special characters, large data
3. **Verify De-duplication:** Ensure your matching logic works correctly
4. **Test Pagination:** Verify your code handles multiple pages
5. **Test Error Scenarios:** Simulate errors to verify handling

---

## Error Handling

### HTTP Status Codes

| Code | Meaning | Action |
|------|---------|--------|
| 200 | Success | Process response |
| 400 | Bad Request | Check request format and parameters |
| 401 | Unauthorized | Verify authentication token |
| 403 | Forbidden | Check permissions for namespace |
| 404 | Not Found | Verify resource exists |
| 429 | Rate Limited | Implement backoff and retry |
| 500 | Server Error | Retry with exponential backoff |
| 503 | Service Unavailable | Retry later |

### Error Response Format

```json
{
  "error": "Detailed error message"
}
```

### Common Errors

#### Invalid Namespace

```json
{
  "error": "Namespace is missing or empty"
}
```

**Solution:** Ensure namespace parameter is provided and valid

#### Invalid Resource Type

```json
{
  "error": "Invalid resource type. It can be either one of ['contacts', 'companies', 'activities', 'customobjects']"
}
```

**Solution:** Use correct resourceTypeCode from allowed values

#### Field Validation Errors

```json
{
  "error": "Field validation failed: invalid email format"
}
```

**Solution:** Validate field values match expected format

#### Authentication Errors

```json
{
  "error": "Unauthorized"
}
```

**Solution:** Verify your authentication token is valid and not expired

### Implementing Retry Logic

```javascript
async function apiCallWithRetry(url, options, maxRetries = 3) {
  let lastError;
  
  for (let i = 0; i < maxRetries; i++) {
    try {
      const response = await fetch(url, options);
      
      if (response.ok) {
        return await response.json();
      }
      
      // Don't retry client errors (except rate limiting)
      if (response.status >= 400 && response.status < 500 && response.status !== 429) {
        throw new Error(`Client error: ${response.status}`);
      }
      
      // Retry server errors and rate limiting
      if (response.status === 429 || response.status >= 500) {
        const delay = Math.pow(2, i) * 1000; // Exponential backoff
        await new Promise(resolve => setTimeout(resolve, delay));
        continue;
      }
      
    } catch (error) {
      lastError = error;
      if (i === maxRetries - 1) {
        throw lastError;
      }
      
      // Exponential backoff
      const delay = Math.pow(2, i) * 1000;
      await new Promise(resolve => setTimeout(resolve, delay));
    }
  }
  
  throw lastError;
}
```

---

## Code Examples

### Python

#### Upsert a Contact

```python
import requests
import json

def upsert_contact(namespace, token, email, first_name, last_name):
    url = f"https://prod.apigateway.co/org/{namespace}/contacts"
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    
    payload = {
        "fields": [
            {
                "id": "standard__email",
                "value": email,
                "operation": "always_overwrite"
            },
            {
                "id": "standard__first_name",
                "value": first_name,
                "operation": "overwrite_if_empty"
            },
            {
                "id": "standard__last_name",
                "value": last_name,
                "operation": "overwrite_if_empty"
            }
        ],
        "searchExisting": ["standard__email"],
        "returnFields": ["system__contact_id", "standard__email"]
    }
    
    response = requests.patch(url, headers=headers, json=payload)
    response.raise_for_status()
    
    return response.json()

# Usage
result = upsert_contact(
    namespace="P-ABC123",
    token="your_token_here",
    email="john@example.com",
    first_name="John",
    last_name="Doe"
)
print(f"Contact ID: {result['fields'][0]['value']}")
```

#### List Companies

```python
def list_companies(namespace, token, page_cursor="", limit=20):
    url = f"https://prod.apigateway.co/org/list/{namespace}/companies"
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    
    payload = {
        "fields": [],
        "returnFields": [
            "system__company_id",
            "standard__company_name",
            "standard__company_website"
        ],
        "page": {
            "cursor": page_cursor,
            "limit": limit
        }
    }
    
    response = requests.post(url, headers=headers, json=payload)
    response.raise_for_status()
    
    return response.json()

# Usage
companies = list_companies(
    namespace="P-ABC123",
    token="your_token_here",
    limit=50
)

for company in companies['objects']:
    name = next((f['value'] for f in company['fields'] if f['id'] == 'standard__company_name'), None)
    print(f"Company: {name}")
```

### JavaScript/Node.js

#### Upsert with External ID

```javascript
const fetch = require('node-fetch');

async function upsertContactWithExternalId(namespace, token, externalId, contactData) {
  const url = `https://prod.apigateway.co/org/${namespace}/contacts`;
  
  const payload = {
    fields: [
      {
        id: 'system__contact_external_id',
        value: externalId,
        operation: 'always_overwrite'
      },
      {
        id: 'standard__first_name',
        value: contactData.firstName,
        operation: 'always_overwrite'
      },
      {
        id: 'standard__last_name',
        value: contactData.lastName,
        operation: 'always_overwrite'
      },
      {
        id: 'standard__email',
        value: contactData.email,
        operation: 'always_overwrite'
      }
    ],
    searchExisting: ['external_id'],
    returnFields: ['system__contact_id', 'system__contact_external_id']
  };

  const response = await fetch(url, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    throw new Error(`API error: ${response.status}`);
  }

  return await response.json();
}

// Usage
upsertContactWithExternalId(
  'P-ABC123',
  'your_token_here',
  'SALESFORCE-12345',
  {
    firstName: 'Jane',
    lastName: 'Smith',
    email: 'jane.smith@example.com'
  }
).then(result => {
  console.log('Contact created/updated:', result);
}).catch(error => {
  console.error('Error:', error);
});
```

#### Create Activity with Associations

```javascript
async function createTask(namespace, token, taskName, dueDate, contactId, companyId) {
  const url = `https://prod.apigateway.co/org/${namespace}/activities`;
  
  const payload = {
    subtype: 'Task',
    fields: [
      {
        id: 'standard__activity_name',
        value: taskName,
        operation: 'always_overwrite'
      },
      {
        id: 'standard__task_due_date',
        value: dueDate, // ISO 8601 format
        operation: 'always_overwrite'
      },
      {
        id: 'standard__task_status',
        value: 'Not Started',
        operation: 'always_overwrite'
      }
    ],
    associations: [
      {
        id: contactId,
        type: 'Contact'
      },
      {
        id: companyId,
        type: 'Company'
      }
    ],
    returnFields: ['system__activity_id', 'standard__activity_name']
  };

  const response = await fetch(url, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    throw new Error(`API error: ${response.status}`);
  }

  return await response.json();
}

// Usage
createTask(
  'P-ABC123',
  'your_token_here',
  'Follow up on proposal',
  '2024-12-31T17:00:00Z',
  'CON-123456',
  'COM-789012'
);
```

### cURL Examples

#### Get Field Schema

```bash
curl -X GET \
  "https://prod.apigateway.co/org/P-ABC123/contacts/meta/fields" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

#### Upsert Company with Association

```bash
curl -X PATCH \
  "https://prod.apigateway.co/org/P-ABC123/companies" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": [
      {
        "id": "standard__company_name",
        "value": "Acme Corporation",
        "operation": "always_overwrite"
      },
      {
        "id": "standard__company_website",
        "value": "https://acme.com",
        "operation": "overwrite_if_empty"
      }
    ],
    "searchExisting": ["standard__company_name"],
    "associations": [
      {
        "id": "CON-123456",
        "type": "Contact"
      }
    ],
    "returnFields": [
      "system__company_id",
      "standard__company_name"
    ]
  }'
```

#### List with Filters

```bash
curl -X POST \
  "https://prod.apigateway.co/org/list/P-ABC123/contacts" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": [
      {
        "id": "standard__tags",
        "value": ["customer", "vip"],
        "operation": "IS_ANY"
      }
    ],
    "returnFields": [
      "standard__first_name",
      "standard__last_name",
      "standard__email",
      "standard__tags"
    ],
    "page": {
      "cursor": "",
      "limit": 50
    }
  }'
```

#### Delete Record

```bash
curl -X DELETE \
  "https://prod.apigateway.co/org/P-ABC123/contacts/CON-123456" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## Appendix

### Field Prefixes Quick Reference

| Prefix | Purpose | Writable | Example |
|--------|---------|----------|---------|
| `standard__` | Standard cross-system fields | Yes | `standard__first_name` |
| `system__` | System-managed fields | Mostly No | `system__contact_id` |
| `custom__` | User-defined custom fields | Yes | `custom__customer_tier` |
| `platform__` | Platform-specific fields | Some | `platform__user_id` |

### Resource Type Codes

| Code | Object Type | Subtypes |
|------|------------|----------|
| `contacts` | Contacts | N/A |
| `companies` | Companies | N/A |
| `activities` | Activities | Task, Meeting, Call, Note, Email, Communication |
| `customobjects` | Custom Objects | User-defined |
| `accounts` | Accounts | N/A (special handling) |

### Activity Subtypes

- `Task` - To-do items
- `Meeting` - Scheduled meetings
- `Call` - Phone calls
- `Note` - Notes and comments
- `Email` - Email communications
- `Communication` - Other communications

### Common Field Patterns

#### Contact Fields

- `standard__first_name`, `standard__last_name` - Name
- `standard__email` - Email address
- `standard__phone_number` - Primary phone
- `standard__contact_mobile_phone_number` - Mobile phone
- `standard__contact_job_title` - Job title
- `standard__contact_company_name` - Company name
- `system__contact_id` - Unique ID
- `system__contact_external_id` - Your external ID
- `system__contact_owner_id` - Record owner
- `system__contact_created` - Creation date
- `system__contact_updated` - Last update date

#### Company Fields

- `standard__company_name` - Company name
- `standard__company_website` - Website URL
- `standard__company_industry` - Industry
- `standard__company_employee_count` - Number of employees
- `standard__company_annual_revenue` - Annual revenue
- `system__company_id` - Unique ID
- `system__company_external_id` - Your external ID
- `system__company_owner_id` - Record owner

#### Activity Fields

- `standard__activity_name` - Activity title
- `standard__task_due_date` - Due date (for tasks)
- `standard__task_status` - Status (for tasks)
- `standard__meeting_start_time` - Start time (for meetings)
- `standard__meeting_end_time` - End time (for meetings)
- `system__activity_id` - Unique ID

### Glossary

- **Namespace:** Identifier for data isolation (Partner ID or Account Group ID)
- **External ID:** Your system's identifier for a record
- **Upsert:** Create if new, update if exists
- **Merge Strategy:** How to handle updates (overwrite, combine, etc.)
- **Association:** Link between two CRM records
- **Resource Type:** Type of CRM object (contact, company, etc.)
- **Field Schema:** Definition of available fields for a resource type
- **Custom Object:** User-defined object type
- **Custom Field:** User-defined field on standard or custom objects
- **De-duplication:** Process of preventing duplicate records
- **Primary Contact:** Main contact associated with a company
- **Source Tracking:** Audit trail of where record was created/updated

---

## Support and Resources

### Documentation
- [API Versioning](https://developers.vendasta.com/platform/ZG9jOjEwMTU2NTYy-versioning)
- [OpenAPI Specification](https://spec.openapis.org/oas/v3.1.0)

### Getting Help
- Check error messages carefully - they usually indicate the issue
- Test in demo environment before production
- Review field schema to ensure you're using correct field IDs
- Verify namespace and resource types are correct

### Best Practices Summary
1. Always use external_id for integrations
2. Implement proper error handling and retries
3. Use searchExisting to prevent duplicates
4. Choose appropriate merge strategies
5. Cache field schemas
6. Test thoroughly in demo environment
7. Monitor API usage and performance
8. Document your integration

---

**Document Version:** 1.0  
**Last Updated:** November 2024  
**API Status:** Trusted Tester

