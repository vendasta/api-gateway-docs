---
tags: [Advertising Intelligence, advertising, campaign, info, campaignInfo]
---
# Getting Ad Campaign Info

`Campaign Info` contains metadata about the advertising campaigns under a connected advertising account, including wether or not the campaign is connected to the Business Location.  In order to access campagin level information or stats, the Business Location must have advanced reporting enabled.

## Setup

Create an access token with `advertising` scopes following the [Authorization guide](../../Authorization/Authorization.md).

## Examples

### Fetching all Campaign Info for a connected account
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
  "url": "https://prod.apigateway.co/products/advertising/campaignInfo",
  "headers": {
    "Authorization": "Bearer <Access Token with 'advertising' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "connectedAccount.id": "AG-HDV4JKPXFR:google:6477366166"
  }
}
```

For more details on this endpoint see [List Campaign Info](../../../openapi/advertising/advertising.yaml/paths/~1campaignInfo/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10740999338",
      "attributes": {
        "campaignName": "DIS-821321303-m1555716-3DX Startup - Display",
        "isConnected": true,
        "status": "paused",
        "startAt": "2020-08-03T00:00:00Z",
        "endAt": "2021-10-05T00:00:00Z"
      }
    },
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10838201922",
      "attributes": {
        "campaignName": "DIS-822540313-m1555716-XDI 3DX Manage Light",
        "isConnected": true,
        "status": "live",
        "startAt": "2020-08-14T00:00:00Z",
        "endAt": "2037-12-30T00:00:00Z"
      }
    },
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10839639780",
      "attributes": {
        "campaignName": "DIS-822581815-m1562396-XDI 3DX Manage Light",
        "isConnected": false,
        "status": "ended",
        "startAt": "2020-08-14T00:00:00Z",
        "endAt": "2021-10-09T00:00:00Z"
      }
    },
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10740999329",
      "attributes": {
        "campaignName": "IP-821321300-m1555716-3DX XDI Manage Light",
        "isConnected": true,
        "status": "paused",
        "startAt": "2020-08-03T00:00:00Z",
        "endAt": "2021-10-05T00:00:00Z"
      }
    },
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10740999332",
      "attributes": {
        "campaignName": "IP-821321301-m1555716-CATIA",
        "isConnected": true,
        "status": "removed",
        "startAt": "2020-08-03T00:00:00Z",
        "endAt": "2021-10-05T00:00:00Z"
      }
    },
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10740999335",
      "attributes": {
        "campaignName": "IP-821321302-m1555716-3DExperience platform for Startups",
        "isConnected": true,
        "status": "paused",
        "startAt": "2020-08-03T00:00:00Z",
        "endAt": "2021-10-05T00:00:00Z"
      }
    }
  ]
}
```
<!--
type: tab-end
-->

### Filtering Campaigns By Date and Connected Status

By default, this endpoint will return all campaigns under the connected account.  If you want to get information for campaigns that start and end within a specific date range, you can set `startAt` and `endAt` parameters.

Similarly, if you want to get only campaigns that are connected or not connected to the business location, you can set the `filter[isConnected]` parameter to either `true` or `false`.

This example will get connected campaigns that run from `2020-07-01` to `2021-10-08`.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/advertising/campaignInfo",
  "headers": {
    "Authorization": "Bearer <Access Token with 'advertising' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "connectedAccount.id": "AG-HDV4JKPXFR:google:6477366166",
    "filter[isConnected]": true,
    "startAt": "2020-07-01T00:00:00Z",
    "endAt": "2021-10-08T00:00:00Z"
  }
}
```

For more details on this endpoint see [List Campaign Info](../../../openapi/advertising/advertising.yaml/paths/~1campaignInfo/get)
<!--
type: tab
title: Example Response
-->
```json
{
  "data": [
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10740999338",
      "attributes": {
        "campaignName": "DIS-821321303-m1555716-3DX Startup - Display",
        "isConnected": true,
        "status": "paused",
        "startAt": "2020-08-03T00:00:00Z",
        "endAt": "2021-10-05T00:00:00Z"
      }
    },
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10740999329",
      "attributes": {
        "campaignName": "IP-821321300-m1555716-3DX XDI Manage Light",
        "isConnected": true,
        "status": "paused",
        "startAt": "2020-08-03T00:00:00Z",
        "endAt": "2021-10-05T00:00:00Z"
      }
    },
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10740999332",
      "attributes": {
        "campaignName": "IP-821321301-m1555716-CATIA",
        "isConnected": true,
        "status": "removed",
        "startAt": "2020-08-03T00:00:00Z",
        "endAt": "2021-10-05T00:00:00Z"
      }
    },
    {
      "type": "campaignInfo",
      "id": "AG-HDV4JKPXFR:google:6477366166:10740999335",
      "attributes": {
        "campaignName": "IP-821321302-m1555716-3DExperience platform for Startups",
        "isConnected": true,
        "status": "paused",
        "startAt": "2020-08-03T00:00:00Z",
        "endAt": "2021-10-05T00:00:00Z"
      }
    }
  ]
}
```
<!--
type: tab-end
-->


