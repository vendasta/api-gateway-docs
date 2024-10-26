# Sell products

## Setup:

**API Authorization**

Create an access token with at least the `order` and `business` scopes following the [Authorization guide](../../Authorization/Authorization.md).

**Start selling skus that you want to order**

Products that you are [selling](https://support.vendasta.com/hc/en-us/articles/4406952901015-Start-selling-Marketplace-products) can be sold to a Sales Account by creating an order. If a product is marked as selling, all of its Editions and Add-ons will automatically also be marked as selling. Note that you may be selling items that aren't in your public store, but anything selling may be activated over API whether it is in your public store or not.

You will need the product SKU ([found here](FindSKU.md)), the ID of a previously created Sales Account, and the price you will be charging to your customer. 

<!-- theme: warning -->
> SKUs with order forms are unable to be activated via API, or Bulk Action at this time.

## Step 1: Add a Sales Account

<!--
type: tab
title: Request
-->

If a Sales Account doesn't exist for the business you are wanting to submit an order for - you can add the Account via the API prior to submitting your order. Fill in as much info as you have for your new Account in the body section. Add the access token to the headers and send it off. 

In the response, you will find the ID of your newly created Sales Account of format `AG-XXXXXXXX`. You will need that for step 2.

```json
{
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

```
For more details on this endpoint see [Create Sales Account](../../../openapi/platform/platform.yaml/paths/~1salesAccounts/post)


<!--
type: tab
title: Example Response
-->



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
<!--
type: tab-end
-->


## Step 2: Generate an Order

### Order Status

By default all orders created by API start out in the `processing` status and do not require approval by either a partner admin or SMB admin. You may create orders with a statusCode of `draft` to pre-populate the order and then finish processing within the platform. To create an order and send it to admins for approval use the statusCode of `submitted`.

<!-- theme: warning -->
>**Note - When creating an order for an Account you just created**
>
>It is usually under 5 seconds for a newly created Sales Account/Business Location to be replicated to the billing system and all necessary records created. However, it may take a little longer on high traffic days. 
>
>We recommend checking for the `NotReady` status code in the error response and retrying on that with an exponential backoff.

**Allowed Order Statuses:**
* `draft`: The order has not yet been sent for approval
* `submitted`: The order is ready for approval
* `processing`: The order and its items are being processed
* `declined`: The order has been declined by an admin or a customer
* `fulfilled`: The order and its items have been successfully processed. This includes activating the products with the vendor(s).
* `error`: One or more of the order's items failed to be processed
* `archived`: The order has been marked as archived
* `NotReady`: The order could not be created as a dependent resource is not yet ready to be used

**Verify the order was processed**

You may use the `ID` that was returned when creating an order to check the status periodically until it reaches a `fulfilled` or `error` status. 

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/orders/AG-1234567:ORD-SJFGNNP55T",
  "headers": {
    "Authorization": "Bearer <Token with 'order' scope>"
  }
}
```

### Sell a single product
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

In the response from creating the order you will notice the `statusCode` immediately goes to `processing` while the products start to be activated. 
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



### Sell multiple products at once
You may include multiple products in a single order by adding additional line items. The order status will not switch to `fulfilled` until all products have been activated.

This example will activate Customer Voice at a monthly cost of $16.50 AUD, Local SEO at a monthly cost of $27.97 AUD, and Listing Distribution at a yearly cost of $350.97
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

### Change the edition of a product
Create a new order with the SKU for the desired product edition. The prior activation, if any, will automatically be cancelled when the new edition activates. 

<!-- theme: info -->
>**Upgrades**
> 
>When activating an edition that costs more, the cancellation of the prior activation, and activation of the new sku will occur immediately. The wholesale billing difference will be prorated.
>
>**Downgrades**
>
>When activating an edition that costs less, the change over will be held, and automatically occur at the end of the current billing period. **Don't worry if the activation appears 'stuck' in `processing` - this is expected for downgrades.**

### Sell multiple units of a product
For products that can have multiple units purchased creating a new order
will add the number of units specified by the `quantity` attribute to any existing subscriptions. The per unit `amount` will only apply to the new units. 

### Apply default pricing to the order items

If the product has been configured with a Retail Price at Partner Center -> Marketplace -> Manage Products -> Product Settings, you may leave off the `amount` and `intervalCode` to have the configured price be used.


## Cancel Products

Cancelling products is a two step process: 
1. [List Subscription Assisgments](../../../openapi/platform/platform.yaml/paths/~1subscriptionAssignments/get) for a business location to find the id coorisponding to the SKU that you wish to cancel. 
2. [Cancel Subscription Assignment](../../../openapi/platform/platform.yaml/paths/~1subscriptionAssignments~1{id}~1actions~1requestCancellation/post) by id

By default the product will remain in a pendingUnassignment status until the items anniversary date, or commitment date, whichever is later. You may force the product to be deacivated immediatly.


For example to cancell the `RM:EDITION-F7JZ5TV8` product you would use the `AG-1234567:RM:e8939ab9-1216-4ddf-9fab-dbe344e36869` id from the list subsription assignments call.



<!--
type: tab
title: List Request
-->

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/subscriptionAssignments",
  "query": {
    "filter[businessLocationId]": "AG-1234567"
  },
  "headers": {
    "Authorization": "Bearer <Token with 'sales.account' scope>"
  }

}
```
For full details see [List Subscription Assisgments](../../../openapi/platform/platform.yaml/paths/~1subscriptionAssignments/get)

<!--
type: tab
title: Example List Response
-->
```json
{
  "data": [
    {
      "type": "subscriptionAssignments",
      "id": "AG-1234567:MP-ee4ea04e553a4b1780caf7aad7be07cd:5644336a-acce-4eb7-898c-7178ca97480b",
      "attributes": {
        "sku": "MP-ee4ea04e553a4b1780caf7aad7be07cd",
        "productId": "MP-ee4ea04e553a4b1780caf7aad7be07cd",
        "editionId": "",
        "status": "assigned"
      }
    },
    {
      "type": "subscriptionAssignments",
      "id": "AG-1234567:RM:e8939ab9-1216-4ddf-9fab-dbe344e36869",
      "attributes": {
        "sku": "RM:EDITION-F7JZ5TV8",
        "productId": "RM",
        "editionId": "EDITION-F7JZ5TV8",
        "status": "assigned"
      }
    }
  ]
}
```

<!--
type: tab
title: Cancel Request
-->

```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/platform/subscriptionAssignments/{id}/actions/requestCancellation",
  "query": {
    "id": "AG-1234567:RM:e8939ab9-1216-4ddf-9fab-dbe344e36869"
  },
  "headers": {
    "Authorization": "Bearer <Token with 'sales.account' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
  "reason": [
    "crisisShiftInPriorities"
  ],
  "comments": "Due to crisis",
  "deactivationType": "cancel"
}

}
```

It should return a `204 No Content` status.

For full details see [Cancel Subscription Assignment](../../../openapi/platform/platform.yaml/paths/~1subscriptionAssignments~1{id}~1actions~1requestCancellation/post)



<!--
type: tab-end
-->

