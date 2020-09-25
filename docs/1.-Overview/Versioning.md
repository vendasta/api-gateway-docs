---
tags: [03, overview, getting-started, versioning]
---

# Versioning

No one likes having to update their entire system from one version to the next. That is why we designed our API to continuously evolve. 

Each operation, parameter and attribute has its own lifecycle defined in the documentation as an `x-lifecycle` property. For undocumented parameters and attributes you can assume the status of the operation. 

## Lifecycle
Items will progress through the following maturity levels:

**Proposed** - We are considering building this item and would like your feedback. It can only be used with the mock server.

**Experimental** - This item has been implemented but may be removed at any time. 

**Active** - You can use this item with confidence.

**Deprecated** - You can still use this item however should consider using something else. Where possible we will continue to support the item for 2 years between announcing the deprecation and removing it. 

**Removed** - This item has been removed and can no longer be used. 

To aid in your planning the `deprecated` date, `proposedRemoval` date and migration `description` properties will be added.

## Other changes
We may add new optional fields or valid values at any time. These will be documented in the [changelog](Changelog.md)

All properties should have clear validation rules in the documentation including data type, min/max length or regular expression. If you find one that is unclear please report this bug in our documentation. Once documented we may loosen the restriction but will not tighten them without warning.

## Warning system
It is strongly recommended that you tell us which fields you are using by including the `fields` query parameter on every request that returns data. That will allow us to warn you if you are using fields that have been deprecated on a resource. It will also stop surprise new fields being added to your response.
