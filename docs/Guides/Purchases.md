# Purchases

Purchases are used to track wholesale transactions for products. 

For advanced usage see the [full details](../../openapi/platform/platform.yaml/paths/~1purchases/get).

## Common Actions

### View your purchases

To view the past purchases for your Partner you may make the following request, filling in your partner ID and [access token](../Authorization/2-legged-oauth/UsingAServiceAccount.md). This example filters the list to the first 10 purchases created between Dec 10th and 20th. 

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/purchases",
  "query": {
    "filter[partner.id]": "<Your partner ID>",
    "filter[createdAt][>]": "2024-12-10T00:00:00Z",
    "filter[createdAt][<]": "2024-12-20T00:00:00Z",
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
    "first": "https://prod.apigateway.co/platform/purchases?filter[partner.id]=VUNI&page[cursor]=&page[limit]=2",
    "next": "https://prod.apigateway.co/platform/purchases?filter[partner.id]=VUNI&page[cursor]=Mg==&page[limit]=2"
  },
  "data": [
    {
      "type": "purchases",
      "id": "3114ee2969c2f4ec",
      "attributes": {
        "createdAt": "2024-12-18T17:16:24Z",
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
            "related": "https://prod.apigateway.co/platform/purchases/3114ee2969c2f4ec/partner",
            "self": "https://prod.apigateway.co/platform/purchases/3114ee2969c2f4ec/relationships/partner"
          },
          "data": {
            "type": "purchases",
            "id": "VUNI"
          }
        }
      }
    },
    {
      "type": "purchases",
      "id": "6771d24790d2c0a9",
      "attributes": {
        "createdAt": "2024-12-18T17:12:30Z",
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
            "related": "https://prod.apigateway.co/platform/purchases/6771d24790d2c0a9/partner",
            "self": "https://prod.apigateway.co/platform/purchases/6771d24790d2c0a9/relationships/partner"
          },
          "data": {
            "type": "purchases",
            "id": "VUNI"
          }
        }
      }
    }
  ]
}
```

If there are a large number of purchases they will be broken into multiple pages. Notice the `links.next` in the response. You may make a GET request to that URI with your Authorization header to retrieve the next page of matches. 

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/purchases",
  "query": {
    "filter[partner.id]": "VUNI",
    "page[cursor]": "Mg==",
    "page[limit]": "2"
  },
  "headers": {
    "Authorization": "Bearer <access token>"
  }
}
```
