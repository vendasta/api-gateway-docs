# Trigger a prebuilt automation

If you have setup automations in the Vendasta platform you can trigger them for a business. This enables you to set up a complex chain of actions but only need one api call. 

## Setup:

Create an access token with at least `business` scopes following the [Authorization guide](../../Authorization/Authorization.md).

Create an automation in the Vendasta platform with a [Manual Trigger and turn it on](https://support.vendasta.com/hc/en-us/articles/4406958983703).

## Step 1: Trigger the automation

<!--
type: tab
title: Request
-->

You will need the ID of the automation you previously created as well as the business ID of the business location you would like to trigger that automation for. Add the access token to the headers and send it off. 

This api call has an empty response.



```json http
{
  "method": "POST",
  "url": "https://prod.apigateway.co/platform/automationRuns",
  "query": {},
  "headers": {
    "Authorization": "Bearer <Token with 'business' scope>",
    "Content-Type": "application/vnd.api+json"
  },
  "body": {
     "data": {
        "type": "automationRuns",
        "attributes": {},
        "relationships": {
            "businessLocation": {
                "data": {
                    "id": "AG-6TRBKMP2BQ",
                    "type": "businessLocations"
                }
            },
            "automation": {
                "data": {
                    "id": "Automation-254551a2-ab4c-41fa-b7c2-1c53d448e258",
                    "type": "automations"
                }
            }
        }
    }
  }
}

```
For more details on this endpoint see [Create Automation Runs](../../../openapi/platform/platform.yaml/paths/~1automationRuns/post).

<!--
type: tab-end
-->
