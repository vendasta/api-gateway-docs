---
tags: [businesses, businessLocations, contacts]
---
# Businesses

A `Business Location` is the basic record for storing information about an organization that is generally part of their 
public profile. More sensitive data can be found in related resources. 
The location may be a retail store, the business' headquarters or identify an area they serve.

## Common Actions

> For info on creating an access token see the [Authorization guide](https://vendasta.stoplight.io/docs/openapi-specs/docs/2.-Authorization/1.-Authorization.md)

### Creating a Business Location

To create a new Business Location make the following call

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/businessLocations",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Access Token with `business` scope>",
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
            "value": "1 (555) 323-1234"
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
            "value": "1 (555) 323-1234"
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


### Creating a Sales Contact 

When creating a Contact for a Business you may also link that Contact to one or more Locations in the same API call.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/salesContacts",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token with `sales.contact` scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "salesContacts",
      "attributes": {
        "givenName": "Samantha",
        "familyName": "Green",
        "phone": "+1 306-555-1234 ext. 89",
        "phoneCountryCode": "CA",
        "email": "user@example.com"
      },
      "relationships": {
        "locations": {
          "data": [
            {
              "id": "AG-1234",
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
  "data": {
    "type": "salesContacts",
    "id": "CO-0739c46cc4794157bf962bd73ce897a7",
    "attributes": {
      "givenName": "Samantha",
      "familyName": "Green",
      "phone": "+1 306-555-1234 ext. 89",
      "phoneCountryCode": "CA",
      "email": "user@example.com"
    },
    "relationships": {
      "locations": {
        "links": {
          "related": "https://prod.apigateway.co/platform/salesContacts/CO-0739c46cc4794157bf962bd73ce897a7/locations",
          "self": "https://prod.apigateway.co/platform/salesContacts/CO-0739c46cc4794157bf962bd73ce897a7/relationships/locations"
        },
        "data": [
          {
            "type": "businessLocations",
            "id": "AG-123"
          }
        ]
      }
    }
  }
}
```

### Update attribute on a business location
When updating the values for a business location you only need to send the fields that have changed. In this example we are updating the `customerIdentifier` on the business location with ID `AG-3VDRVLBNJG`. Note that the ID gets set in both the path and body. 

```json http
{
  "method": "patch",
  "url": "https://prod.apigateway.co/platform/businessLocations/AG-3VDRVLBNJG",
  "query": {
    "fields[businessLocations]": "customerIdentifier"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with `business` scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "id": "AG-3VDRVLBNJG",
      "type": "businessLocations",
      "attributes": {
        "customerIdentifier": "CUST-5"
      }
    }
  }
}
```
For more details on this endpoint see [Update Business Locations](https://vendasta.stoplight.io/docs/openapi-specs/openapi/platform/platform.yaml/paths/~1businessLocations~1%7Bid%7D/patch)


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

