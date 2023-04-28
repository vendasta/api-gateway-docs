---
stoplight-id: mlniubd7ehiyf
---

# User Synchronization for partners

> This guide is for partners that maintain a database of user records outside of Vendasta.
>
> It is most commonly used alongside a custom [Identity Provider](Identity-Provider-SSO.md) to provide seamless SSO.

You will want to implement a way to detect these 4 events occuring in your system.
- A new user is created
- One of the user profile fields that are sent to Vendasta is modified
- Data that affects the access a user should have within Vendasta is modified
- A user is deleted

That may be a database trigger or adding function calls within your current processing.

When any of the events occur you will want to do the following:

1. Check for existing user in Vendasta by email
2. If not found and they should have access then create a new user in Vendasta
3. else compare the user records and send updates as needed

After creating or finding a user in Vendasta you may want to store the Vendasta user id in your system so that you can skip the lookup by email for future updates.

## Check for existing user

Email addresses can only be linked to a single user within a partner. 

<!--
type: tab
title: Request
-->
``` json http
{
  "method": "GET",
  "url": "https://prod.apigateway.co/platform/users",
  "query": {
    "filter[partner.id]": "Your partner id",
    "filter[email]": "user@example.com"
  },
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```
<!--
type: tab
title: User Found Response
-->
If the contact information you provide is valid, it will create a new customer record in Vendasta's system and return the newly created record.
```json
200 OK
{
  "links": {
    "first": "https://prod.apigateway.co/platform/users?filter[email]=chris@example.com&filter[partner.id]=9YW9&page[cursor]=eyJ1c2VySWRzIjpudWxsLCJwaWQiOiI5WVc5Iiwicm9sZUlkcyI6WyJzbWIiLCJwYXJ0bmVyIiwic2FsZXNfcGVyc29uIiwiZGlnaXRhbF9hZ2VudCIsInBhcnRuZXJfc2VydmljZV9hY2NvdW50Il0sImVtYWlsIjoiY2hyaXNAbW9uanVyZWFsYW1vdXRsb29rLm9ubWljcm9zb2Z0LmNvbSIsInNvcnQiOlt7IkRpcmVjdGlvbiI6MSwiRmllbGQiOjB9XSwibGltaXQiOjEwfQ==&page[limit]=10"
  },
  "data": [
    {
      "type": "users",
      "id": "U-a1654fed-78d3-4 bd 4-bf39-80203ca5273a",
      "attributes": {
        "givenName": "Chris",
        "familyName": "Green",
        "displayName": "Chris Green",
        "greetingName": "",
        "email": "chris@example.com",
        "emailVerified": false,
        "languageLocaleCode": "",
        "phoneNumberSet": false,
        "phoneNumbers": [],
        "address": {
          "streetAddress": "",
          "additionalAddress": "",
          "line1": "",
          "line2": "",
          "city": "",
          "postalCode": "",
          "countryCode": "",
          "regionCode": ""
        },
        "profileImage": "",
        "timeZone": "",
        "createdAt": "2023-04-03T11:42:10.06419601Z",
        "updatedAt": "2023-04-03T11:42:20.163366517Z",
        "isPartnerUser": false,
        "isBusinessUser": true,
        "isBotUser": false,
        "isGuestUser": false,
        "isSelf": false
      },
      "relationships": {
        "businessLocations": {
          "links": {
            "related": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/businessLocations",
            "self": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/relationships/businessLocations"
          },
          "data": [
            {
              "type": "businessLocations",
              "id": "AG-G5HFCGJWDK"
            }
          ]
        },
        "customFields": {
          "links": {
            "related": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/customFields",
            "self": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/relationships/customFields"
          },
          "data": {
            "type": "userCustomFields",
            "id": "U-a1654fed-78d3-4 bd 4-bf39-80203ca5273a"
          }
        },
        "partner": {
          "links": {
            "related": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/partner",
            "self": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/relationships/partner"
          },
          "data": {
            "type": "partners",
            "id": "9YW9"
          }
        },
        "platformAccess": {
          "links": {
            "related": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/platformAccess",
            "self": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/relationships/platformAccess"
          }
        }
      }
    }
  ]
}
```
<!--
type: tab
title: User Not Found Response
-->
If there is no user within your partner instance with the specified email the response will contain an empty `data` member.

```json
200 OK
{
  "links": {
    "first": "https://prod.apigateway.co/platform/users?filter[email]=user@example.com&filter[partner.id]=ABC&page[cursor]=eyJ1c2VySWRzIjpudWxsLCJwaWQiOiI5WVc5Iiwicm9sZUlkcyI6WyJzbWIiLCJwYXJ0bmVyIiwic2FsZXNfcGVyc29uIiwiZGlnaXRhbF9hZ2VudCIsInBhcnRuZXJfc2VydmljZV9hY2NvdW50Il0sImVtYWlsIjoicnJhbmNlQHZlbmRhc3RhLmNvbSIsInNvcnQiOlt7IkRpcmVjdGlvbiI6MSwiRmllbGQiOjB9XSwibGltaXQiOjEwfQ==&page[limit]=10"
  },
  "data": []
}
```
<!--
type: tab-end
-->

