# Rate Limits

To keep the platform fast and reliable for everyone, requests to the Vendasta APIs are rate limited. Limits are applied per partner (across all of your service accounts) and separately for each API endpoint.

## Default limits

Unless a higher limit has been arranged for your account, the following limits apply to every endpoint:

| Window      | Limit          |
| ----------- | -------------- |
| Per second  | 100 requests   |
| Per minute  | 500 requests   |
| Per hour    | 10,000 requests |

These tiers are evaluated independently. A request is rejected as soon as it would exceed any one of them, so a burst that stays under the per-second limit can still be throttled by the per-minute or per-hour limit.

## When you exceed a limit

When you go over a limit, the API responds with HTTP `429 Too Many Requests` and a JSON body describing the limit that was hit:

```json
{
  "code": 429,
  "message": "Rate limit exceeded. Too many requests.",
  "details": {
    "limit": 500,
    "remaining": 0,
    "retryAfter": 12
  }
}
```

Every response, whether it succeeds or is rejected, includes headers describing your current usage:

| Header                  | Description                                                   |
| ----------------------- | ------------------------------------------------------------- |
| `X-RateLimit-Limit`     | Maximum requests allowed in the current window.               |
| `X-RateLimit-Remaining` | Requests remaining in the current window.                     |
| `X-RateLimit-Used`      | Requests already consumed in the current window.              |
| `X-RateLimit-Reset`     | Unix timestamp (in seconds) when the window resets.           |
| `Retry-After`           | Seconds to wait before retrying. Sent on `429` responses.     |

## Staying within the limits

<!-- theme: info -->
> Read the `X-RateLimit-Remaining` header and slow down before you run out, rather than waiting for a `429`.

- On a `429`, wait for the number of seconds in the `Retry-After` header before retrying, and use exponential backoff for repeated failures.
- Spread bulk or batch work out over time instead of sending it all at once.
- Cache responses that do not change often instead of re-requesting them.

If your integration consistently needs higher limits, contact [support@vendasta.com](mailto:support@vendasta.com) to discuss your use case.
