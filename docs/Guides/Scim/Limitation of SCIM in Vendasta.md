---
stoplight-id: 1vx6kpuftqy26
---

# Limitation of SCIM in Vendasta 

The SCIM implementation in Vendasta has few limitations which align with our business functionality. Here in this article we will discuss these limitations in more detail.

## Users resources

We do not support multiple `emails`. An user can have only one email associated. If multiple `emails` are provided on request we will only pick up for which `type eq work`.

Similarly for `addresses`, an user can have only one address. If multiple `addresses` are provided in request we will only pick up for which `type eq work`.

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


 > Search criteria is limited to `displayName` only.

 > Update of groups are limited to adding or removing members through patch requests.

