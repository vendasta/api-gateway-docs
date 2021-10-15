---
tags: [01, overview, getting-started]
---

# Overview

Welcome to the API Gateway. This service has been designed to allow all our partners access to build on top of the platform using a simple set of REST APIs. 


## Broken into contexts
Our platform serves many different types of users, each with their own frame of reference. If you ask a sales person to describe the properties of a business vs asking someone in accounting you will get two different answers. This is because each have their own context. 

To aid you in presenting the correct version of similarly named fields we have grouped endpoints by the context in which they should be used. 


## Stable
The platform is rapidly changing but you should not have to update your integration every few months. Our endpoints are designed to continually evolve instead of making you upgrade versions. Each operation, parameter and model field will have a status assigned to it. We aim to provide 2 years of notice before removal so you have time to migrate.

[More on Versioning](Versioning.md)


## Built on standards
We can all be more efficient when we follow the same way of doing things. When facing a design decision we have opted to follow existing standards instead of defining yet another way.

Most notably we use [JSON:API](https://jsonapi.org/examples/) as the default [format for requests](RequestFormat.md).

If you have ideas on how we can support more standards please let us know. We want developers to be able to reuse their favorite tools.


## Self documenting
In the body of responses you will find links to related actions and helpful details on errors. This is the HATEOAS part of REST that is so often forgotten.


## Conventions

### Authentication
Every request requires an OAuth2 bearer authorization header that was issued by the API Gateway. [Learn to create them](../Authorization/Authorization.md) 

### Localization
Translating the content in the platform is crowed sourced and will improve over time. You are encouraged to provide a list of languages that the user can read using the [Accept-Language](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language) request header. Web browsers will often set this for you by default. If not set `en-US` will be used. 

We will pick the most appropriate locale based on the header and available translations. Resource attributes representing an enum will have both an untranslated `{attribute}Code` and a translated `{attribute}Name` option. Look for the `x-translateable` flag in the documentation. Translatable fields are readonly over API.



### Errors
They happen. It is a fact of life but we don't think they should be hard to solve. 

When an error occurs a 4XX or 5xx HTTP status code will be on the response. The body of the response will also contain a list of more specific error messages.

The `code` property will tell you the specific platform error that occurred. You can find a complete list of possible errors for the operation in the `x-errors` attribute of the documentation.

```json 
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/vnd.api+json

{
  "errors": [
    {
      "status": "422",
      "code": "MalformedRequestBody",
      "title":  "Malformed request body",
      "detail": "Could not deserialize terms from body: unexpected comma at line 20",
      "meta": {
        "resourceType": "terms",
        "paramValue": "unexpected comma at line 20"
      },
      "links": {
        "about": {
          "href": "https://prod.apigateway.co/docs/errorTypes/MalformedRequestBody"
        },
        "docs": {
          "href": "https://prod.apigateway.co/docs/#operation/get-terms-type"
        }
      }
    },
    {
      "status": "422",
      "code": "QueryParameterBadValue",
      "title": "Query parameter value not allowed",
      "source": { "parameter": "page[size]" },
      "detail": "The query parameter 'page[size]' does not support the value 'lastName' for this request.",
      "meta": {
        "param": "page[size]",
        "paramValue": "lastName"
      },
      "links": {
        "about": {
          "href": "https://prod.apigateway.co/docs/errorTypes/QueryParameterBadValue"
        },
        "docs": {
          "href": "https://prod.apigateway.co/docs/#operation/get-terms-type"
        }
      }
    }
  ]
}
```



### Filters
Most operations that return on a list of data allow you to filter the list using query params in the form of `&filter[fieledName1]=value1&filter[sub.fieledName3]=value2`.

### Paging
All operations that return a list of data will let you specify a query param of `page[limit]` to indicate the max number of records you would like returned in a single batch. The body of the response will provide links that you can use to get the next batch without needing to send the filters again. The link will be omitted if not available. 

```json
{
  "links": {
    "first": "https://prod.apigateway.co/crm/customers?page[cursor]=abc",
    "prev": "https://prod.apigateway.co/crm/customers?page[cursor]=hij",
    "self": "https://prod.apigateway.co/crm/customers?page[cursor]=klm",
    "next": "https://prod.apigateway.co/crm/customers?page[cursor]=nop",
    "last": "https://prod.apigateway.co/crm/customers?page[cursor]=xyz"
  }
}
```

### Dates & times
Dates are formatted according to [RFC-3339](https://tools.ietf.org/html/rfc3339) which is an extension of [ISO 8601](https://www.w3.org/TR/NOTE-datetime).


Type | Form | Example
---------|----------|---------
 Date, hour, minute and second in UTC | YYYY-MM-DDTHH:MM:SSTZD | 2020-10-28T10:37:23Z
 Date, hour and minute in Saskatoon (UTC-6) | YYYY-MM-DDTHH:MMTZD | 2020-10-28T4:37-6:00
 Date | YYYY-MM-DD | 2001-12-25


### Naming
All time fields end with `At` to help you identify them while reading your code

More info comming soon