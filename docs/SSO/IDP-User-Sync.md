---
stoplight-id: 36hexofe8f7lb
---

# User Syncronization for partners

> This guide is for partners that maintain a database of user records outside of Vendasta.
>
> It is most commonly used alongside a custom [Identity Provider](Identity-Provider-SSO.md) to provide seemless SSO.

You will want to implement a way to detect these 4 events occuring in your system
- A new user is created
- One of the user profile fields that is sent to Vendasta is modified
- Data that affects the access a user should have within Vendasta is modified
- A user is deleted

That may be a database trigger or adding function calls within your current processing.

When any of the events occur you will want to do the following:

1. Check for existing user in Vendasta by email
2. If not found and they should have access then create a new user in Vendasta
3. else compair the user records and send updates as needed

After creating or finding a user in Vendasta you may want to store the Vendasta user id in your system so that you have easy access to it instead of doing a lookup by email.

## Check for existing user

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
      "id": "U-a1654fed-78d3-4bd4-bf39-80203ca5273a",
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
            "id": "U-a1654fed-78d3-4bd4-bf39-80203ca5273a"
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


## Create new user

Business User, My Team

## Update user profile fields

## Update My Team permissions

## Update Bussiness Location permissions

## Remove a user


## Frequently Asked Questions

### Why don't we automatically create users when they SSO?
Many systems will create users Just In Time (JIT) when the user signs on from another system. Using the info provided by OAuth2/OpenID Connect's userinfo endpoint or SAML's assertion values is a handy way to skip having the user fill out a sign up form. 

There are however many drawbacks:
- Updates to the user profile, such as changing their name or adding permissions, are not applied until the next time that the user logs in. Users often report this as a bug or get frusterated that they have to log out and back in.
- If a permission is removed or user account locked within your identity provider the user will continue to be able to use the Vendasta platform until their session expires. (Up to 30 days.)
- It requires extra prossessing which can make signing in a slow experiance for your users.
- It requires customization in your identity provider to map user identities to the accounts and platform features they should have access to.
- New employees or customers in your system are not added to Vendasta until the first time they log into Vendasta. As a result they can't be linked to teams, tasks or other features ahead of time.
- Old users remain in the Vendasta platform until manually cleaned up