# Sell your products

Products that you have added to your store can be sold to a business location by creating an order.

You simply need the product SKU ([found here](FindSKU.md)), ID of a previously created business location, and the price you will be charging to your customer. 

## Examples

### Sell a simple product (without an order form)
The following request will activate the Express edition of Customer Voice (MP-c4974d390a044c28aec31e421aa662b2:EDITION-TC8HJZNS) for business location AG-1234567. If automatic invoicing is turned on an invoice in the amount of $18.00 AUD will be created.
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


### Change editions of a simple product
Simply create a new order with the SKU for the desired product edition. The prior activation, if any, will automatically be cancelled when the new edition activates. 

Note: When an account changes editions of a product multiple times within a month it may appear to get stuck. This is because it is waiting for the end of the payment period before downgrading the edition.

### Sell multiple products at once
You may include multiple products in a single order by adding additional line items. The order status will not switch to `fulfilled` until all products have been activated.

This example will activate Customer Voice at a monthly cost of $16.50 AUD, Listing Builder at a montly cost of $27.97 AUD, and Listing Distribution at a yearly cost of $350.97
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

### Sell multiple units of a product
For products that can have multiple units purchased creating a new order
will add the number of units specified by the `quantity` attribute to any existing subscriptions. The per unit `amount` will only apply to the new units. 


### Apply default pricing to the order items
**Coming soon**

If product has been configured with a Retail Price at Partner Center -> Marketplace -> Manage Products -> Product Settings, you may leave off the `amount` and `intervalCode` to have the configured price be used.

### Sell a product with an order form
Coming soon