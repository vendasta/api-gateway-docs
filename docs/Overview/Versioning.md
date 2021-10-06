---
tags: [03, overview, getting-started, versioning]
---

# Versioning

No one likes having to update their entire system from one version to the next. That is why we designed our API to continuously evolve. 

Each operation, parameter and attribute has its own lifecycle defined in the documentation as an `x-lifecycle` property. For undocumented parameters and attributes you can assume the status of the operation. 

## Lifecycle
Items will progress through the following maturity levels:

**Proposed** - We are considering building this item and would like your feedback. It can only be used with the mock server.

**Trusted Tester** - The item is newly built and is undergoing rapid iteration based on feedback from a select set of partners. The item will transition to Early Access after receiving feedback from a sufficient number of partners.  Breaking changes may occur and will be communicated directly to the testing partners.

**Early Access** - The item is considered complete and is now in a stabilization period. This is the public beta. We will aim to provide 30 days notice for any breaking changes. The item will transition to General Availability after 90 days of stability.

**General Availability** - The item has proven stability under load and no development work is currently being done. New fields, query parameters, or enum values may eventually be added in a backward-compatible way.

**Deprecated** - You can still use this item however should consider using something else. Where possible for items that have reached GA we will continue to support the item for 2 years between announcing the deprecation and removing it. This time may be shortened for unused items.

**Removed** - The item has been sunset and can no longer be used

To aid in your planning the `deprecated` date, `proposedRemoval` date and migration `description` properties will be added within the OpenAPI specification files under the `x-lifecycle` property.

## Other changes
We may add new optional fields or valid values at any time. These will be documented in the [changelog](Changelog.md)

All properties should have clear validation rules in the documentation including data type, min/max length or regular expression. If you find one that is unclear please report this bug in our documentation. Once documented we may loosen the restriction but will not tighten them without warning.

## Warning system
It is strongly recommended that you tell us which fields you are using by including the `fields` query parameter on every request that returns data. That will allow us to warn you if you are using fields that have been deprecated on a resource. 
