package com.example.mcp.server;

import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mcp.server.entity.Customer;
import com.example.mcp.server.service.CustomerService;

/**
 * MCP Tools for AI clients - Delegates to CustomerService which uses H2 database
 * Provides customer lookup and filtering operations via MCP protocol
 */
@Service
public class CustomerTools {

	@Autowired
	private CustomerService customerService;

	@Tool(description = "Get a customer's name, account status, and tier using their customer ID.")
	public Customer getCustomerById(String customerId) {
		return customerService.getCustomerById(customerId)
				.orElse(new Customer(customerId, "Not found", "UNKNOWN", "N/A"));
	}

	@Tool(description = "Get all customers stored in memory")
	public List<Customer> getAllCustomers() {
		return customerService.getAllCustomers();
	}

	@Tool(description = "Get customers whose name starts with a specific letter. Example: 'J' returns customers starting with J")
	public List<Customer> getCustomersByNameStartsWith(String letter) {
		return customerService.getCustomersByNameStartsWith(letter);
	}

	@Tool(description = "Get customers whose name does NOT start with a specific letter. Example: 'J' returns all customers except those starting with J")
	public List<Customer> getCustomersByNameNotStartsWith(String letter) {
		return customerService.getCustomersByNameNotStartsWith(letter);
	}

	@Tool(description = "Get customers by their account status. Example: 'ACTIVE', 'SUSPENDED'")
	public List<Customer> getCustomersByStatus(String status) {
		return customerService.getCustomersByStatus(status);
	}

	@Tool(description = "Get customers by their tier level. Example: 'Gold', 'Silver', 'Standard'")
	public List<Customer> getCustomersByTier(String tier) {
		return customerService.getCustomersByTier(tier);
	}

	@Tool(description = "Get the total count of all customers stored in memory")
	public long countAllCustomers() {
		return customerService.countAllCustomers();
	}

	@Tool(description = "Search customers by partial name match. Example: 'Smith' returns all customers with 'Smith' in their name")
	public List<Customer> searchCustomersByName(String namePattern) {
		return customerService.searchCustomersByName(namePattern);
	}

}
