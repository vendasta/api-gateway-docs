# Create a Business User

> This guide assumes you have already created an [Account](Accounts.md) either in Partner Center or using the API, and have reviewed the [Users Guide](Users.md)
>
> For info on creating an access token see the [Authorization guide](../Authorization/Authorization.md)

After creating an Account you most likely want to add some users to it, allowing them to access Business App and/or any of the Account's active products that support SSO.


## Create a new user
When creating a new business user you will need their email address, and the IDs of the business locations that you would like to give them access to. For full details on the available fields see the [API specification](../../openapi/platform/platform.yaml/components/schemas/users). 

Creating a user by API does **not** send out a welcome email. You may build your own message or send out the default welcome email in a later step of your process.

**Adding Roles**

A role **must** be applied at time of user creation. A role can be applied by adding a supported object to the User's `relationships`. Provide either the `businessLocations` object to add the `smb` role to the user, or the `platformAccess` to add the `partner`(Partner Center Admin) role, or both. The `sales-person` and `digital-agent` roles must be added via the Partner Center dashboard at this time(Administration-->My Teams).

See the [Create a Partner User](Create-Partner-Users.md) Guide for details on creating users for your staff.

*Test Creating a User*


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
        "displayName": "Bill Smith",
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
          "line1": "190 11th Street E.",
          "line2": "Unit 342",
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
        "businessLocations": {
          "data": [
            {
              "type": "businessLocations",
              "id": "AG-1234567"
            },
            {
              "type": "businessLocations",
              "id": "AG-9876543"
            }
          ]
        },
      "platformAccess": {
        "data": [
          {
            "type": "appFeatures",
            "id": "pc:access"
          }
        ]
      }
      }
    }
  }
}
```


## Check for an existing user
If another user already exists within your platform with the same email address you will get an error when trying to create a new user. You can search for an existing user by email with the following request.

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/users",
  "query": {
    "filter[partner.id]": "",
    "filter[email]": "bill@example.com",
    "fields": "user[displayName]"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'user.admin' scope>"
  }
}
```


## Manage business location associations for a user with the SMB role

You may list or modify a user's accessible business locations by making requests to the `businessLocations` relationship of that user.

For example, for a user with ID `U-1234567`, the URI path to interact with their business locations is `/platform/users/U-1234567/relationships/businessLocations`.

### List a user's business locations

You can list a user's business locations by making a `GET` request to the `businessLocations` relationship of that user.

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/users/U-1234567/relationships/businessLocations",
  "headers": {
    "Authorization": "Bearer <Access Token with 'user.admin' scope>"
  }
}
```


### Add to a user's business locations

You can add business locations to a user by making a `POST` request to the `businessLocations` relationship of that user.

After this operation completes, the user will have access to any existing locations plus the locations in the request.

Any location(s) in the request to which the user already had access will be silently ignored.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/users/U-1234567/relationships/businessLocations",
  "headers": {
    "Authorization": "Bearer <Access Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": [
      {
        "id": "AG-1234567",
        "type": "businessLocations"
      }
    ]
  }
}
```

### Replace all existing locations

You can replace any existing business locations for a user by making a `PATCH` request to the `businessLocations` relationship of that user.

After this operation completes, the user will have access only to the specified locations.

```json http
{
  "method": "patch",
  "url": "https://prod.apigateway.co/platform/users/U-1234567/relationships/businessLocations",
  "headers": {
    "Authorization": "Bearer <Access Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": [
      {
        "id": "AG-1234567",
        "type": "businessLocations"
      }
    ]
  }
}
```

### Remove one or more business locations

You can remove existing business location(s) for a user by making a `DELETE` request to the `businessLocations` relationship of that user.

After this operation completes, the user will no longer have access to the specified locations.

Any location(s) in the request to which the user did not already have access will be silently ignored.

```json http
{
  "method": "delete",
  "url": "https://prod.apigateway.co/platform/users/U-1234567/relationships/businessLocations",
  "headers": {
    "Authorization": "Bearer <Access Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": [
      {
        "id": "AG-1234567",
        "type": "businessLocations"
      }
    ]
  }
}
```
