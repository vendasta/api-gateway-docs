# Create a Business User

After creating a business location you most likely want to add some users to it who can access Business App and any of the activated products.


## Create a new user
When creating a new user you will need their email address and the IDs of the business locations that you would like to give them access to. For full details on the available fields see the [API specification](../../openapi/platform/platform.yaml/components/schemas/users).


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
        }
      }
    }
  }
}
```


Creating a user by API does **not** send out a welcome email. You may build your own message or send ours in a later step of your process.


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

## Link an existing user
Adding additional business locations

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
