---
tags: [businesses, businessLocations]
---
# Businesses

A Business Location is the basic record for storing information about an organization that is generally part of their public profile. More sensitive data can be found in related resources. **Collectively your sales team may refer to this as an "Account"**.
The location may represent a retail store, the business' headquarters, or identify an area they serve(service area business). Accounts may also represent professionals like _Doctors, Lawyers, Real Estate Agents, etc._

## Prerequisites
**Authorization:** For info on creating an access token see the [Authorization guide](../Authorization/Authorization.md)

**Terms**
* `Business Location`: Legacy representation of an Account. Currently we recommend utilizing the SalesAccount API for interacting with Accounts.
* `SalesAccount`: An [Account](https://support.vendasta.com/hc/en-us/articles/4406960310679-Accounts-Overview). The SalesAccount API adds additional field support and is the only way to associate Salespeople with Accounts over API.
* `Company`: CRM record representing a Company, or a location. These are for sales motions, while Accounts are more for billing and finances. See [Accounts vs. Companies](https://support.vendasta.com/hc/en-us/articles/19448215739031-Accounts-vs-Companies-in-Vendasta-What-you-need-to-know). The CRM API is currently a collection of Trusted Tester, and Proposed endpoints.
* `Listing Profile`: A subset of Account data that is pertinent to syncing to Listing Aggregateors, and Listing Providers. The [Listing Profile](../../../openapi/listings/listings.yaml/components/schemas/listingProfiles) is only applicable when the [Local SEO](https://support.vendasta.com/hc/en-us/articles/4406952361495-Local-SEO-Overview) Product is active on an Account. 

> Every Account will automatically create a corresponding Company, but creating a Company will not create corresponding Account. An Account will not be created until there is an event that occurs to necessitate the creation of an associated Account.

## Common Actions

### Creating a SalesAccount

To create a new Business Location make the following call, replacing values as appropriate. You may choose to associate a Salesperson at the time of Account creation via the `salesPeople` relationship.

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/salesAccounts",
  "query": {
    
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'sales.account' scope>",
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

It will return the newly created record including any server populated values. Be sure to save the ID as an External ID or a reference table for future use:

```json
{
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
```
 



For more details on this endpoint see [Create SalesAccount](../../openapi/platform/platform.yaml/paths/~1salesAccounts/post)

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


### Update attribute on an Account
When updating the values for an Account you only need to send the fields that have changed. In this example we are updating the `customerIdentifier` on the SalesAccount with ID `AG-3VDRVLBNJG`. _Note that the ID gets set in both the path and body._ 

```json http
{
  "method": "patch",
  "url": "https://prod.apigateway.co/platform/salesAccounts/AG-3VDRVLBNJG",
  "headers": {
    "Authorization": "Bearer <Access Token with 'sales.account' scope>",
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
For more details on this endpoint see [Update Sales Account](../../openapi/platform/platform.yaml/paths/~1salesAccounts~1{id}/patch)
