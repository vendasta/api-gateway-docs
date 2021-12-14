---
tags: [Social Profiles]
---
# Getting Social Profiles

A `Social Profile` represents an account on a social network that has been connected to a specific Location in Social Marketing.

> For info on Business Locations see the [Businesses guide](../Accounts.md)

## Setup

Create an access token with either the `social` or `business` scopes following the [Authorization guide](../../Authorization/Authorization.md).

## Examples

### Fetching All Connected Social Profiles For A Business

Social Profiles for a single Business Location can be fetched using its ID and an appropriate Authorization token. The data can be used to verify what a business has connected for use in Social Marketing and its current status. Future APIs that are in development will use the IDs obtained from this endpoint to let you access additional functionality.

This example request will enable you to get the first 25 connected Social Profiles for a Business Location, and if there are more, will give you a cursor so you can access the rest. 

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/social/socialProfiles",
  "headers": {
    "Authorization": "Bearer <Access Token with 'social' or 'business' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "businessLocation.id": "AG-X5FZQG6T25"
  }
}
```
<!--
type: tab-end
-->

Additional requests then only need a cursor alongside your Authorization token to access the rest of the results.

<!--
type: tab
title: Request
-->
```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/social/socialProfiles",
  "headers": {
    "Authorization": "Bearer <Access Token with 'social' or 'business' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "query": {
    "page[cursor]": ""
  }
}
```
<!--
type: tab-end
-->