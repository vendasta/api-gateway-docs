---
tags: [Advertising Intelligence, adintel, account, connectedAccount]
---
# Getting Advertising Connected Accounts

A `Connected Account` represents the metadata of an advertising account that has been connected to a specific Business Location in Advertising Intelligence.  

> For info on Business Locations see the [Businesses guide](../Accounts.md)

## Setup

Create an access token with `adintel` scopes following the [Authorization guide](../../Authorization/Authorization.md).

## Examples

### Retrieving A Connected Account With ID

The ID of a connected account is composite of the following parts, separated by `:`
1. The Business Location ID
1. Provider of the account, either `google`, `facebook` or `localAds`
1. The assigned account ID
For example, an account with ID `1324354698` from Google Adwords that's connected for business `AG-X5FZQG6T25` would have its ID to be `AG-X5FZQG6T25:google:1324354698`

A connected account can be retrieved using its ID.  This data can be used to provide an overview of a single advertising account for a business when the advertising account ID is known.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/adintel/connectedAccount/AG-X5FZQG6T25:google:1324354698",
  "headers": {
    "Authorization": "Bearer <Access Token with 'adintel' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

For more details on this endpoint see [Get A Connected Account](../../../openapi/adintel/adintel.yaml/paths/~1connectedAccount/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": {
      "type": "connectedAccount",
      "id": "AG-X5FZQG6T25:google:1324354698",
      "attributes": {
        "name": "AdWords Account",
        "currencyCode": "CAD",
        "credentialsInvalid": false,
        "created": "2020-12-10T00:00:00Z",
        "updated": "2020-12-10T00:00:00Z",
        "provider": "google"
      }
   }
}
```
<!--
type: tab-end
-->


### Fetching Connected Accounts For A Business

Connected accounts can be fetched for a single business location using its Business Location ID.  This data can be used to provide an overview of all advertising accounts for a business, or to fetch account stats afterwards for returned connected accounts.  The following request will fetch all advertising accounts connected to a given business.  In this case, `AG-X5FZQG6T25` has both Google and Facebook advertising accounts connected.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/adintel/connectedAccount",
  "headers": {
    "Authorization": "Bearer <Access Token with 'adintel' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "filter[businessLocation.id]": "AG-X5FZQG6T25"
  }
}
```

For more details on this endpoint see [List Connected Accounts](../../../openapi/adintel/adintel.yaml/paths/~1connectedAccount/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "connectedAccount",
      "id": "AG-X5FZQG6T25:google:1324354698",
      "attributes": {
        "name": "AdWords Account",
        "currencyCode": "CAD",
        "credentialsInvalid": false,
        "created": "2020-12-10T00:00:00Z",
        "updated": "2020-12-10T00:00:00Z",
        "provider": "google"
      }
    },
    {
      "type": "connectedAccount",
      "id": "AG-X5FZQG6T25:facebook:act_63184543",
      "attributes": {
        "name": "Facebook Account",
        "currencyCode": "USD",
        "credentialsInvalid": false,
        "created": "2021-12-10T00:00:00Z",
        "updated": "2021-12-10T00:00:00Z",
        "provider": "facebook"
      }
    }
  ]
}
```
<!--
type: tab-end
-->

## Fetching Connected Accounts For Multiple Businesses

Connected accounts can be fetched for multiple businesses in order to get a high level overview of connection status for multiple businesses at once.  Accounts for multiple businesses are fetched by including multiple Business Location IDs in the request. In this example, `AG-X5FZQG6T25` has a connected Google Ads account, and `AG-X5FZQG6T25` has both Google and Facebook accounts connected.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/adintel/connectedAccount",
  "headers": {
    "Authorization": "Bearer <Access Token with 'adintel' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "filter[businessLocation.id]": "AG-JT87JWQ7KN,AG-X5FZQG6T25"
  }
}
```
For more details on this endpoint see [List Connected Accounts](../../../openapi/adintel/adintel.yaml/paths/~1connectedAccount/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "connectedAccount",
      "id": "AG-JT87JWQ7KN:google:1348315432",
      "attributes": {
        "name": "AdWords Account",
        "currencyCode": "CAD",
        "credentialsInvalid": false,
        "created": "2020-12-10T00:00:00Z",
        "updated": "2020-12-10T00:00:00Z",
        "provider": "google"
      }
    },
    {
      "type": "connectedAccount",
      "id": "AG-X5FZQG6T25:google:1324354698",
      "attributes": {
        "name": "YXE Hairstyles Studio",
        "currencyCode": "CAD",
        "credentialsInvalid": false,
        "created": "2020-12-10T00:00:00Z",
        "updated": "2020-12-10T00:00:00Z",
        "provider": "google"
      }
    },
    {
      "type": "connectedAccount",
      "id": "AG-X5FZQG6T25:facebook:act_63184543",
      "attributes": {
        "name": "YXE Hairstyles Studio",
        "currencyCode": "CAD",
        "credentialsInvalid": false,
        "created": "2020-12-10T00:00:00Z",
        "updated": "2020-12-10T00:00:00Z",
        "provider": "facebook"
      }
    }
  ]
}
```
<!--
type: tab-end
-->

### Filtering Accounts By Provider

By default, this endpoint will return connected accounts for all providers. If you want to get accounts for only one provider, you can set the `filter[provider]` parameter to either `google`, `facebook` or `localAds`.

This example will get all Google Ads accounts connected to the businesses `AG-JT87JWQ7KN` and `AG-X5FZQG6T25`.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/adintel/connectedAccount",
  "headers": {
    "Authorization": "Bearer <Access Token with 'adintel' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "filter[businessLocation.id]": "AG-JT87JWQ7KN,AG-X5FZQG6T25",
    "filter[provider]": "google"
  }
}
```

For more details on this endpoint see [List Connected Accounts](../../../openapi/adintel/adintel.yaml/paths/~1connectedAccount/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "connectedAccount",
      "id": "AG-JT87JWQ7KN:google:1348315432",
      "attributes": {
        "name": "YXE Hairstyles Studio",
        "currencyCode": "CAD",
        "credentialsInvalid": false,
        "created": "2020-12-10T00:00:00Z",
        "updated": "2020-12-10T00:00:00Z",
        "provider": "google"
      }
    },
    {
      "type": "connectedAccount",
      "id": "AG-X5FZQG6T25:google:1324354698",
      "attributes": {
        "name": "Hairstyle Inn",
        "currencyCode": "CAD",
        "credentialsInvalid": false,
        "created": "2020-12-10T00:00:00Z",
        "updated": "2020-12-10T00:00:00Z",
        "provider": "google"
      }
    }
  ]
}
```
<!--
type: tab-end
-->
