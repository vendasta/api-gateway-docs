---
stoplight-id: 1vx6kpuftqy26
---

# Limitation of SCIM in Vendasta 

The SCIM implementation in Vendasta has few limitations which align with our business needs. In this article we will discuss these limitations in more detail.

## Namespace scoping
Every SCIM path is scoped to a `{namespace}` — your partner id. You can only manage users who belong to that namespace: a user belongs to it when they hold at least one role there, or when it is their **home namespace**, the partner they were originally created under.

- `GET`, `PUT`, `PATCH` and `DELETE` on `/{namespace}/Users/{id}` return `404` for a user who is not in your namespace, even when that user exists elsewhere on the Vendasta platform. A `GET /{namespace}/Users` lookup by `userName` or `externalId` returns an empty list for the same user.
- A user's `groups` lists only the roles they hold in your namespace. Roles the same user holds under another partner are never returned.
- `active` reports whether the user is in your namespace. It is not a global enabled/disabled flag and cannot be set through SCIM.
- A `DELETE` outside a user's home namespace removes only that namespace's roles. Only a delete in their home namespace removes the Vendasta account.

## Users resources
Email address is used as an `userName`. Anything other that email, system will reject as a invalid `userName`.

We do not support multiple `emails`. An user can have only one email associated. If multiple `emails` are provided on request we will only pick up for which `type eq work`.

Similarly for `addresses`, an user can have only one address. If multiple `addresses` are provided in request we will only pick up for which `type eq work`.

The accepted `addresses[].country` is ISO 3166-1 alpha-2. Example: `CA`

The accepted `addresses[].region` comprises of ISO 3166-1 alpha-2 of both country code and state code. Example: `CA-SK`

The `phoneNumbers[].value` should match the region/country given in address. Example: `+1-306-555-1234`


`Replace User` (PUT) is a full replace of the **profile** only: a profile attribute your request omits is cleared. Roles and group membership are preserved, and `userName` / `emails` cannot be changed. `externalId` is also taken from the body alone, so a PUT that omits it clears the stored mapping — always send it.

Here is a list of supported/not-supported operations under Users resources
Operation | Supported 
---------|----------
 Search Users | Yes 
 Create User | Yes  
 Get User | Yes 
 Replace User | Yes 
 Update User | Yes 
 Delete User | Yes  

> Filter for searching users are limited to `userName` and `externalId`. However if no filter is specified it will list down all users.

## Groups resources
Groups in Vendasta are predefined according to business needs. Groups can't be created or deleted. Also Groups name can not be updated through SCIM. In terms of Group update members can be added to a Group or deleted from a Group.

We do support `id` as a unique identifier of a group and `displayName` for human readable names.

Group reads never include membership: `members` is not returned by `Search Groups` or `Get Groups`. Read a user to see the groups they belong to.

For more information on Groups in Vendasta please see the [SCIM Groups and their assignment to users](SCIM-Groups-and-their-assignment-to-users.md) section.

Here is a list of supported/not-supported operations under Groups resources
Operation | Supported 
---------|----------
 Search Groups | Yes 
 Create Groups | No 
 Get Groups | Yes 
 Replace Groups | No 
 Update Group | Yes 
 Delete Group | No 


 > Search criteria is limited to `displayName eq "..."` and `type eq "platformFeature"`.

 > Update of groups are limited to adding or removing members through patch requests.

