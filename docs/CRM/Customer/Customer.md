---
tags: [customer]
---

# Customers

A customer is an organization that has purchased a product from your organization. Organizations that have not made a purchase may be accessable as [prospects](<>).

## Common actions

### Get a list of your customers

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/crm/customers",
  "query": {
    "filter[parent]": "me",
    "fields[crmCustomer]": "name",
    "page[limit]": 2
  },
  "headers": {
    "Authorization": "Bearer {your token}",
    "Content-Type": "application/vnd.api+json",
    "Accept-Encoding": "application/vnd.api+json"
  }
}
```

Will return something like 

```json
{
  "data": [
    {
      "type": "crmCustomer",
      "id": "1234",
      "attributes": {
        "name": "Bill's Bakery"
      }
    },
    {
      "type": "crmCustomer",
      "id": "5642",
      "attributes": {
        "name": "Shane's Snack Hut"
      }
    }
  ],
  "links": {
    "first": "https://prod.apigateway.co/crm/customers?page[cursor]=abc",
    "self": "https://prod.apigateway.co/crm/customers?page[cursor]=klm",
    "next": "https://prod.apigateway.co/crm/customers?page[cursor]=nop",
    "last": "https://prod.apigateway.co/crm/customers?page[cursor]=xyz"
  },
  "meta": {
    "total": 234
  }
}
```

### Find a customer using the ID from your external system

### 
