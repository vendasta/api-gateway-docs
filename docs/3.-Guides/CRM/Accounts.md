---
tags: [businesses, businessLocations, contacts]
---
# Businesses

A `Business Location` is the basic record for storing information about an organization that is generally part of their 
public profile. More sensitive data can be found in related resources. 
The location may be a retail store, the business' headquarters or identify an area they serve.

## Common Actions

### Creating a Business Location

To create a new Business Location make the following call

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/businessLocations",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Access Token>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "businessLocations",
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
        "businessPartner": {
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
      "type": "businessLocations",
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
        "businessPartner": {
          "data": {
            "type": "organizations",
            "id": "ABC"
          }
        }
      }
    },
    "links": {
      "self": "https://prod.apigateway.co/platform/businessLocations/AG-123"
    }
  }
```


### Creating a Business Contact

When creating a Contact for a Business you may also link that Contact to one or more Locations in the same API call.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/contacts",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "contacts",
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
        "businessPartner": {
          "data": {
            "type": "organizations",
            "id": "ABC"
          }
        },
        "locations": {
          "data": [
            {
              "id": "AG-1234",
              "type": "businessLocations"
            },
            {
              "id": "AG-4567",
              "type": "businessLocations"
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
      "type": "contacts",
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
        "businessPartner": {
          "data": {
            "type": "organizations",
            "id": "ABC"
          }
        },
        "locations": {
          "links": {
            "self": "https://prod.apigateway.co/platform/contacts/U-1234/relationships/locations"
          }
        }
      },
      "links":{
        "self":"https://prod.apigateway.co/platform/contacts/U-1234"
      }
    }
  }
}
```

## Proposed future actions

### Get a list of your business locations

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/businessLocations",
  "query": {
    "filter[parent]": "me",
    "fields[businessLocations]": "name",
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
      "type": "businessLocations",
      "id": "AG-1234",
      "attributes": {
        "name": "Bill's Bakery"
      }
    },
    {
      "type": "businessLocations",
      "id": "AG-5642",
      "attributes": {
        "name": "Shane's Snack Hut"
      }
    }
  ],
  "links": {
    "first": "https://prod.apigateway.co/platform/businessLocations?page[cursor]=abc",
    "self": "https://prod.apigateway.co/platform/businessLocations?page[cursor]=klm",
    "next": "https://prod.apigateway.co/platform/businessLocations?page[cursor]=nop",
    "last": "https://prod.apigateway.co/platform/businessLocations?page[cursor]=xyz"
  },
  "meta": {
    "total": 234
  }
}
```

### Find a customer using the ID from your external system

Comming soon
