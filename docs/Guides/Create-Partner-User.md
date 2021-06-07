# Create a Partner User

When you hire a new employee they are going to need access to some of the platform features so they can start selling or fufilling orders.


## Create a new user
When creating a new user you will need their email address and the IDs of the platform features that you would like to give them access to. 

The most common platform features are:

|ID|Description|
|pc:access|Allows the user to access the admin tools in partner center|
|ssc:access|Provides access to Sales & Success Center|
|ssc:manage|Allows access to the management tools in Sales & Success Center|
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
        "platformAccess": {
          "data": [
            {
              "type": "appFeatures",
              "id": "ssc:access"
            },
            {
              "type": "appFeatures",
              "id": "ssc:manage"
            }
          ]
        }
      }
    }
  }
}
```

For full details on the available fields see the [API specification](TODO).

Creating a user by API does **not** send out a welcome email. You may build your own message or send ours in a later step of your process.


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
