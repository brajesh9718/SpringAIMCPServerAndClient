package com.example.mcp.client;

import java.util.Arrays;
import java.util.Map;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes the MCP tools discovered by this client for a quick connectivity check. */
@RestController
public class ToolCatalogController {

    private final ToolCallbackProvider toolCallbackProvider;

    public ToolCatalogController(ToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @GetMapping("/tools")
    public Map<String, Object> tools() {
        return Map.of("tools", Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .map(ToolCallback::getToolDefinition)
                .map(definition -> Map.of(
                        "name", definition.name(),
                        "description", definition.description()))
                .toList());
    }
}

