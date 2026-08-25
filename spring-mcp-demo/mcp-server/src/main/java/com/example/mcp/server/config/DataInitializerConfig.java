package com.example.mcp.server.config;

import com.example.mcp.server.entity.Customer;
import com.example.mcp.server.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Data Initialization Configuration
 * Populates H2 in-memory database with initial customer data
 */
@Configuration
public class DataInitializerConfig {

	@Autowired
	private CustomerRepository customerRepository;

	@Bean
	public ApplicationRunner initializeData() {
		return args -> {
			// Check if data already exists
			if (customerRepository.count() > 0) {
				System.out.println("Database already populated with customers");
				return;
			}

			// Create sample customers
			Customer customer1 = new Customer("101", "Jane Doe", "ACTIVE", "Gold", "jane.doe@example.com",
					"+1-555-0101");
			Customer customer2 = new Customer("102", "Alice Smith", "ACTIVE", "Silver", "alice.smith@example.com",
					"+1-555-0102");
			Customer customer3 = new Customer("103", "Bob Johnson", "SUSPENDED", "Gold", "bob.johnson@example.com",
					"+1-555-0103");
			Customer customer4 = new Customer("104", "Charlie Brown", "ACTIVE", "Standard", "charlie.brown@example.com",
					"+1-555-0104");
			Customer customer5 = new Customer("105", "Diana Prince", "ACTIVE", "Gold", "diana.prince@example.com",
					"+1-555-0105");
			Customer customer6 = new Customer("106", "Eve Wilson", "ACTIVE", "Silver", "eve.wilson@example.com",
					"+1-555-0106");
			Customer customer7 = new Customer("107", "Frank Miller", "SUSPENDED", "Standard", "frank.miller@example.com",
					"+1-555-0107");
			Customer customer8 = new Customer("108", "Grace Lee", "ACTIVE", "Gold", "grace.lee@example.com",
					"+1-555-0108");

			// Save all customers
			customerRepository.save(customer1);
			customerRepository.save(customer2);
			customerRepository.save(customer3);
			customerRepository.save(customer4);
			customerRepository.save(customer5);
			customerRepository.save(customer6);
			customerRepository.save(customer7);
			customerRepository.save(customer8);

			System.out.println("Database initialized with 8 customers");
		};
	}
}
