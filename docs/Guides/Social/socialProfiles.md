---
tags: [Social Profiles]
---
# Getting Social Profiles

A `Social Profile` represents an account on a social network that has been connected to a specific Business Location in Social Marketing.

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
    "businessLocation.id": "AG-ABCD1234G"
  }
}
```
<!--
type: tab
title: Example Response
-->
```json
{
   "data":[
      {
         "type":"socialProfiles",
         "id":"FBP-1234567890ABCEDF",
         "attributes":{
            "name":"John's Bakery",
            "avatarUrl":"https://graph.facebook.com/1111111/picture",
            "socialNetwork":"facebook",
            "status":"active"
         },
         "relationships":{
            "businessLocation":{
               "links":{
                  "related":"https://prod.apigateway.co/products/social/socialProfiles/FBP-1234567890ABCEDF/businessLocation",
                  "self":"https://prod.apigateway.co/products/social/socialProfiles/FBP-1234567890ABCEDF/relationships/businessLocation"
               },
               "data":{
                  "id":"AG-ABCD1234G",
                  "type":"businessLocations"
               }
            }
         }
      },
      {
         "attributes":{
            "name":"John Smith",
            "avatarUrl":"https://graph.facebook.com/2222222222/picture",
            "socialNetwork":"facebook",
            "status":"active"
         },
         "relationships":{
            "businessLocation":{
               "links":{
                  "related":"https://prod.apigateway.co/products/social/socialProfiles/FBU-987654321ABCDEF0/businessLocation",
                  "self":"https://prod.apigateway.co/products/social/socialProfiles/FBU-987654321ABCDEF0/relationships/businessLocation"
               },
               "data":{
                  "type":"businessLocations",
                  "id":"AG-ABCD1234G"
               }
            }
         },
         "type":"socialProfiles",
         "id":"FBU-987654321ABCDEF0"
      },
      {
         "attributes":{
            "avatarUrl":"https://pbs.twimg.com/profile_images/11111111111/kvdhcfUs_normal.png",
            "name":"CatPics101",
            "socialNetwork":"twitter",
            "status":"active"
         },
         "id":"TWU-11AA22BB33CC",
         "relationships":{
            "businessLocation":{
               "data":{
                  "id":"AG-ABCD1234G",
                  "type":"businessLocations"
               },
               "links":{
                  "related":"https://prod.apigateway.co/products/social/socialProfiles/TWU-11AA22BB33CC/businessLocation",
                  "self":"https://prod.apigateway.co/products/social/socialProfiles/TWU-11AA22BB33CC/relationships/businessLocation"
               }
            }
         },
         "type":"socialProfiles"
      }
   ],
   "links":{
      "first":"https://prod.apigateway.co/products/social/socialProfiles?filter[businessLocation.id]=AG-F7GHCJ4QCX&page[cursor]=eyJidXNpbmVzc0xvY2F0aW9uIjoiQUctRjdHSENKNFFDWCIsIm9mZnNldCI6MH0=&page[limit]=3",
      "next":"https://prod.apigateway.co/products/social/socialProfiles?filter[businessLocation.id]=AG-F7GHCJ4QCX&page[cursor]=eyJidXNpbmVzc0xvY2F0aW9uIjoiQUctRjdHSENKNFFDWCIsIm9mZnNldCI6MX0=&page[limit]=3"
   },
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