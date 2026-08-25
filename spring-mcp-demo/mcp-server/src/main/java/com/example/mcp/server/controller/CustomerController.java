package com.example.mcp.server.controller;

import com.example.mcp.server.entity.Customer;
import com.example.mcp.server.service.CustomerService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer Controller - REST API endpoints for customer operations
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	/**
	 * Get all customers
	 * GET /api/customers
	 */
	@GetMapping
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return ResponseEntity.ok(customers);
	}

	/**
	 * Get customer by customerId
	 * GET /api/customers/{customerId}
	 */
	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
		Optional<Customer> customer = customerService.getCustomerById(customerId);
		return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Get customers by status
	 * GET /api/customers/filter/status?status=ACTIVE
	 */
	@GetMapping("/filter/status")
	public ResponseEntity<List<Customer>> getCustomersByStatus(@RequestParam String status) {
		List<Customer> customers = customerService.getCustomersByStatus(status);
		return ResponseEntity.ok(customers);
	}

	/**
	 * Get customers by tier
	 * GET /api/customers/filter/tier?tier=Gold
	 */
	@GetMapping("/filter/tier")
	public ResponseEntity<List<Customer>> getCustomersByTier(@RequestParam String tier) {
		List<Customer> customers = customerService.getCustomersByTier(tier);
		return ResponseEntity.ok(customers);
	}

	/**
	 * Get customers whose name starts with a letter
	 * GET /api/customers/filter/name-starts?letter=J
	 */
	@GetMapping("/filter/name-starts")
	public ResponseEntity<List<Customer>> getCustomersByNameStartsWith(@RequestParam String letter) {
		List<Customer> customers = customerService.getCustomersByNameStartsWith(letter);
		return ResponseEntity.ok(customers);
	}

	/**
	 * Get customers whose name does NOT start with a letter
	 * GET /api/customers/filter/name-not-starts?letter=J
	 */
	@GetMapping("/filter/name-not-starts")
	public ResponseEntity<List<Customer>> getCustomersByNameNotStartsWith(@RequestParam String letter) {
		List<Customer> customers = customerService.getCustomersByNameNotStartsWith(letter);
		return ResponseEntity.ok(customers);
	}

	/**
	 * Search customers by name pattern
	 * GET /api/customers/search?name=Smith
	 */
	@GetMapping("/search")
	public ResponseEntity<List<Customer>> searchCustomersByName(@RequestParam String name) {
		List<Customer> customers = customerService.searchCustomersByName(name);
		return ResponseEntity.ok(customers);
	}

	/**
	 * Get total count of customers
	 * GET /api/customers/count
	 */
	@GetMapping("/count")
	public ResponseEntity<Long> countAllCustomers() {
		long count = customerService.countAllCustomers();
		return ResponseEntity.ok(count);
	}

	/**
	 * Create new customer
	 * POST /api/customers
	 */
	@PostMapping
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		try {
			Customer createdCustomer = customerService.createCustomer(customer);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Update customer
	 * PUT /api/customers/{customerId}
	 */
	@PutMapping("/{customerId}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable String customerId, @RequestBody Customer customerDetails) {
		try {
			Customer updatedCustomer = customerService.updateCustomer(customerId, customerDetails);
			return ResponseEntity.ok(updatedCustomer);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Delete customer
	 * DELETE /api/customers/{customerId}
	 */
	@DeleteMapping("/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
		try {
			customerService.deleteCustomer(customerId);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Health check endpoint
	 * GET /api/customers/health
	 */
	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("Customer Service is running");
	}
}
