---
tags: [Advertising Intelligence, adintel, account, stats, accountstats]
---
# Getting Advertising Account Stats

`Account Stats` are the total stats of all campaigns under one advertising account that has been connected to a specific Business Location in Advertising Intelligence.  

> For info on Business Locations see the [Businesses guide](../Accounts.md)

## Setup

Create an access token with `adintel` scopes following the [Authorization guide](../../Authorization/Authorization.md).

## Examples

### Fetching Account Stats For A Business

Account stats can be fetched for a single business location using its Business Location ID.  This data can be used to provide an overview of a business's advertising performance.  The following request will fetch stats for all advertising accounts connected to a given business.  In this case, `AG-X5FZQG6T25` has both Google and Facebook advertising accounts connected.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/products/adintel/accountStats",
  "headers": {
    "Authorization": "Bearer <Access Token with 'adintel' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "filter[businessLocation.id]": "AG-X5FZQG6T25"
  }
}
```

For more details on this endpoint see [List Account Stats](../../../openapi/adintel/adintel.yaml/paths/~1accountStats/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "accountStats",
      "id": "AG-X5FZQG6T25:google:1324354698",
      "attributes": {
        "impressions": 5136,
        "clicks": 99,
        "conversions": 2,
        "costMicros": 53000000,
        "averageCostPerClickMicros": 535353,
        "clickThroughRate": 0.019275,
        "currencyCode": "CAD",
        "provider": "google"
      }
    },
    {
      "type": "accountStats",
      "id": "AG-X5FZQG6T25:facebook:act_63184543",
      "attributes": {
        "impressions": 5145,
        "clicks": 66,
        "conversions": 0,
        "costMicros": 47000000,
        "averageCostPerClickMicros": 712121,
        "clickThroughRate": 0.012828,
        "currencyCode": "CAD",
        "provider": "facebook"
      }
    }
  ]
}
```
<!--
type: tab-end
-->

## Fetching Account Stats For Multiple Businesses

Account stats can be fetched for multiple businesses in order to get a high level overview for multiple businesses at once that can be used for comparison.  Stats for multiple businesses are fetched by including multiple Business Location IDs in the request. In this example, `AG-X5FZQG6T25` has a connected Google Ads account, and `AG-X5FZQG6T25` has both Google and Facebook accounts connected.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/products/adintel/accountStats",
  "headers": {
    "Authorization": "Bearer <Access Token with 'adintel' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "filter[businessLocation.id]": "AG-JT87JWQ7KN,AG-X5FZQG6T25"
  }
}
```
For more details on this endpoint see [List Account Stats](../../../openapi/adintel/adintel.yaml/paths/~1accountStats/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "accountStats",
      "id": "AG-JT87JWQ7KN:google:1348315432",
      "attributes": {
        "impressions": 3968,
        "clicks": 105,
        "conversions": 1,
        "costMicros": 1197416000,
        "averageCostPerClickMicros": 11403962,
        "clickThroughRate": 0.0265,
        "currencyCode": "USD",
        "provider": "google"
      }
    },
    {
      "type": "accountStats",
      "id": "AG-X5FZQG6T25:google:1324354698",
      "attributes": {
        "impressions": 5136,
        "clicks": 99,
        "conversions": 2,
        "costMicros": 53000000,
        "averageCostPerClickMicros": 535353,
        "clickThroughRate": 0.019275,
        "currencyCode": "CAD",
        "provider": "google"
      }
    },
    {
      "type": "accountStats",
      "id": "AG-X5FZQG6T25:facebook:act_63184543",
      "attributes": {
        "impressions": 5145,
        "clicks": 66,
        "conversions": 0,
        "costMicros": 47000000,
        "averageCostPerClickMicros": 712121,
        "clickThroughRate": 0.012828,
        "currencyCode": "CAD",
        "provider": "facebook"
      }
    }
  ]
}
```
<!--
type: tab-end
-->

### Filtering Stats By Date and Provider

By default, this endpoint will return stats for all time.  If you want to get account stats for a specific date range, you can set `startAt` and `endAt` parameters.

Similarly, if you want to get stats for only one provider, you can set the `filter[provider]` parameter to either `google`, `facebook` or `localAds`.

This example will get stats from `2021-01-01` to `2021-02-01` for all Google Ads accounts connected to the businesses `AG-JT87JWQ7KN` and `AG-X5FZQG6T25`.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "post",
  "url": "https://prod.apigateway.co/products/adintel/accountStats",
  "headers": {
    "Authorization": "Bearer <Access Token with 'adintel' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "filter[businessLocation.id]": "AG-JT87JWQ7KN,AG-X5FZQG6T25",
    "filter[provider]": "google",
    "startAt": "2021-01-01T00:00:00Z",
    "endAt": "2021-02-01T00:00:00Z"
  }
}
```

For more details on this endpoint see [List Account Stats](../../../openapi/adintel/adintel.yaml/paths/~1accountStats/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "accountStats",
      "id": "AG-JT87JWQ7KN:google:1348315432",
      "attributes": {
        "impressions": 1654,
        "clicks": 52,
        "conversions": 2,
        "costMicros": 242350000,
        "averageCostPerClickMicros": 4660577,
        "clickThroughRate": 0.0314,
        "currencyCode": "USD",
        "provider": "google"
      }
    },
    {
      "type": "accountStats",
      "id": "AG-X5FZQG6T25:google:1324354698",
      "attributes": {
        "impressions": 1654,
        "clicks": 52,
        "conversions": 2,
        "costMicros": 242350000,
        "averageCostPerClickMicros": 4660577,
        "clickThroughRate": 0.0314,
        "currencyCode": "CAD",
        "provider": "google"
      }
    }
  ]
}
```
<!--
type: tab-end
-->
