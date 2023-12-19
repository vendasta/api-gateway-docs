# Sell your products

## Setup:

**API Authorization**

Create an access token with at least the `order` and `business` scopes following the [Authorization guide](../../Authorization/Authorization.md).

**Start selling skus that you want to order**

Products that you are [selling](https://support.vendasta.com/hc/en-us/articles/4406952901015-Start-selling-Marketplace-products) can be sold to a Sales Account by creating an order. If a product is marked as selling, it's Add-ons will automatically also be marked as selling.

You will need the product SKU ([found here](FindSKU.md)), the ID of a previously created Sales Account, and the price you will be charging to your customer. 

<!-- theme: warning -->
> SKUs with order forms are unable to be activated via API, or Bulk Action at this time.

## Step 1: Add a Sales Account

<!--
type: tab
title: Request
-->

If a Sales Account doesn't exist for the business you are wanting to submit an order for - you can add the Account via the API prior to submitting your order. Fill in as much info as you have for your lead on a new Account in the body section. Add the access token to the headers and send it off. 

In the response, you will find the ID of your newly created Sales Account. You will need that for step 2.



```json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/salesAccounts",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token with 'sales.account' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "salesAccounts",
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
For more details on this endpoint see [Create Sales Account](../../../openapi/platform/platform.yaml/paths/~1salesAccounts/post)


<!--
type: tab
title: Example Response
-->


## Sell a single product
The following request will activate the Standard edition of Customer Voice (MP-c4974d390a044c28aec31e421aa662b2:EDITION-TC8HJZNS) for business location AG-1234567. 

<!-- theme: info -->
>If automatic invoicing is turned on an invoice in the amount of $18.00 AUD will be created.

```json http
{
  "method": "post",
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
      "currencyCode": "AUD",
      "lineItems": [
        {
          "sku": "MP-c4974d390a044c28aec31e421aa662b2:EDITION-TC8HJZNS",
          "quantity": 1,
          "amount": 1800,
          "intervalCode": "monthly"
        }
      ]
    },
    "relationships": {
      "businessLocation": {
        "data": {
          "id": "AG-1234567",
          "type": "businessLocations"
        }
      }
    }
  }
}
}
```

In the response from creating the order you will notice the `statusCode` immediatly goes to `processing` while the products start to be activated. 
```json
{
  "data": {
    "type": "orders",
    "id": "AG-1234567:ORD-SJFGNNP55T",
    "attributes": {
      "lineItems": [
        {
          "sku": "MP-fba21121b71148c9bb33e11fcd92d520:EDITION-4WWZC3RJ",
          "quantity": 1,
          "amount": 1800,
          "intervalCode": "monthly"
        }
      ],
      "currencyCode": "AUD",
      "statusCode": "processing"
    },
    "relationships": {
      "businessLocation": {
        "links": {
          "related": "https://prod.apigateway.co/platform/orders/AG-1234567:ORD-SJFGNNP55T/businessLocation",
          "self": "https://prod.apigateway.co/platform/orders/AG-1234567:ORD-SJFGNNP55T/relationships/businessLocation"
        },
        "data": {
          "type": "businessLocations",
          "id": "AG-1234567"
        }
      }
    }
  }
}
```

### Verify the order was processed

You may use the `ID` that was returned when creating an order to check the status periodicly until it reaches a `fulfilled` or `error` status. 

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/orders/AG-1234567:ORD-SJFGNNP55T",
  "headers": {
    "Authorization": "Bearer <Token with 'order' scope>"
  }
}
```

## Sell multiple products at once
You may include multiple products in a single order by adding additional line items. The order status will not switch to `fulfilled` until all products have been activated.

This example will activate Customer Voice at a monthly cost of $16.50 AUD, Local SEO at a montly cost of $27.97 AUD, and Listing Distribution at a yearly cost of $350.97
```json http
{
  "method": "post",
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
      "currencyCode": "AUD",
      "lineItems": [
        {
          "sku": "MP-c4974d390a044c28aec31e421aa662b2:EDITION-TC8HJZNS",
          "quantity": 1,
          "amount": 1650,
          "intervalCode": "monthly"
        },
        {
          "sku": "MS",
          "quantity": 1,
          "amount": 2797,
          "intervalCode": "monthly"
        },
        {
          "sku": "A-GMXXNQ4ZGD",
          "quantity": 1,
          "amount": 35097,
          "intervalCode": "yearly"
        }
      ]
    },
    "relationships": {
      "businessLocation": {
        "data": {
          "id": "AG-1234567",
          "type": "businessLocations"
        }
      }
    }
  }
}
}
```

## Change the edition of a product
Create a new order with the SKU for the desired product edition. The prior activation, if any, will automatically be cancelled when the new edition activates. 

<!-- theme: info -->
>**Upgrades**
> 
>When activating an edition that costs more, the cancellation of the prior activation, and activation of the new sku will occur immediately. The wholesale billing difference will be pro-rated.
>
>**Downgrades**
>
>When activating an edition that costs less, the change over will be held, and automatically occur at the end of the current billing period. **Don't worry if the activation appears 'stuck' in `processing` - this is expected for downgrades.**

## Sell multiple units of a product
For products that can have multiple units purchased creating a new order
will add the number of units specified by the `quantity` attribute to any existing subscriptions. The per unit `amount` will only apply to the new units. 

## Coming soon
 Apply default pricing to the order items
Coming soon

If product has been configured with a Retail Price at Partner Center -> Marketplace -> Manage Products -> Product Settings, you may leave off the `amount` and `intervalCode` to have the configured price be used.
