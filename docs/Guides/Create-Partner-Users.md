# Create a Partner User

When you hire a new employee they are going to need access to some of the platform features so they can start selling or fulfilling orders.


## Create a new user

When creating a new partner user you will need their email address and the IDs of the platform features that you would like to give them access to. 

The most common platform features are:

|ID|Description|
|--|-----------|
|pc:access|Allows the user to access the admin tools in partner center|
|pc:canManageSales|Access to manage salespeople, pipelines, and sales settings|
|tm:access|Provides access to Task Manager|
|tm:manage|Allows access to the management tools in Task Manager|


```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/users",
  "headers": {
    "Authorization": "Bearer <Access Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "users",
      "attributes": {
        "givenName": "William",
        "familyName": "Smith",
        "greetingName": "Billy",
        "email": "bill@example.com",
        "languageLocaleCode": "en-US",
        "phoneNumbers": [
          {
            "number": "+1 306 555 1234 ext. 5",
            "typeCode": "mobile"
          }
        ],
        "address": {
          "streetAddress": "190 11th Street E.",
          "additionalAddress": "Unit 342",
          "city": "Saskatoon",
          "postalCode": "H0H 0H0",
          "regionCode": "CA-SK",
          "countryCode": "CA"
        },
        "timeZone": "America/Regina"
      },
      "relationships": {
        "partner": {
          "data": {
            "type": "partners",
            "id": "ABC"
          }
        },
        "platformAccess": {
          "data": [
            {
              "type": "appFeatures",
              "id": "pc:access"
            },
            {
              "type": "appFeatures",
              "id": "tm:manage"
            }
          ]
        }
      }
    }
  }
}
```

For full details on the available fields see the [API specification](../../openapi/platform/platform.yaml/components/schemas/users).

Creating a user by API does **not** send out a welcome email. You may build your own message or send the default welcome email in a later step of your process.

> The number of partner users you can have before needing to pay for extra seats depends on your subscription, so be aware of your limits prior to adding new seats over API. 


## Set a user's CRM permissions

Alongside the platform feature IDs above, you can control how much of the CRM a user can see and edit by adding **CRM permission tags** to `platformAccess`. These mirror the CRM permission controls shown in the platform UI, so an agent can be limited to just the records they own.

There are two kinds of CRM tag per object (`contacts`, `companies`, `custom_objects`):

| Tag | Matches the UI control | Meaning |
|-----|------------------------|---------|
| `crm:<object>:write:<scope>` | "Can view and edit &lt;object&gt;" + its scope | Lets the user view and edit the object, limited to `<scope>` |
| `crm:<object>:manage` | "Can manage &lt;object&gt; fields" | Lets the user manage the object's fields (on/off, no scope) |

`<scope>` is one of:

| Scope | UI label | Meaning |
|-------|----------|---------|
| `own` | Records assigned to the user | Only records the user owns |
| `ownunowned` | Records assigned to the user and unassigned records | Owned records plus records with no owner |
| `all` | All records | Every record in the CRM |

For example, `crm:contacts:write:own` lets the user view and edit only the contacts they own, and `crm:companies:write:all` lets them view and edit every company.

> **One write scope per object.** A user can hold at most one `write` scope for a given object. Sending both `crm:contacts:write:own` and `crm:contacts:write:all` in the same request is rejected. `manage` is a plain on/off tag with no scope.
>
> **Tags are the full CRM state.** When a request includes `platformAccess`, the CRM tags it contains are the user's complete CRM permissions. Leaving a tag out **removes** that access. To remove all CRM access, send `platformAccess` with no `crm:*` tags.

CRM tags go in the same `platformAccess` list as the feature IDs:

```json
"platformAccess": {
  "data": [
    { "type": "appFeatures", "id": "pc:access" },
    { "type": "appFeatures", "id": "crm:contacts:write:own" },
    { "type": "appFeatures", "id": "crm:contacts:manage" },
    { "type": "appFeatures", "id": "crm:companies:write:all" }
  ]
}
```

A `GET` on the user returns the same `crm:*` tags in `platformAccess`, so you can read back a user's current CRM permissions.


## Check for an existing user
If another user already exists within your platform with the same email address you will get an error when trying to create a new user. You can search for an existing user by email with the following request.

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/users",
  "query": {
    "filter[partner.id]": "",
    "filter[email]": "bill@example.com"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'user.admin' scope>"
  }
}
```