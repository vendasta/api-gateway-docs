---
tags: [Customers]
---
# Sync Contact Information From Your System to Vendasta Business App

> This guide relies on "Trusted Tester" endpoints and is likely to change until all features have been made generally available.

Vendasta allows you to sync contact information from your CRM or POS system onto our platform. Doing so will allow you to use features in our platform like SMS messaging through Vendasta's Inbox or [requesting business review through Customer Voice](./CustomerVoice/SendReviewRequest.md).

## Setup
Create an access token with `customers` scopes following the [Authorization guide](../Authorization/Authorization.md).

## Create a Customer Record
<!--
type: tab
title: Request
-->
``` json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/business/customers",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token with 'customers' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
    "data": {
      "type": "customers",
      "attributes": {
        "givenName": "William",
        "familyName": "Smith",
        "address": {
          "streetAddress": "123 Fake St.",
          "city": "Kalamazoo",
          "regionCode": "US-MI",
          "countryCode": "US"
        },
        "phoneNumbers": [
          "+13065551234"
        ],
        "emailAddresses": [
          "example@email.com"
        ],
        "tags": [
          "2021 Christmas Campaign"
        ],
        "permissionToContact": true
      },
      "relationships": {
        "businessLocation": {
          "data": {
            "id": "AG-123"
          }
        }
      }
    }
  }
}
```
<!--
type: tab
title: Customer Created Response
-->
If the contact information you provide is valid, it will create a new customer record in Vendasta's system and return the newly created record.
```json
{
  "data": {
    "type": "customers",
    "id": "AG-123:CUSTOMER-123",
    "attributes": {
      "givenName": "William",
      "familyName": "Smith",
      "address": {
        "streetAddress": "123 Fake St.",
        "city": "Kalamazoo",
        "regionCode": "US-MI",
        "countryCode": "US"
      },
      "phoneNumbers": [
        "+13065551234"
      ],
      "emailAddresses": [
        "example@email.com"
      ],
      "tags": [
        "2021 Christmas Campaign"
      ],
      "permissionToContact": true,
      "createdAt": "2019-08-24T14:15:22Z",
      "updatedAt": "2019-08-24T14:15:22Z"
    },
    "relationships": {
      "businessLocation": {
        "data": {
          "type": "businessLocation",
          "id": "AG-123"
        }
      }
    }
  }
}
```
<!--
type: tab
title: Customer Conflict Response
-->
If a customer with a matching email or phone number already exists in Vendasta's system for a business location, the endpoint will return a `409 Conflict` error with information on the conflicting attribute and the id of the existing customer record.
```json
{
    "errors": [
        {
              "status": "409",
              "title": "Already exists",
              "details": "A customers already exists with matching values",
              "meta": {
                "conflictingCustomerId": "AG-123:CUSTOMER-456",
                "field": "emailAddresses",
                "fieldValue": "example@email.com",
                "resourceType": "customers"
              }
        }
    ],
    "meta": {
        "requestId": "123"
    },
    "links": {
        "self": {
            "href": "/business/customers"
        }
    }
}
```
<!--
type: tab-end
-->

Once the contact information has been synced onto Vendasta's system you should see the customer record appear in the Business App "Customers" page.