package com.example.mcp.server.service;

import com.example.mcp.server.entity.Customer;
import com.example.mcp.server.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Customer Service - Business logic for customer operations
 */
@Service
@Transactional
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Get customer by customerId
	 */
	public Optional<Customer> getCustomerById(String customerId) {
		return customerRepository.findByCustomerId(customerId);
	}

	/**
	 * Get all customers
	 */
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	/**
	 * Get customers by status
	 */
	public List<Customer> getCustomersByStatus(String status) {
		return customerRepository.findByStatus(status);
	}

	/**
	 * Get customers by tier
	 */
	public List<Customer> getCustomersByTier(String tier) {
		return customerRepository.findByTier(tier);
	}

	/**
	 * Get customers whose name starts with a specific letter
	 */
	public List<Customer> getCustomersByNameStartsWith(String letter) {
		return customerRepository.findByNameStartsWith(letter.toUpperCase());
	}

	/**
	 * Get customers whose name does NOT start with a specific letter
	 */
	public List<Customer> getCustomersByNameNotStartsWith(String letter) {
		return customerRepository.findByNameNotStartsWith(letter.toUpperCase());
	}

	/**
	 * Search customers by partial name match
	 */
	public List<Customer> searchCustomersByName(String namePattern) {
		return customerRepository.searchByName(namePattern.toUpperCase());
	}

	/**
	 * Get total count of customers
	 */
	public long countAllCustomers() {
		return customerRepository.count();
	}

	/**
	 * Create/Save new customer
	 */
	public Customer createCustomer(Customer customer) {
		if (customerRepository.existsByCustomerId(customer.getCustomerId())) {
			throw new IllegalArgumentException("Customer with ID " + customer.getCustomerId() + " already exists");
		}
		return customerRepository.save(customer);
	}

	/**
	 * Update existing customer
	 */
	public Customer updateCustomer(String customerId, Customer customerDetails) {
		Optional<Customer> existing = customerRepository.findByCustomerId(customerId);
		if (existing.isEmpty()) {
			throw new IllegalArgumentException("Customer with ID " + customerId + " not found");
		}

		Customer customer = existing.get();
		if (customerDetails.getName() != null) {
			customer.setName(customerDetails.getName());
		}
		if (customerDetails.getStatus() != null) {
			customer.setStatus(customerDetails.getStatus());
		}
		if (customerDetails.getTier() != null) {
			customer.setTier(customerDetails.getTier());
		}
		if (customerDetails.getEmail() != null) {
			customer.setEmail(customerDetails.getEmail());
		}
		if (customerDetails.getPhone() != null) {
			customer.setPhone(customerDetails.getPhone());
		}

		return customerRepository.save(customer);
	}

	/**
	 * Delete customer by customerId
	 */
	public void deleteCustomer(String customerId) {
		Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
		if (customer.isEmpty()) {
			throw new IllegalArgumentException("Customer with ID " + customerId + " not found");
		}
		customerRepository.delete(customer.get());
	}

	/**
	 * Get customer by email
	 */
	public Optional<Customer> getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
}
