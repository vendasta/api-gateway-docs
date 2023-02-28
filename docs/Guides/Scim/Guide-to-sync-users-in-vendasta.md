# Guide to use scim APIs 


## Overview
System for Cross-domain Identity Management (SCIM) focuses on syncing user accounts and permissions between systems but has an extension system that allows syncing any type of record. There are several well known endpoint paths for discovering what the server supports and resource schema definitions.


## Step 1 : Pre-requisites for accessing the Vendasta's Scim APIs

### 1. Namespace

This must be the id of the partner that wish to manage users in sync with Vendasta. It should be unique for a partner.

### 2. Authorisation token
- For a partner with Vendasta, in order to access APIs of Vendasta they must need a authorisation token which is generated against the namespace of the partner. This should be refreshed over a particular time.

- The authorisation token must be generated with required scopes in order to access a particular platform.

### 3. cope
- It is used to specify the access rights the partner should have towards Vendasta’s Scim APIs.

- user.admin: Read-write access to manage all users

> To create a service account and create a token , see [here](../../Authorization/2-legged-oauth/Overview.md).



## Step 2 : Vendasta Scim Endpoints to sync users

Endpoint Name | Method | URL | Description 
---------|----------|---------|---------------
 Create User | POST | https://prod.apigateway.co/scim/{namespace}/Users | Create User based on SCIM schema | [Create API]((../../openapi/scim/scim.yaml))
 Get User | GET | https://prod.apigateway.co/scim/{namespace}/Users/{id} | Get an user when the user id is known| 
 Replace User | PUT | https://prod.apigateway.co/scim/{namespace}/Users/{id} | API to replace an user. All of the attributes will be replaced with provided value and attributes will be kept black for which no value is specified.| 
 Update User | PATCH | https://prod.apigateway.co/scim/{namespace}/Users/{id} | Update an user's one or multiple attributes by providing operation path and value. Post update all other attributes will be unchanged apart from provided attributes.|
 Search Users| GET | https://prod.apigateway.co/scim/{namespace}/Users | This endpoint can be used to search users based on filter or list all user if filter criteria is ignored |
 Delete User | DELETE | https://prod.apigateway.co/scim/{namespace}/Users/{id} | API to delete an user.|



## External ID
It is an external unique identifier of the user. It is an option field it can be generated if a partner wish to have an external unique identifier.