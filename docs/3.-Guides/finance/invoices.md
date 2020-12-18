# Invoices

Invoices are used to track requests for payment from one organization to another for products that have been provided. 

## Common Actions

### View invoices sent to you

To view the invoices that have been sent to you make the following request filling in your partner ID and an [access token](https://vendasta.stoplight.io/docs/openapi-specs/docs/2.-Authorization/2-legged-oauth/3.-UsingAServiceAccount.md).

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/invoices",
  "query": {
    "filter[recipient.id]": "<Your partner ID>",
    "page[limit]": "10"
  },
  "headers": {
    "Authorization": "Bearer <access token>"
  }
}
```

That will give you a response similar to 

```json
{
  "links": {
    "first": "https://prod.apigateway.co/platform/invoices?filter[recipient.id]=VUNI&page[cursor]=&page[limit]=2",
    "next": "https://prod.apigateway.co/platform/invoices?filter[recipient.id]=VUNI&page[cursor]=Mg==&page[limit]=2"
  },
  "data": [
    {
      "type": "invoices",
      "id": "3114ee2969c2f4ec",
      "attributes": {
        "createdAt": "2020-12-18T17:16:24Z",
        "statusCode": "paid",
        "currencyCode": "CAD",
        "discountAmount": 0,
        "taxAmount": 28,
        "total": 278,
        "lineItems": [
          {
            "sku": "MS",
            "customerId": "AG-CB55LS3M4M",
            "unitAmount": 500,
            "quantity": 1,
            "discountAmount": 250,
            "taxAmount": 28,
            "total": 278
          }
        ]
      },
      "relationships": {
        "recipient": {
          "links": {
            "related": "https://prod.apigateway.co/platform/invoices/3114ee2969c2f4ec/recipient",
            "self": "https://prod.apigateway.co/platform/invoices/3114ee2969c2f4ec/relationships/recipient"
          },
          "data": {
            "type": "invoices",
            "id": "VUNI"
          }
        }
      }
    },
    {
      "type": "invoices",
      "id": "6771d24790d2c0a9",
      "attributes": {
        "createdAt": "2020-12-18T17:12:30Z",
        "statusCode": "paid",
        "currencyCode": "CAD",
        "discountAmount": 0,
        "taxAmount": 0,
        "total": 0,
        "lineItems": [
          {
            "sku": "RM-TRIAL",
            "customerId": "AG-WMBDR3QMLF",
            "unitAmount": 0,
            "quantity": 1,
            "discountAmount": 0,
            "taxAmount": 0,
            "total": 0
          }
        ]
      },
      "relationships": {
        "recipient": {
          "links": {
            "related": "https://prod.apigateway.co/platform/invoices/6771d24790d2c0a9/recipient",
            "self": "https://prod.apigateway.co/platform/invoices/6771d24790d2c0a9/relationships/recipient"
          },
          "data": {
            "type": "invoices",
            "id": "VUNI"
          }
        }
      }
    }
  ]
}
```

If there are a large number of invoices they will be broken into multiple pages. Notice the `links.next` in the response. You may make a GET request to that URI with your Authorization header to retrive the next page of matches. 

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/invoices",
  "query": {
    "filter[recipient.id]": "VUNI",
    "page[cursor]": "Mg==",
    "page[limit]": "2"
  },
  "headers": {
    "Authorization": "Bearer <access token>"
  }
}
```
