# Guide to use Vendasta SCIM APIs to sync Users


## Overview
System for Cross-domain Identity Management ([SCIM](https://en.wikipedia.org/wiki/System_for_Cross-domain_Identity_Management)) focuses on syncing user accounts and permissions between systems but has an extension system that allows syncing any type of record. There are several well known endpoint paths for discovering what the server supports and resource schema definitions.


## Step 1 : Pre-requisites for accessing the Vendasta's SCIM APIs

### 1. Namespace

This must be the id of the partner that wish to manage users in sync with Vendasta. It should be unique for a partner.

### 2. Authorization token
- For a partner with Vendasta, in order to access APIs of Vendasta they must need a authorisation token which is generated against the namespace of the partner. This should be refreshed over a particular time.

- The authorization token must be generated with required scopes in order to access a particular platform.

### 3. Scope
- It is used to specify the access rights the partner should have towards Vendasta’s SCIM APIs.

- user.admin: Read-write access to manage all users

> To create a service account and create a token , see [here](../../Authorization/2-legged-oauth/Overview.md).



## Step 2 : Vendasta SCIM Endpoints to sync users

Endpoint Name | Method | URL | &nbsp;&nbsp;&nbsp; Description 
---------|----------|---------|---------------
 [Create User](../../../openapi/scim/scim.yaml/paths/~1{namespace}~1Users) | POST | /scim/{namespace}/Users | Create User based on SCIM schema | [Create API]((../../openapi/scim/scim.yaml))
 [Get User](../../../openapi/scim/scim.yaml/paths/~1{namespace}~1Users~1{id}) | GET | /scim/{namespace}/Users/{id} | Get an user when the user id is known| 
 [Replace User](../../../openapi/scim/scim.yaml/paths/~1{namespace}~1Users~1{id}) | PUT | /scim/{namespace}/Users/{id} | API to replace an user. All of the attributes will be replaced with provided value and attributes will be kept black for which no value is specified.| 
 [Update User](../../../openapi/scim/scim.yaml/paths/~1{namespace}~1Users~1{id}) | PATCH | /scim/{namespace}/Users/{id} | Update an user's one or multiple attributes by providing operation path and value. Post update all other attributes will be unchanged apart from provided attributes.|
 [Search Users](../../../openapi/scim/scim.yaml/paths/~1{namespace}~1Users)| GET | /scim/{namespace}/Users | This endpoint can be used to search users based on filter or list all user if filter criteria is ignored |
 [Delete User](../../../openapi/scim/scim.yaml/paths/~1{namespace}~1Users~1{id}) | DELETE | /scim/{namespace}/Users/{id} | API to delete an user.|



## External ID 
It is an external unique identifier of the user. It is an option field it can be generated if a partner wish to have an external unique identifier.