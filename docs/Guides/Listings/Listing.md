---
tags: [listings]
---

# Listing

An online webpage containing an entity's location, service area, hours, and other vital business information.

## Common Actions

## View listings for an entity

To view the listings for a business entity make the following request filling in the business location ID and
an [access token](../../Authorization/2-legged-oauth/UsingAServiceAccount.md)

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/listingBuilder/listListings",
  "query": {
    "filter[businessLocations.id]": "AG-123456",
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>"
  }
}
```

That will give you a response similar to:

```json
{
  "data": [
    <insert listing sample here>
  ]
}
```