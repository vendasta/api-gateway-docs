---
tags: [businesses, businessLocations, contacts]
---
# Businesses

A `Business Location` is the basic record for storing information about an organization that is generally part of their 
public profile. More sensitive data can be found in related resources. Collectively your sales team may refer to this as an "Account".
The location may be a retail store, the business' headquarters or identify an area they serve.

## Common Actions

> For info on creating an access token see the [Authorization guide](../Authorization/Authorization.md)

### Creating a Business Location

To create a new Business Location make the following call, replacing values as appropriate.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/businessLocations",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>",
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
        "phoneNumbers": [ 
           "1 (555) 323-1234",
           "1 800 Go Green"
        ],
        "serviceAreaBusiness": false
      },
      "relationships": {
        "businessPartner": {
          "data": {
            "type": "partners",
            "id": "ABC"
          }
        },
        "businessCategories": {
          "data": [
            {
              "type": "businessCategories",
              "id": "restaurants:buffets"
            },
            {
              "type": "businessCategories",
              "id": "restaurants:fishnchips"
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
    "type": "businessLocations",
    "id": "AG-123",
    "attributes": {
      "customerIdentifier": "",
      "name": "Fred's Fish on Young",
      "address": {
        "line1": "123 Young St",
        "line2": "",
        "city": "Toronto",
        "stateCode": "ON",
        "zip": "O2C 4W9",
        "countryCode": "CA"
      },
      "geoCoordinate": {
        "latitude": 0,
        "longitude": 0
      },
      "serviceAreaBusiness": false,
      "phoneNumbers": [
        "1 (555) 323-1234",
        "1 800 Go Green"
      ]
    },
    "relationships": {
      "businessCategories": {
        "links": {
          "related": "https://prod.apigateway.co/platform/businessLocations/AG-123/businessCategories",
          "self": "https://prod.apigateway.co/platform/businessLocations/AG-123/relationships/businessCategories"
        },
        "data": [
          {
            "type": "businessCategories",
            "id": "restaurants:buffets"
          },
          {
            "type": "businessCategories",
            "id": "restaurants:fishnchips"
          }
        ]
      },
      "businessPartner": {
        "links": {
          "related": "https://prod.apigateway.co/platform/businessLocations/AG-123/businessPartner",
          "self": "https://prod.apigateway.co/platform/businessLocations/AG-123/relationships/businessPartner"
        },
        "data": {
          "type": "partners",
          "id": "ABC"
        }
      }
    }
  }
}
```

For more details on this endpoint see [Create Business Locations](../../openapi/platform/platform.yaml/paths/~1businessLocations/post)

### List valid Business Categories
When creating a business location you will likely want to know what ids you can use for the businessCategories field. 

This example will return the first 3 in Canadian French. Change the `Accept-Language` header to pick a different supported language or omit it to default to English. 

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/businessCategories",
  "query": {
    "page[limit]": "3"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>",
    "Accept-Language": "fr-CA"
  }
}
```

Response
```json
{
  "links": {
    "last": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=eyJsYXN0UGFnZVN0YXJ0IjowLCJsYXN0UGFnZUVuZCI6MywiZm9yUGFnZVR5cGUiOiJsYXN0IiwibGltaXQiOjN9&page[limit]=3",
    "next": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=eyJsYXN0UGFnZVN0YXJ0IjowLCJsYXN0UGFnZUVuZCI6MywiZm9yUGFnZVR5cGUiOiJuZXh0IiwibGltaXQiOjN9&page[limit]=3"
  },
  "data": [
    {
      "type": "businessCategories",
      "id": "active",
      "attributes": {
        "name": "Vie active"
      }
    },
    {
      "type": "businessCategories",
      "id": "active:amateursportsteams",
      "attributes": {
        "name": "Vie active > Équipe sportive amateur "
      }
    },
    {
      "type": "businessCategories",
      "id": "active:amusementparks",
      "attributes": {
        "name": "Vie active > Parcs d’attractions"
      }
    }
  ]
}
```

There are over 700 categories to pick from currently. To view them all you have two choices: 
1) Set the `page[limit]` query parameter to a higher number. 
2) Make additional request(s) to the `links.next` URI to get the next batch. You will know you have reached the last page when there is no `links.next` URI in the response.

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/businessCategories",
  "query": {
    "page[limit]": "3",
    "page[cursor]": "eyJsYXN0UGFnZVN0YXJ0IjowLCJsYXN0UGFnZUVuZCI6MywiZm9yUGFnZVR5cGUiOiJuZXh0IiwibGltaXQiOjN9"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>",
    "Accept-Language": "fr-CA"
  }
}
```

Response
```json
{
  "links": {
    "first": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=eyJsYXN0UGFnZVN0YXJ0IjozLCJsYXN0UGFnZUVuZCI6NiwiZm9yUGFnZVR5cGUiOiJmaXJzdCIsImxpbWl0IjozfQ==&page[limit]=3",
    "last": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=eyJsYXN0UGFnZVN0YXJ0IjozLCJsYXN0UGFnZUVuZCI6NiwiZm9yUGFnZVR5cGUiOiJsYXN0IiwibGltaXQiOjN9&page[limit]=3",
    "next": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=eyJsYXN0UGFnZVN0YXJ0IjozLCJsYXN0UGFnZUVuZCI6NiwiZm9yUGFnZVR5cGUiOiJuZXh0IiwibGltaXQiOjN9&page[limit]=3",
    "prev": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=eyJsYXN0UGFnZVN0YXJ0IjozLCJsYXN0UGFnZUVuZCI6NiwiZm9yUGFnZVR5cGUiOiJwcmV2IiwibGltaXQiOjN9&page[limit]=3"
  },
  "data": [
    {
      "type": "businessCategories",
      "id": "active:aquariums",
      "attributes": {
        "name": "Vie active > Aquariums"
      }
    },
    {
      "type": "businessCategories",
      "id": "active:archery",
      "attributes": {
        "name": "Vie active > Archerie"
      }
    },
    {
      "type": "businessCategories",
      "id": "active:badminton",
      "attributes": {
        "name": "Vie active > Badminton"
      }
    }
  ]
}
```
For more details on this endpoint see [List Business Categories](../../openapi/platform/platform.yaml/paths/~1businessCategories/get)


### Creating a Sales Contact 

When creating a Contact for a Business you may also link that Contact to a Location in the same API call.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/salesContacts",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token with 'sales.contact' scope>",
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
        "businessLocations": {
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
      "businessLocations": {
        "links": {
          "related": "https://prod.apigateway.co/platform/salesContacts/CO-0739c46cc4794157bf962bd73ce897a7/businessLocations",
          "self": "https://prod.apigateway.co/platform/salesContacts/CO-0739c46cc4794157bf962bd73ce897a7/relationships/businessLocations"
        },
        "data": [
          {
            "type": "businessLocations",
            "id": "AG-1234"
          }
        ]
      }
    }
  }
}
```

For more details on this endpoint see [Create Sales Contacts](../../openapi/platform/platform.yaml/paths/~1salesContacts/post)

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
    "Authorization": "Bearer <Access Token with 'business' scope>",
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
For more details on this endpoint see [Update Business Locations](../../openapi/platform/platform.yaml/paths/~1businessLocations~1%7Bid%7D/patch)

### Get a list of your business locations

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/businessLocations",
  "query": {
    "filter[businessPartner.id]": "ABC",
    "fields[businessLocations]": "name",
    "page[limit]": "2"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>",
    "Content-Type": "application/vnd.api+json",
    "Accept-Encoding": "application/vnd.api+json"
  }
}
```

Will return a list of business location:

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
    "next": "https://prod.apigateway.co/platform/businessLocations?page[cursor]=nop"
  }
}
```

For more details on this endpoint see [List Business Locations](../../openapi/platform/platform.yaml/paths/~1businessLocations~1%7Bid%7D/get)
