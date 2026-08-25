package com.example.mcp.server.repository;

import com.example.mcp.server.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Customer Repository - Spring Data JPA for database operations
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * Find customer by customerId (business identifier)
	 */
	Optional<Customer> findByCustomerId(String customerId);

	/**
	 * Find all customers by status
	 */
	List<Customer> findByStatus(String status);

	/**
	 * Find all customers by tier
	 */
	List<Customer> findByTier(String tier);

	/**
	 * Find customers whose name starts with a specific letter
	 */
	@Query("SELECT c FROM Customer c WHERE UPPER(c.name) LIKE :letter%")
	List<Customer> findByNameStartsWith(@Param("letter") String letter);

	/**
	 * Find customers whose name does NOT start with a specific letter
	 */
	@Query("SELECT c FROM Customer c WHERE UPPER(c.name) NOT LIKE :letter%")
	List<Customer> findByNameNotStartsWith(@Param("letter") String letter);

	/**
	 * Search customers by partial name match
	 */
	@Query("SELECT c FROM Customer c WHERE UPPER(c.name) LIKE %:namePattern%")
	List<Customer> searchByName(@Param("namePattern") String namePattern);

	/**
	 * Find customers by email
	 */
	Optional<Customer> findByEmail(String email);

	/**
	 * Check if customer exists by customerId
	 */
	boolean existsByCustomerId(String customerId);
}
