---
stoplight-id: 1ljuzmi2uboim
---

# Addresses

Vendasta maintains a list of countries and subregions within them that is based on an early version of the [ISO 3166 standard](https://en.wikipedia.org/wiki/ISO_3166). 

When building a UI in your system you may use the following API calls to generate dropdown lists for the `countryCode` and `regionCode` fields on various resources.

## List Countries

<!--
type: tab
title: Request
-->

``` json http
{
  "method": "GET",
  "url": "https://prod.apigateway.co/platform/countries",
  "query": {
    "page[size]": 10
  },
  "headers": {
    "Authorization": "Bearer <Token with 'business' scope>",
    "Content-Type": "application/vnd.api+json",
    "Accept-Language": "en-CA"
  }
}
```

<!--
type: tab
title: Response
-->

```json
{
  "links": {
    "last": "https://prod.apigateway.co/platform/countries?page[cursor]=eyJsYXN0UGFnZVN0YXJ0IjowLCJsYXN0UGFnZUVuZCI6MTAsImZvclBhZ2VUeXBlIjoibGFzdCIsImxpbWl0IjoxMH0=&page[limit]=10",
    "next": "https://prod.apigateway.co/platform/countries?page[cursor]=eyJsYXN0UGFnZVN0YXJ0IjowLCJsYXN0UGFnZUVuZCI6MTAsImZvclBhZ2VUeXBlIjoibmV4dCIsImxpbWl0IjoxMH0=&page[limit]=10"
  },
  "data": [
    {
      "type": "countries",
      "id": "AD",
      "attributes": {
        "name": "Andorra"
      }
    },
    {
      "type": "countries",
      "id": "AE",
      "attributes": {
        "name": "United Arab Emirates"
      }
    },
    {
      "type": "countries",
      "id": "AF",
      "attributes": {
        "name": "Afghanistan"
      }
    },
    {
      "type": "countries",
      "id": "AG",
      "attributes": {
        "name": "Antigua & Barbuda"
      }
    },
    {
      "type": "countries",
      "id": "AI",
      "attributes": {
        "name": "Anguilla"
      }
    },
    {
      "type": "countries",
      "id": "AL",
      "attributes": {
        "name": "Albania"
      }
    },
    {
      "type": "countries",
      "id": "AM",
      "attributes": {
        "name": "Armenia"
      }
    },
    {
      "type": "countries",
      "id": "AN",
      "attributes": {
        "name": "Netherlands Antilles"
      }
    },
    {
      "type": "countries",
      "id": "AO",
      "attributes": {
        "name": "Angola"
      }
    },
    {
      "type": "countries",
      "id": "AR",
      "attributes": {
        "name": "Argentina"
      }
    }
  ]
}
```
<!--
type: tab-end
-->

## List Regions in country

Once a user has selected a country you can request a list of regions within that country. 
This example gets the list of regions in Canada (CA).

<!--
type: tab
title: Request
-->

``` json http
{
  "method": "GET",
  "url": "https://prod.apigateway.co/platform/countryRegions",
  "query": {
    "page[size]": 20
  },
  "headers": {
    "Authorization": "Bearer <Token with 'business' scope>",
    "Content-Type": "application/vnd.api+json",
    "Accept-Language": "en-CA"
  }
}
```

<!--
type: tab
title: Response
-->

```json
{
  "data": [
    {
      "type": "countryRegions",
      "id": "CA-AB",
      "attributes": {
        "name": "Alberta"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-BC",
      "attributes": {
        "name": "British Columbia"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-MB",
      "attributes": {
        "name": "Manitoba"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-NB",
      "attributes": {
        "name": "New Brunswick"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-NL",
      "attributes": {
        "name": "Newfoundland and Labrador"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-NS",
      "attributes": {
        "name": "Nova Scotia"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-NT",
      "attributes": {
        "name": "Northwest Territories"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-NU",
      "attributes": {
        "name": "Nunavut"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-ON",
      "attributes": {
        "name": "Ontario"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-PE",
      "attributes": {
        "name": "Prince Edward Island"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-QC",
      "attributes": {
        "name": "Quebec"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-SK",
      "attributes": {
        "name": "Saskatchewan"
      }
    },
    {
      "type": "countryRegions",
      "id": "CA-YT",
      "attributes": {
        "name": "Yukon"
      }
    }
  ]
}
```
<!--
type: tab-end
-->
