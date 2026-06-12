---
tags: [overview, errors, troubleshooting]
---

# Errors

When an error occurs, the API returns a 4XX or 5XX HTTP status code. The response body contains a list of error objects with details to help you diagnose and fix the issue.

## Error Response Format

Errors follow the [JSON:API error format](https://jsonapi.org/format/#errors):

```json
{
  "errors": [
    {
      "status": "422",
      "code": "MalformedRequestBody",
      "title": "Malformed request body",
      "detail": "Could not deserialize terms from body: unexpected comma at line 20",
      "meta": {
        "resourceType": "terms",
        "paramValue": "unexpected comma at line 20"
      },
      "source": {
        "parameter": "page[size]"
      },
      "links": {
        "about": {
          "href": "https://prod.apigateway.co/docs/errorTypes/MalformedRequestBody"
        },
        "docs": {
          "href": "https://prod.apigateway.co/docs/#operation/get-terms-type"
        }
      }
    }
  ]
}
```

### Error Object Fields

| Field | Description |
|---|---|
| `status` | The HTTP status code as a string |
| `code` | A machine-readable error code identifying the specific error type |
| `title` | A short, human-readable summary of the error |
| `detail` | A more specific explanation of what went wrong |
| `source.parameter` | The query parameter that caused the error (when applicable) |
| `source.pointer` | A JSON pointer to the request body field that caused the error (when applicable) |
| `meta` | Additional context about the error |
| `links.about` | A URL with more information about this error type |
| `links.docs` | A URL to the documentation for the operation that was called |

## HTTP Status Codes

### Client Errors (4XX)

| Status | Meaning | Common Causes |
|---|---|---|
| `400` | Bad Request | Malformed JSON, invalid content type, missing required fields |
| `401` | Unauthorized | Missing or expired access token, invalid credentials |
| `403` | Forbidden | The access token lacks the required scope, or the service account doesn't have permission for this resource |
| `404` | Not Found | The resource ID doesn't exist, or the endpoint path is incorrect |
| `409` | Conflict | The resource already exists (e.g., creating a customer with a duplicate identifier) |
| `422` | Unprocessable Entity | The request body is valid JSON but contains invalid values (wrong type, out-of-range, unsupported enum value) |
| `429` | Too Many Requests | You've exceeded the rate limit. Implement exponential backoff and retry. |

### Server Errors (5XX)

| Status | Meaning | What To Do |
|---|---|---|
| `500` | Internal Server Error | Retry with exponential backoff. If persistent, contact support. |
| `502` | Bad Gateway | Temporary infrastructure issue. Retry after a short delay. |
| `503` | Service Unavailable | The service is temporarily overloaded or under maintenance. Retry with backoff. |

## Common Error Codes

### Request Errors

| Code | Status | Description |
|---|---|---|
| `MalformedRequestBody` | 422 | The request body could not be parsed. Check for syntax errors in your JSON. |
| `QueryParameterBadValue` | 422 | A query parameter has an invalid value. The `source.parameter` field indicates which one. |

### Resource Errors

| Code | Status | Description |
|---|---|---|
| `NotReady` | 422 | A dependent resource is not yet ready. This commonly happens when creating an order immediately after creating a new sales account. Retry with exponential backoff — the account is typically ready within 5 seconds. |
| `NotFound` | 404 | The requested resource does not exist. Verify the ID and endpoint path. |

### Authentication Errors

| Code | Status | Description |
|---|---|---|
| `Unauthorized` | 401 | The access token is missing, expired, or invalid. Obtain a new token using the [Authorization guide](../Authorization/Authorization.md). |
| `Forbidden` | 403 | The access token is valid but lacks the required scope. Check the `scope` parameter when requesting your token. |

## Handling Errors

### Retry Strategy

For transient errors (`429`, `500`, `502`, `503`, and `NotReady`), implement exponential backoff:

1. Wait 1 second, then retry
2. If it fails again, wait 2 seconds
3. Then 4 seconds, 8 seconds, etc.
4. Give up after 5 attempts

```python
import time
import requests

def call_with_retry(method, url, **kwargs):
    for attempt in range(5):
        response = method(url, **kwargs)
        if response.status_code in (429, 500, 502, 503):
            time.sleep(2 ** attempt)
            continue
        if response.status_code == 422:
            errors = response.json().get("errors", [])
            if any(e.get("code") == "NotReady" for e in errors):
                time.sleep(2 ** attempt)
                continue
        return response
    return response
```

### Error Responses May Contain Multiple Errors

A single request can produce multiple errors. Always iterate over the full `errors` array rather than only reading the first entry.

### Use the `links` Object

Error responses include `links.about` URLs that provide additional context for each error type. These can be useful for debugging and for building user-facing error messages.