## Types of users

There are two categories of users that you may want to keep in sync between your system and Vendasta.

- Customers
  - These can be found in [partner center -> businesses -> users](https://partners.vendasta.com/bc-admin)
  - They have access to business locations (your sales accounts)
  - When managing their permissions by API you will be using the `data.relationships.businessLocations` list
- Your Team
  - They are generally your employees. 
  - They have access to platform features such as Partner Center, Sales & Success Center, Task Manager.
  - These can be found in [partner center -> administration -> my team](https://partners.vendasta.com/my-team)
  - When managing their permissions by API you will be using the `data.relationships.platformAccess` list



## Create new user

Here are common requests that you may make. For full details see the [API operation docs](../openapi/platform/platform.yaml/paths/~1users/post).

For each of the examples you will need to replace the partner id with your own.

In the profile attributes only the email address is required.

<!--
type: tab
title: Customer User
-->
You will need to create one or more sales accounts/business locations first. They should be added to the list at `data.relationships.businessLocations`

``` json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/users",
  "body": {
  "data": {
    "type": "users",
    "attributes": {
      "givenName": "William",
      "familyName": "Smith",
      "greetingName": "Billy",
      "email": "bill@example.com",
      "languageLocaleCode": "en-US",
      "phoneNumbers": [
        {
          "number": "+1-306-555-1234 ext. 56",
          "typeCode": "mobile"
        }
      ],
      "address": {
        "line1": "109 8th Street E.",
        "line2": "Suite 23",
        "streetAddress": "109 8th Street E.",
        "additionalAddress": "Suite 23",
        "city": "Saskatoon",
        "postalCode": "S7M 1R3",
        "regionCode": "CA-SK",
        "countryCode": "CA"
      },
      "timeZone": "America/Regina"
    },
    "relationships": {
      "partner": {
        "data": {
          "type": "partners",
          "id": "ABC"
        }
      },
      "businessLocations": {
        "data": [
          {
            "type": "businessLocations",
            "id": "AG-1234567"
          },
          {
            "type": "businessLocations",
            "id": "AG-4567890"
          }
        ]
      }
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab
title: Admin
-->

Users with just the `pc:access` feature access will have access to all features. You can reduce their access by listing specific sub-features.

``` json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/users",
  "body": {
  "data": {
    "type": "users",
    "attributes": {
      "givenName": "William",
      "familyName": "Smith",
      "greetingName": "Billy",
      "email": "bill@example.com",
      "languageLocaleCode": "en-US",
      "phoneNumbers": [
        {
          "number": "+1-306-555-1234 ext. 56",
          "typeCode": "mobile"
        }
      ],
      "address": {
        "line1": "109 8th Street E.",
        "line2": "Suite 23",
        "streetAddress": "109 8th Street E.",
        "additionalAddress": "Suite 23",
        "city": "Saskatoon",
        "postalCode": "S7M 1R3",
        "regionCode": "CA-SK",
        "countryCode": "CA"
      },
      "timeZone": "America/Regina"
    },
    "relationships": {
      "partner": {
        "data": {
          "type": "partners",
          "id": "ABC"
        }
      },
      "platformAccess": {
        "data": [
          {
            "type": "appFeatures",
            "id": "pc:access"
          }
        ]
      }
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab
title: Salesperson
-->

``` json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/users",
  "body": {
  "data": {
    "type": "users",
    "attributes": {
      "givenName": "William",
      "familyName": "Smith",
      "greetingName": "Billy",
      "email": "bill@example.com",
      "languageLocaleCode": "en-US",
      "phoneNumbers": [
        {
          "number": "+1-306-555-1234 ext. 56",
          "typeCode": "mobile"
        }
      ],
      "address": {
        "line1": "109 8th Street E.",
        "line2": "Suite 23",
        "streetAddress": "109 8th Street E.",
        "additionalAddress": "Suite 23",
        "city": "Saskatoon",
        "postalCode": "S7M 1R3",
        "regionCode": "CA-SK",
        "countryCode": "CA"
      },
      "timeZone": "America/Regina"
    },
    "relationships": {
      "partner": {
        "data": {
          "type": "partners",
          "id": "ABC"
        }
      },
      "platformAccess": {
        "data": [
          {
            "type": "appFeatures",
            "id": "ssc:access"
          }
        ]
      }
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab
title: Sales Manager
-->

To turn a salesperson into a manager simply add `ssc:manage` to the list of platform access.

``` json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/users",
  "body": {
  "data": {
    "type": "users",
    "attributes": {
      "givenName": "William",
      "familyName": "Smith",
      "greetingName": "Billy",
      "email": "bill@example.com",
      "languageLocaleCode": "en-US",
      "phoneNumbers": [
        {
          "number": "+1-306-555-1234 ext. 56",
          "typeCode": "mobile"
        }
      ],
      "address": {
        "line1": "109 8th Street E.",
        "line2": "Suite 23",
        "streetAddress": "109 8th Street E.",
        "additionalAddress": "Suite 23",
        "city": "Saskatoon",
        "postalCode": "S7M 1R3",
        "regionCode": "CA-SK",
        "countryCode": "CA"
      },
      "timeZone": "America/Regina"
    },
    "relationships": {
      "partner": {
        "data": {
          "type": "partners",
          "id": "ABC"
        }
      },
      "platformAccess": {
        "data": [
          {
            "type": "appFeatures",
            "id": "ssc:access"
          },
          {
            "type": "appFeatures",
            "id": "ssc:manage"
          }
        ]
      }
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab
title: Digital Agent
-->

``` json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/users",
  "body": {
  "data": {
    "type": "users",
    "attributes": {
      "givenName": "William",
      "familyName": "Smith",
      "greetingName": "Billy",
      "email": "bill@example.com",
      "languageLocaleCode": "en-US",
      "phoneNumbers": [
        {
          "number": "+1-306-555-1234 ext. 56",
          "typeCode": "mobile"
        }
      ],
      "address": {
        "line1": "109 8th Street E.",
        "line2": "Suite 23",
        "streetAddress": "109 8th Street E.",
        "additionalAddress": "Suite 23",
        "city": "Saskatoon",
        "postalCode": "S7M 1R3",
        "regionCode": "CA-SK",
        "countryCode": "CA"
      },
      "timeZone": "America/Regina"
    },
    "relationships": {
      "partner": {
        "data": {
          "type": "partners",
          "id": "ABC"
        }
      },
      "platformAccess": {
        "data": [
          {
            "type": "appFeatures",
            "id": "tm:access"
          }
        ]
      }
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab-end
-->

Users may have multiple roles simply by combining the examples.


## Update user profile fields

When updating a user's profile attributes you only need to send the fields that changed. Fields that are omitted from the request will keep their original values.

You will need to set the user id in both the URL path and body for all examples.

<!--
type: tab
title: Update first name
-->


``` json http
{
  "method": "PATCH",
  "url": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
  "body": {
  "data": {
    "type": "users",
    "id": "U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
    "attributes": {
      "givenName": "William"
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab-end
-->


## Update My Team permissions

<!--
type: tab
title: Replace permissions
-->
Sending a PATCH request that includes `data.relationships.platformAccess` will replace all platform access.

``` json http
{
  "method": "PATCH",
  "url": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
  "body": {
  "data": {
    "type": "users",
    "id": "U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
    "relationships": {
      "platformAccess": {
        "data": [
          {
            "type": "appFeatures",
            "id": "tm:access"
          }
        ]
      }
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab-end
-->

## Update Business Location permissions

<!--
type: tab
title: Replace permissions
-->
Sending a PATCH request that includes `data.relationships.businessLocations` will replace all location access.

``` json http
{
  "method": "PATCH",
  "url": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
  "body": {
  "data": {
    "type": "users",
    "id": "U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
    "relationships": {
      "businessLocations": {
        "data": [
          {
            "type": "businessLocations",
            "id": "AG-1234567"
          }
        ]
      }
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab
title: Add location
-->
You can add locations in addition to what the user already has with a POST request

``` json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/relationships/businessLocations",
  "body": {
  "data": [
    {
      "id": "AG-1234567",
      "type": "businessLocations"
    }
  ]
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab
title: Remove location
-->
You can remove locations from what the user currently has with a DELETE request. If the user does not currently have access to the specified location the request will be ignored.

``` json http
{
  "method": "DELETE",
  "url": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a/relationships/businessLocations",
  "body": {
  "data": [
    {
      "id": "AG-1234567",
      "type": "businessLocations"
    }
  ]
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab-end
-->

## Remove a user

When a user should no longer have access to the system the best course of action is to reset their business location and platform access to empty lists. 

<!--
type: tab
title: Remove all permissions
-->
``` json http
{
  "method": "PATCH",
  "url": "https://prod.apigateway.co/platform/users/U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
  "body": {
  "data": {
    "type": "users",
    "id": "U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
    "relationships": {
      "businessLocations": {
        "data": []
      },
      "platformAccess": {
        "data": []
      }
    }
  }
},
  "headers": {
    "Authorization": "Bearer <Token with 'user.admin' scope>",
    "Content-Type": "application/vnd.api+json"
  }
}
```

<!--
type: tab-end
-->

## Frequently Asked Questions

### Why don't we automatically create users when they SSO?
Many systems will create users Just In Time (JIT) when the user signs on from another system. Using the info provided by OAuth2/OpenID Connect's userinfo endpoint or SAML's assertion values is a handy way to skip having the user fill out a sign up form. 

There are however many drawbacks:
- Updates to the user profile, such as changing their name or adding permissions, are not applied until the next time that the user logs in. Users often report this as a bug or get frustrated that they have to log out and back in.
- If a permission is removed or user account locked within your identity provider the user will continue to be able to use the Vendasta platform until their session expires. (Up to 30 days.)
- It requires extra processing which can make signing in a slow experience for your users.
- It requires customization in your identity provider to map user identities to the accounts and platform features they should have access to.
- New employees or customers in your system are not added to Vendasta until the first time they log into Vendasta. As a result they can't be linked to teams, tasks or other features ahead of time.
- Old users remain in the Vendasta platform until manually cleaned up