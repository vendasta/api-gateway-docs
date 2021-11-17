---
tags: [listings]
---

# Listing

An online webpage containing an entities location, hours, and other vital business information.

## Common Actions

## View listings for an entity

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/listingBuilder/listListings",
  "query": {
    "filter[businessLocations.id]": "AG-123456",
    "page[limit]": "2"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>"
  }
}
```