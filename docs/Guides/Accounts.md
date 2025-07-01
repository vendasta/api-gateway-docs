---
tags: [accounts, businesses, businessLocations, contacts]
---
# Businesses

An `Account` typically represents a single business location.
The location may be a retail store, the business' headquarters, or identify an area they serve. Sometimes an `Account` may represent a professional such as a Realtor, Dr., or Lawyer, etc.

## Common Actions

> For info on creating an access token see the [Authorization guide](../Authorization/Authorization.md). *For quick tests use the 'Generate Token' button at the top of the Developer Center, and send test requests directly from the documentation.*

### Creating an Account

To create a new `Account` using the standard API Gateway make the following call, replacing values as appropriate:

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/salesAccounts",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "salesAccounts",
      "attributes": {
        "tags": [
          "tag1",
          "tag2"
        ],
        "customerIdentifier": "User-defined-id-123123",
        "name": "Company Example",
        "address": {
          "line1": "109 8th Street E.",
          "line2": "Suite 23",
          "city": "Saskatoon",
          "stateCode": "CA",
          "zip": "S7M 1R3",
          "postalCode": "S7M 1R3",
          "regionCode": "CA-SK",
          "countryCode": "CA"
        },
        "phoneNumbers": [
          "+13068800001"
        ],
        "serviceAreaBusiness": true,
        "geoCoordinate": {
          "latitude": -90,
          "longitude": -180
        }
      },
      "relationships": {
        "salesPeople": {
          "data": [
            {
              "id": "U-123123-123123",
              "type": "users"
            }
          ]
        },
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
              "id": "active:diving:freediving"
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
    "id": "AG-1234567",
    "type": "salesAccounts",
    "attributes": {
      "tags": [
        "tag1",
        "tag2"
      ],
      "customerIdentifier": "User-defined-id-123123",
      "name": "Company Example",
      "address": {
        "line1": "109 8th Street E.",
        "line2": "Suite 23",
        "city": "Saskatoon",
        "stateCode": "CA",
        "zip": "S7M 1R3",
        "postalCode": "S7M 1R3",
        "regionCode": "CA-SK",
        "countryCode": "CA"
      },
      "phoneNumbers": [
        "+13068800001"
      ],
      "serviceAreaBusiness": true,
      "geoCoordinate": {
        "latitude": -90,
        "longitude": -180
      }
    },
    "relationships": {
      "salesPeople": {
        "data": [
          {
            "id": "U-123123-123123",
            "type": "users"
          }
        ]
      },
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
            "id": "active:diving:freediving"
          }
        ]
      },
      "type": "salesAccounts"
    }
  }
}
```

For more details on this endpoint see [Create salesAccounts](../../openapi/platform/platform.yaml/paths/~1salesAccounts/post)

### List valid Business Categories
When creating an `Account` you will likely want to know what ids you can use for the businessCategories field. 

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
    "first": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=&page[limit]=3",
    "next": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=3&page[limit]=3"
  },
  "data": [
    {
      "type": "businessCategories",
      "id": "acrobatic_diving_pool",
      "attributes": {
        "name": "Acrobatic diving pool"
      }
    },
    {
      "type": "businessCategories",
      "id": "disabled_sports_center",
      "attributes": {
        "name": "Adaptive sports center"
      }
    },
    {
      "type": "businessCategories",
      "id": "adventure_sports",
      "attributes": {
        "name": "Adventure sports"
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
    "page[cursor]": 3
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
        "first": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=&page[limit]=3",
        "next": "https://prod.apigateway.co/platform/businessCategories?page[cursor]=6&page[limit]=3"
    },
    "data": [
        {
            "type": "businessCategories",
            "id": "adventure_sports_center",
            "attributes": {
                "name": "Adventure sports center"
            }
        },
        {
            "type": "businessCategories",
            "id": "aerial_sports_center",
            "attributes": {
                "name": "Aerial sports center"
            }
        },
        {
            "type": "businessCategories",
            "id": "aero_dance_class",
            "attributes": {
                "name": "Aero dance class"
            }
        }
    ]
}
```
For more details on this endpoint see [List Business Categories](../../openapi/platform/platform.yaml/paths/~1businessCategories/get)


### Update attribute on an Account
When updating the values for an Account you only need to send the fields that have changed. In this example we are updating the `customerIdentifier` on the Account with ID `AG-3VDRVLBNJG`. Note that the Account ID gets set in both the path and body. 

```json http
{
  "method": "patch",
  "url": "https://prod.apigateway.co/platform/salesAccounts/AG-3VDRVLBNJG",
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "id": "AG-3VDRVLBNJG",
      "type": "salesAccounts",
      "attributes": {
        "customerIdentifier": "CUST-5"
      }
    }
  }
}
```
For more details on this endpoint see [Update salesAccount](../../openapi/platform/platform.yaml/paths/~1salesAccounts~1%7Bid%7D/patch)
