---
tags: [overview, api-architecture]
---

# API Architecture

Vendasta exposes two layers of APIs. Understanding the difference will help you pick the right one for your use case.

## Gateway APIs (JSON:API)

These are the **hand-curated REST APIs** documented under the Platform, CRM, and Product sections of this site. They are the recommended starting point for most integrations.

**Characteristics:**
- Follow the [JSON:API](https://jsonapi.org/examples/) standard (`application/vnd.api+json`)
- Stable versioning with [lifecycle status tracking](Versioning.md) (`x-lifecycle` annotations)
- Consistent pagination, filtering, and error formats across all endpoints
- Rich documentation with examples and guides

**Sections:**
- **[Platform APIs](../../openapi/platform/platform.yaml)** — Core operations: accounts, orders, users, subscriptions, business categories, automations
- **[CRM APIs](../../openapi/crm/crm.json)** — Contacts, companies, activities, custom objects
- **Product APIs** — [Advertising Intelligence](../../openapi/advertising/advertising.yaml), [Customer Voice](../../openapi/customervoice/customervoice.yaml), [Local SEO](../../openapi/listings/listings.yaml), [Reputation AI](../../openapi/reputation/reputation.yaml), [Social Marketing](../../openapi/social/social.yaml)
- **[SCIM APIs](../../openapi/scim/scim.yaml)** — User provisioning via the SCIM 2.0 standard

## Service APIs

These are **additional APIs** auto-generated from Vendasta's internal gRPC services, providing access to capabilities not yet covered by the gateway APIs.

**Characteristics:**
- Use standard `application/json` format
- Endpoints follow `/v1/` or `/v2/` path patterns
- Schema types reflect their protobuf origins (e.g., `protobufAny`, `rpcStatus`)
- May evolve more rapidly than gateway APIs

**Available services include:**
- **Social Posts** / **Social Drafts** — Create, manage, and schedule social media content
- **Reputation** — Extended review and NPS management beyond the gateway Reputation AI APIs
- **CRM** — Extended CRM operations beyond the gateway CRM APIs
- **Forms** — Form builder and submission management
- **Meetings** — Meeting scheduling and contact management
- **Conversation** — Messaging and inbox management
- **Sales Orders** — Order processing and management
- **Listing Products** — Listing distribution product management
- **Multi Location Analytics** — Cross-location reporting
- **Vanalytics** — Call tracking and conversation analytics
- **Composer** — Content composition
- **Website** — WordPress hosting, site monitoring, and management (Website Pro)

## Which Should I Use?

| Scenario | Recommended |
|---|---|
| New integration, standard CRUD operations | Gateway APIs |
| Account management, orders, user provisioning | Gateway APIs (Platform) |
| CRM contacts, companies, activities | Gateway APIs (CRM) |
| Social post scheduling and management | Service APIs (Social Posts) |
| Features not available in gateway APIs | Service APIs |
| Building on a standard like SCIM or JSON:API | Gateway APIs |

When both layers cover similar functionality (e.g., Reputation, CRM), the gateway APIs provide a more stable, better-documented interface. The service APIs offer broader coverage when you need capabilities the gateway doesn't expose yet.

## Authentication

Both API layers use the same OAuth2 authentication. An access token obtained via [2-legged OAuth](../Authorization/2-legged-oauth/Overview.md) works with both gateway and service APIs. See the [Authorization guide](../Authorization/Authorization.md) for details.

## Environments

| Environment | Gateway Base URL | Service API Base URL |
|---|---|---|
| Production | `https://prod.apigateway.co` | `https://prod.apigateway.co` |
| Demo/Sandbox | `https://demo.apigateway.co` | `https://demo.apigateway.co` |
