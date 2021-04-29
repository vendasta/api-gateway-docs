# Sell your products

Products and packages that you have added to your store can be sold to a business location by creating an order.

You simply need the product SKU ([found here](FindSKU.md)), ID of a previously created business location, and the price you will be charging to your customer. 

## Examples

### Sell a simple product (without an order form)
The following request will activate Customer Voice | Express (MP-c4974d390a044c28aec31e421aa662b2:EDITION-TC8HJZNS) for business location AG-1234567. If automatic invoicing is turned on an invoice in the amount of $18.00 CAD will be created.
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


> TODO Should we break out each "advanced" example to its own page or just the last two? It would let us get page view metrics for the features that are coming soon. 

### Change editions of a simple product
Simply create a new order with the SKU for the desired product edition. The prior activation, if any, will automatically be cancelled when the new edition activates. 

### Sell multiple products at once

### Sell multiple units of a product

### Sell a product with an order form
Coming soon

### Apply default pricing to the order items
If product has been configured with a Retail Price at Partner Center -> Marketplace -> Manage Products -> Product Settings, you may leave off the `amount` and `intervalCode` to have the configured price be used.

### Activate the Online Toolkit
Full example including creating an account group

### Activate Reputation Managment
Full example including creating an account group with the fields Rep Man uses