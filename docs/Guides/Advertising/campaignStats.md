---
tags: [Advertising Intelligence, advertising, account, stats, accountstats]
---
# Getting Advertising Campaign Stats

`Account Stats` are the stats of connected campaigns under one advertising account that has been connected to a specific Business Location in Advertising Intelligence.  In order to access campagin level information or stats, the Business Location must have advanced reporting enabled.

## Setup

Create an access token with `advertising` scopes following the [Authorization guide](../../Authorization/Authorization.md).

## Examples

### Fetching all Campaign Stats for a connected account
In order to fetch campaign info for an account, you will need the ID of a Connected Account. 
> For info on Connected Advertising Accounts see the [Connected Accounts guide](./connectedAccount.md)
The ID will be in the format `AG-X5FZQG6T25:google:1324354698`

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/advertising/campaignStats",
  "headers": {
    "Authorization": "Bearer <Access Token with 'advertising' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "connectedAccount.id": "AG-X5FZQG6T25:google:1324354698"
  }
}
```

For more details on this endpoint see [List Account Stats](../../../openapi/advertising/advertising.yaml/paths/~1accountStats/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "campaignStats",
      "id": "AG-X5FZQG6T25:google:1324354698:543211234",
      "attributes": {
        "impressions": 5136,
        "clicks": 99,
        "conversions": 2,
        "costMicros": 53000000,
        "averageCostPerClickMicros": 535353,
        "clickThroughRate": 0.019275,
        "currencyCode": "CAD",
      }
    },
    {
      "type": "accountStats",
      "id": "AG-X5FZQG6T25:google:1324354698:395728305",
      "attributes": {
        "impressions": 5145,
        "clicks": 66,
        "conversions": 0,
        "costMicros": 47000000,
        "averageCostPerClickMicros": 712121,
        "clickThroughRate": 0.012828,
        "currencyCode": "CAD",
      }
    }
  ]
}
```
<!--
type: tab-end
-->

### Filtering Stats By Date

By default, this endpoint will return stats for all time.  If you want to get account stats for a specific date range, you can set `startAt` and `endAt` parameters.

This example will get stats from `2021-01-01` to `2021-02-01` for all campaigns under the connected account `AG-X5FZQG6T25:google:1324354698`

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/advertising/campaignStats",
  "headers": {
    "Authorization": "Bearer <Access Token with 'advertising' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "connectedAccount.id": "AG-X5FZQG6T25:google:1324354698"
  }
}
```

For more details on this endpoint see [List Account Stats](../../../openapi/advertising/advertising.yaml/paths/~1accountStats/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "campaignStats",
      "id": "AG-X5FZQG6T25:google:1324354698:543211234",
      "attributes": {
        "impressions": 2156,
        "clicks": 99,
        "conversions": 2,
        "costMicros": 53000000,
        "averageCostPerClickMicros": 535353,
        "clickThroughRate": 0.045918,
        "currencyCode": "CAD",
      }
    },
    {
      "type": "accountStats",
      "id": "AG-X5FZQG6T25:google:1324354698:395728305",
      "attributes": {
        "impressions": 3512,
        "clicks": 66,
        "conversions": 0,
        "costMicros": 47000000,
        "averageCostPerClickMicros": 712121,
        "clickThroughRate": 0.018792,
        "currencyCode": "CAD",
      }
    }
  ]
}
```
<!--
type: tab-end
-->
