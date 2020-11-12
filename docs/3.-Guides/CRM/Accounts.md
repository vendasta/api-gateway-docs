---
tags: [accounts, accountLocations, accountContacts]
---
# Accounts

An `Account` is the basic record for storing information about an orginization that you have a sustained relationship with. This may include a potential buyer, an existing client, or a past client that has churned.

## Common Actions

### Creating an Account Location

To create a new Account Location make the following call

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/accountLocations",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Access Token>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "accountLocations",
      "attributes": {
        "name": "Fred's Fish on Young",
        "address": {
          "line1": "123 Young St",
          "line2": "",
          "city": "Toronto",
          "stateCode": "ON",
          "zip": "O2C 4W9",
          "countryCode": "CA"
        },
        "phone": [
          {
            "countryCode": "CA",
            "raw": "1 (555) 323-1234"
          }
        ],
        "serviceAreaBusiness": false
      },
      "relationships": {
        "owner": {
          "data": {
            "type": "organizations",
            "id": "ABC"
          }
        }
      }
    }
  }
}
```

It will return the newly created record including any server populated values. Be sure to save the ID for later.

```json
{
    "data": {
      "type": "accountLocations",
      "id": "AG-123",
      "attributes": {
        "name": "Fred's Fish on Young",
        "address": {
          "line1": "123 Young St",
          "line2": "",
          "city": "Toronto",
          "stateCode": "ON",
          "zip": "O2C 4W9",
          "countryCode": "CA"
        },
        "phone": [
          {
            "countryCode": "CA",
            "raw": "1 (555) 323-1234"
          }
        ],
        "serviceAreaBusiness": false
      },
      "relationships": {
        "owner": {
          "data": {
            "type": "organizations",
            "id": "ABC"
          }
        }
      }
    },
    "links": {
      "self": "https://prod.apigateway.co/platform/accountLocations/AG-123"
    }
  }
```


### Creating an Account Contact

When creating a Contact for an Account you may also link that Contact to one or more Locations in the same API call.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/accountContacts",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "accountContacts",
      "attributes": {
        "name": {
          "first": "Samantha",
          "last": "Green",
          "greeting": "Sam"
        },
        "phone": {
          "countryCode": "CA",
          "raw": "15553061234"
        },
        "email": "user@example.com",
        "languageCode": "en-US"
      },
      "relationships": {
        "owner": {
          "data": {
            "type": "organizations",
            "id": "ABC"
          }
        },
        "locations": {
          "data": [
            {
              "id": "AG-1234",
              "type": "accountLocations"
            },
            {
              "id": "AG-4567",
              "type": "accountLocations"
            }
          ]
        }
      }
    }
  }
}
```

It will return the newly created record including any server populated values. Be sure to save the ID for later.

```json
{
  {
    "data": {
      "type": "accountContacts",
      "id": "U-1234",
      "attributes": {
        "name": {
          "first": "Samantha",
          "last": "Green",
          "greeting": "Sam"
        },
        "phone": {
          "countryCode": "CA",
          "raw": "15553061234"
        },
        "email": "user@example.com",
        "languageCode": "en-US"
      },
      "relationships": {
        "owner": {
          "data": {
            "type": "organizations",
            "id": "ABC"
          }
        },
        "locations": {
          "links": {
            "self": "https://prod.apigateway.co/platform/accountContacts/U-1234/relationships/locations"
          }
        }
      },
      "links":{
        "self":"https://prod.apigateway.co/platform/accountContacts/U-1234"
      }
    }
  }
}
```

## Proposed future actions

### Get a list of your account locations

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/accountLocations",
  "query": {
    "filter[parent]": "me",
    "fields[accountLocations]": "name",
    "page[limit]": 2
  },
  "headers": {
    "Authorization": "Bearer {your token}",
    "Content-Type": "application/vnd.api+json",
    "Accept-Encoding": "application/vnd.api+json"
  }
}
```

Will return something like 

```json
{
  "data": [
    {
      "type": "accountLocations",
      "id": "AG-1234",
      "attributes": {
        "name": "Bill's Bakery"
      }
    },
    {
      "type": "accountLocations",
      "id": "AG-5642",
      "attributes": {
        "name": "Shane's Snack Hut"
      }
    }
  ],
  "links": {
    "first": "https://prod.apigateway.co/platform/accountLocations?page[cursor]=abc",
    "self": "https://prod.apigateway.co/platform/accountLocations?page[cursor]=klm",
    "next": "https://prod.apigateway.co/platform/accountLocations?page[cursor]=nop",
    "last": "https://prod.apigateway.co/platform/accountLocations?page[cursor]=xyz"
  },
  "meta": {
    "total": 234
  }
}
```

### Find a customer using the ID from your external system

Comming soon
