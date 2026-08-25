# Spring MCP Demo - Server URLs

## ✅ Servers Running

Both the MCP Server and MCP Client are now running successfully!

### MCP Server
- **Port**: 8081
- **Base URL**: `http://localhost:8081`
- **MCP Endpoint**: `http://localhost:8081/mcp`
- **Status**: ✅ Running (Listening)

### MCP Client  
- **Port**: 8082
- **Base URL**: `http://localhost:8082`
- **Tools Endpoint**: `http://localhost:8082/tools`
- **Status**: ✅ Running (Listening)

---

## How to Access

### 1. View Available Tools (from Client)
```bash
curl http://localhost:8082/tools
```

**Expected Response:**
```json
{
  "tools": [
    {
      "description": "Get a customer's name, account status, and tier using their customer ID.",
      "name": "getCustomerById"
    }
  ]
}
```

### 2. Access the Server Directly
The server exposes MCP tools at:
```bash
curl -H "Accept: text/event-stream" http://localhost:8081/mcp
```

### 3. Client-to-Server Communication
- The **MCP Client** (port 8082) connects to the **MCP Server** (port 8081)
- The client discovers and caches the server's tools at startup
- Tools are exposed via the `/tools` endpoint on the client

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                    Spring MCP Demo                      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────────────┐    ┌──────────────────────┐  │
│  │   MCP Server         │    │   MCP Client         │  │
│  │   Port: 8081         │◄───┤   Port: 8082         │  │
│  │                      │    │                      │  │
│  │ • Tool Provider:     │    │ • Tool Discoverer    │  │
│  │   - getCustomerById  │    │ • Tool Aggregator    │  │
│  │                      │    │ • HTTP Interface     │  │
│  │ • HTTP Streaming     │    │                      │  │
│  │   (SSE)              │    │ Endpoint:            │  │
│  │                      │    │ /tools - List tools  │  │
│  │ Endpoint:            │    │                      │  │
│  │ /mcp - MCP Protocol  │    │                      │  │
│  └──────────────────────┘    └──────────────────────┘  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## Next Steps

### For AI Integration
To turn this into an AI agent:

1. Add a Spring AI chat-model starter (OpenAI, Azure OpenAI, or Ollama) to `mcp-client`
2. Inject `ToolCallbackProvider` into a `ChatClient`
3. Call `.tools(toolCallbackProvider)` to enable tool use
4. The model can then decide when to invoke the `getCustomerById` tool

### Production Readiness
- Replace the in-memory map in `CustomerTools` with an API client
- Enforce identity and authorization inside every tool
- Do not return confidential fields unless needed

---

## Troubleshooting

### Servers not starting?
1. Ensure Java 17+ is installed
2. Check that ports 8081 and 8082 are not in use
3. Run from the project root directory

### Connection refused?
- Verify server has fully started (wait 15-20 seconds)
- Check firewall settings
- Ensure localhost resolution works

### Invalid Accept header error on /mcp endpoint?
- This is expected - the MCP endpoint requires `Accept: text/event-stream` header
- Use the `/tools` endpoint on the client instead for simple HTTP access

