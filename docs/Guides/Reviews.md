---
tags: [reviews]
---

# Reviews

A review of an item - for example, of a restaurant, movie, or store.

## Common Actions

### View reviews for a business location

To view the reviews for a business location make the following request filling in the business location ID and an [access token](../Authorization/2-legged-oauth/UsingAServiceAccount.md). 
This example filters the list to the most recent 2 reviews: 


```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/reputation",
  "query": {
    "filter[businessLocations.id]": "AG-123456",
    "page[limit]": "2"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'business' scope>"
  }
}
```

That will give you a response similar to:

```json
{
  "links": {
    "first": "https://prod.apigateway.co/products/reputation?filter[businessLocations.id]=AG-123456&page[cursor]=&page[limit]=2",
    "next": "https://prod.apigateway.co/products/reputation?filter[businessLocations.id]=AG-123456&page[cursor]=Mg==&page[limit]=2"
  },
  "data": [
    <insert reviews sample here>
  ]
}
```

If there are a large number of reviews they will be broken into multiple pages. Notice the `links.next` in the response. You may make a GET request to that URI with your Authorization header to retrieve the next page of matches. 

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/products/reputation",
  "query": {
    "filter[businessLocations.id]": "AG-123456",
    "page[cursor]": "Mg==",
    "page[limit]": "2"
  },
  "headers": {
    "Authorization": "Bearer <access token>"
  }
}
```