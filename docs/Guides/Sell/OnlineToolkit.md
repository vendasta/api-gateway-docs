# Activate the Online Toolkit for a new lead

When you have a lead for a new business in your own system you can easily add it to the Vendasta platform and activate the [Local Business Online Toolkit](https://www.vendasta.com/protect-local/local-business-online-toolkit/) for it. 

## Setup:

Create an access token with at least the `order` and `business` scopes following the [Authorization guide](../../Authorization/Authorization.md).

## Step 1: Add the business location

<!--
type: tab
title: Request
-->

Fill in as much info as you have for your lead on a new business location in the body section. Add the access token to the headers and send it off. 

In the response, you will find the ID of your newly created business location. You will need that for step 2.



```json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/businessLocations",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token with 'business' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "businessLocations",
      "attributes": {
        "address": {
          "city": "Saskatoon",
          "countryCode": "CA",
          "line1": "234 River Ave",
          "line2": "Below the dock",
          "stateCode": "SK",
          "zip": "S7H 0V6"
        },
        "name": "Fred's Fish & Chips",
        "phoneNumbers": [
          "+1 (306) 555-8996"
        ]
      },
      "relationships": {
        "businessCategories": {
          "data": [
            {
              "id": "active:diving:freediving",
              "type": "businessCategories"
            },
            {
              "id": "restaurants:fishnchips",
              "type": "businessCategories"
            }
          ]
        },
        "businessPartner": {
          "data": {
            "id": "VUNI",
            "type": "partners"
          }
        }
      }
    }
  }
}

```
For more details on this endpoint see [Create Business Locations](../../../openapi/platform/platform.yaml/paths/~1businessLocations/post).


<!--
type: tab
title: Example Response
-->



```json
{
  "data": {
    "type": "businessLocations",
    "id": "AG-Z62G4D8863",
    "attributes": {
      "customerIdentifier": "",
      "name": "Fred's Fish & Chips",
      "address": {
        "line1": "234 River Ave",
        "line2": "Below the dock",
        "city": "Saskatoon",
        "stateCode": "SK",
        "zip": "S7H 0V6",
        "countryCode": "CA"
      },
      "geoCoordinate": {
        "latitude": 0,
        "longitude": 0
      },
      "serviceAreaBusiness": false,
      "phoneNumbers": [
        "+1 (306) 555-8996"
      ]
    },
    "relationships": {
      "businessCategories": {
        "links": {
          "related": "https://prod.apigateway.co/platform/businessLocations/AG-Z62G4D8863/businessCategories",
          "self": "https://prod.apigateway.co/platform/businessLocations/AG-Z62G4D8863/relationships/businessCategories"
        },
        "data": [
          {
            "type": "businessCategories",
            "id": "active:diving:freediving"
          },
          {
            "type": "businessCategories",
            "id": "restaurants:fishnchips"
          }
        ]
      },
      "businessPartner": {
        "links": {
          "related": "https://prod.apigateway.co/platform/businessLocations/AG-Z62G4D8863/businessPartner",
          "self": "https://prod.apigateway.co/platform/businessLocations/AG-Z62G4D8863/relationships/businessPartner"
        },
        "data": {
          "type": "partners",
          "id": "VUNI"
        }
      }
    }
  }
}
```
<!--
type: tab-end
-->



## Step 2: Place the order

<!--
type: tab
title: Request
-->

Using the ID of the business location we can now create an order to activate the express editions of products that are part of the Local Business Online Toolkit. This request will create an order for Website Express, Customer Voice Express, Reputation Management Express, Local SEO, Social Marketing Express.

```json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/orders",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token with 'order' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "orders",
      "attributes": {
        "currencyCode": "CAD",
        "lineItems": [
          {
            "sku": "MP-ee4ea04e553a4b1780caf7aad7be07cd:EDITION-VFNL43ZF"
          },
          {
            "sku": "MP-c4974d390a044c28aec31e421aa662b2:EDITION-TC8HJZNS"
          },
          {
            "sku": "RM:EDITION-F7JZ5TV8"
          },
          {
            "sku": "MS"
          },
          {
            "sku": "SM:EDITION-FVGBNLVZ"
          }
        ]
      },
      "relationships": {
        "businessLocation": {
          "data": {
            "id": "AG-Z62G4D8863",
            "type": "businessLocations"
          }
        }
      }
    }
  }
}

```
<!--
type: tab
title: Example Response
-->


```json
{
  "data": {
    "type": "orders",
    "id": "AG-Z62G4D8863:ORD-MDKV2X4NFG",
    "attributes": {
      "lineItems": [
        {
          "sku": "MP-ee4ea04e553a4b1780caf7aad7be07cd:EDITION-VFNL43ZF",
          "quantity": 0,
          "amount": 0,
          "intervalCode": ""
        },
        {
          "sku": "MP-c4974d390a044c28aec31e421aa662b2:EDITION-TC8HJZNS",
          "quantity": 0,
          "amount": 0,
          "intervalCode": ""
        },
        {
          "sku": "RM:EDITION-F7JZ5TV8",
          "quantity": 0,
          "amount": 0,
          "intervalCode": ""
        },
        {
          "sku": "MS",
          "quantity": 0,
          "amount": 0,
          "intervalCode": ""
        },
        {
          "sku": "SM:EDITION-FVGBNLVZ",
          "quantity": 0,
          "amount": 0,
          "intervalCode": ""
        }
      ],
      "currencyCode": "CAD",
      "statusCode": "processing"
    },
    "relationships": {
      "businessLocation": {
        "links": {
          "related": "https://prod.apigateway.co/platform/orders/AG-Z62G4D8863:ORD-MDKV2X4NFG/businessLocation",
          "self": "https://prod.apigateway.co/platform/orders/AG-Z62G4D8863:ORD-MDKV2X4NFG/relationships/businessLocation"
        },
        "data": {
          "type": "businessLocations",
          "id": "AG-Z62G4D8863"
        }
      }
    }
  }
}
```

Note: It can take up to a couple of seconds for a newly created business location to make it into the ordering system. If you receive a 404 error status with code `NotReady` simply retry the request after a brief pause. 

<!--
type: tab-end
-->

## Upgrading to pro
At some point, your lead will turn into a sale. When that happens you can change editions by creating a new order with the SKU for the pro edition. You may also specify the retail price that you sold it for.

<!--
type: tab
title: Request
-->
This request will activate the pro edition of Customer Voice at a price of $52.29 CAD per month. Adjust it to meet your needs. 


```json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/orders",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token with 'order' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "orders",
      "attributes": {
        "currencyCode": "CAD",
        "lineItems": [
          {
            "amount": 5229,
            "intervalCode": "monthly",
            "quantity": 1,
            "sku": "MP-c4974d390a044c28aec31e421aa662b2"
          }
        ]
      },
      "relationships": {
        "businessLocation": {
          "data": {
            "id": "AG-Z62G4D8863",
            "type": "businessLocations"
          }
        }
      }
    }
  }
}

```
<!--
type: tab
title: Example Response
-->



```json
{
  "data": {
    "type": "orders",
    "id": "AG-Z62G4D8863:ORD-S3SBQC2FZ7",
    "attributes": {
      "lineItems": [
        {
          "sku": "MP-c4974d390a044c28aec31e421aa662b2",
          "quantity": 1,
          "amount": 5229,
          "intervalCode": "monthly"
        }
      ],
      "currencyCode": "CAD",
      "statusCode": "processing"
    },
    "relationships": {
      "businessLocation": {
        "links": {
          "related": "https://prod.apigateway.co/platform/orders/AG-Z62G4D8863:ORD-S3SBQC2FZ7/businessLocation",
          "self": "https://prod.apigateway.co/platform/orders/AG-Z62G4D8863:ORD-S3SBQC2FZ7/relationships/businessLocation"
        },
        "data": {
          "type": "businessLocations",
          "id": "AG-Z62G4D8863"
        }
      }
    }
  }
}
```
<!--
type: tab-end
-->