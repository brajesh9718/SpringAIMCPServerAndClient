package com.example.mcp.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI Agent endpoint that uses ChatClient with MCP tools.
 * Leverages all available MCP Server tools for intelligent customer data retrieval.
 * 
 * Available MCP Server Tools:
 * - getCustomerById: Get a customer's details by ID
 * - getAllCustomers: Get all customers
 * - getCustomersByNameStartsWith: Filter customers by name prefix
 * - getCustomersByNameNotStartsWith: Filter customers by name exclusion
 * - getCustomersByStatus: Filter customers by account status (ACTIVE, SUSPENDED, etc.)
 * - getCustomersByTier: Filter customers by tier level (Gold, Silver, Standard)
 * - countAllCustomers: Get total customer count
 * - searchCustomersByName: Search customers by partial name match
 */
@RestController
@RequestMapping("/ai")
public class AiAgentController {

    private final ChatClient chatClient;
    private final ChatClient guidedChatClient;
    private final ToolCallbackProvider toolCallbackProvider;

    public AiAgentController(ChatModel chatModel, ToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;
        
        // Standard chat client with all tools enabled
        this.chatClient = ChatClient.builder(chatModel)
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
                .build();
        
        // Guided chat client with system prompt to optimize tool usage
        this.guidedChatClient = ChatClient.builder(chatModel)
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
                .defaultSystem("""
                    You are an intelligent customer data assistant with access to the following tools:
                    
                    Customer Lookup Tools:
                    - getCustomerById: Retrieve a specific customer by their ID
                    - getAllCustomers: Retrieve all customers in the system
                    - searchCustomersByName: Search for customers by partial name match
                    
                    Customer Filtering Tools:
                    - getCustomersByStatus: Filter customers by status (ACTIVE, SUSPENDED, INACTIVE)
                    - getCustomersByTier: Filter customers by tier (Gold, Silver, Standard)
                    - getCustomersByNameStartsWith: Find customers whose names start with a letter
                    - getCustomersByNameNotStartsWith: Find customers whose names don't start with a letter
                    
                    Analytics Tools:
                    - countAllCustomers: Get the total number of customers
                    
                    Always use the most appropriate tool to answer the user's query.
                    Provide a clear summary of the results with customer details.
                    """)
                .build();
    }

    /**
     * Chat endpoint that can invoke MCP tools.
     * Handles both single customer and list of customers automatically.
     * 
     * Example requests:
     * POST /ai/chat
     * {
     *   "message": "What is the status of customer 101?"
     * }
     * {
     *   "message": "Get all customers stored in memory"
     * }
     * {
     *   "message": "Find all ACTIVE customers"
     * }
     * {
     *   "message": "How many customers do we have?"
     * }
     */
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        try {
            // Try to get response as CustomerResponse directly
            CustomerResponse response = chatClient.prompt()
                    .user(request.message())
                    .call()
                    .entity(CustomerResponse.class);
            
            return new ChatResponse(request.message(), response);
        } catch (Exception e) {
            // Fallback: try to get as List of customers
            try {
                List<CustomerDto> customers = chatClient.prompt()
                        .user(request.message())
                        .call()
                        .entity(new ParameterizedTypeReference<List<CustomerDto>>() {});
                
                CustomerResponse response = new CustomerResponse(customers);
                return new ChatResponse(request.message(), response);
            } catch (Exception e2) {
                // Last resort: return error response
                System.err.println("Error in chat: " + e2.getMessage());
                e2.printStackTrace();
                return new ChatResponse(request.message(), new CustomerResponse(new ArrayList<>()));
            }
        }
    }

    /**
     * Guided chat endpoint that uses a system prompt to optimize tool usage.
     * Better at understanding customer queries and using appropriate tools.
     * 
     * Example requests:
     * POST /ai/chat-guided
     * {
     *   "message": "Show me all Gold tier customers"
     * }
     * {
     *   "message": "Find customers with 'Smith' in their name"
     * }
     */
    @PostMapping("/chat-guided")
    public ChatResponse chatGuided(@RequestBody ChatRequest request) {
        try {
            // Try to get response as CustomerResponse
            CustomerResponse response = guidedChatClient.prompt()
                    .user(request.message())
                    .call()
                    .entity(CustomerResponse.class);
            
            return new ChatResponse(request.message(), response);
        } catch (Exception e) {
            // Fallback: try to get as List of customers
            try {
                List<CustomerDto> customers = guidedChatClient.prompt()
                        .user(request.message())
                        .call()
                        .entity(new ParameterizedTypeReference<List<CustomerDto>>() {});
                
                CustomerResponse response = new CustomerResponse(customers);
                return new ChatResponse(request.message(), response);
            } catch (Exception e2) {
                System.err.println("Error in chatGuided: " + e2.getMessage());
                e2.printStackTrace();
                return new ChatResponse(request.message(), new CustomerResponse(new ArrayList<>()));
            }
        }
    }

    /**
     * Chat endpoint with detailed response metadata.
     * Returns information about tools used and execution details.
     * 
     * Example request:
     * POST /ai/chat-detailed
     * {
     *   "message": "Tell me about customer 103"
     * }
     */
    @PostMapping("/chat-detailed")
    public Map<String, Object> chatDetailed(@RequestBody ChatRequest request) {
        try {
            // Get response as CustomerResponse
            CustomerResponse customerResponse = guidedChatClient.prompt()
                    .user(request.message())
                    .call()
                    .entity(CustomerResponse.class);
            
            return Map.of(
                    "message", request.message(),
                    "customers", customerResponse.customers(),
                    "customerCount", customerResponse.customers().size(),
                    "status", "success",
                    "toolsAvailable", getToolMetadata()
            );
        } catch (Exception e) {
            // Fallback: try to get as List directly
            try {
                List<CustomerDto> customers = guidedChatClient.prompt()
                        .user(request.message())
                        .call()
                        .entity(new ParameterizedTypeReference<List<CustomerDto>>() {});
                
                return Map.of(
                        "message", request.message(),
                        "customers", customers,
                        "customerCount", customers.size(),
                        "status", "success",
                        "toolsAvailable", getToolMetadata()
                );
            } catch (Exception e2) {
                System.err.println("Error in chatDetailed: " + e2.getMessage());
                e2.printStackTrace();
                return Map.of(
                        "message", request.message(),
                        "error", e2.getMessage(),
                        "status", "error",
                        "toolsAvailable", getToolMetadata()
                );
            }
        }
    }

    /**
     * Get metadata about all available MCP tools.
     * Useful for understanding tool capabilities.
     * 
     * Example request:
     * POST /ai/tools-metadata
     * {
     *   "message": "Tell me what tools are available"
     * }
     */
    @PostMapping("/tools-metadata")
    public Map<String, Object> getToolsMetadataEndpoint(@RequestBody ChatRequest request) {
        return Map.of(
                "message", request.message(),
                "response", "Available MCP Server Tools",
                "tools", getToolMetadata()
        );
    }

    /**
     * Helper method to get detailed tool metadata
     */
    private List<Map<String, String>> getToolMetadata() {
        return Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .map(ToolCallback::getToolDefinition)
                .map(definition -> Map.of(
                        "name", definition.name(),
                        "description", definition.description() != null ? definition.description() : "No description"))
                .toList();
    }

}
