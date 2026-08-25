package com.example.mcp.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Customer Entity - Represents customer data in the database
 * JPA entity mapped to 'customers' table
 */
@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "customer_id", unique = true, nullable = false)
	private String customerId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "tier", nullable = false)
	private String tier;

	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phone;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// Constructors
	public Customer() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Customer(String customerId, String name, String status, String tier) {
		this.customerId = customerId;
		this.name = name;
		this.status = status;
		this.tier = tier;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Customer(String customerId, String name, String status, String tier, String email, String phone) {
		this.customerId = customerId;
		this.name = name;
		this.status = status;
		this.tier = tier;
		this.email = email;
		this.phone = phone;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Customer{" + "id=" + id + ", customerId='" + customerId + '\'' + ", name='" + name + '\'' + ", status='"
				+ status + '\'' + ", tier='" + tier + '\'' + ", email='" + email + '\'' + ", phone='" + phone + '\''
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
	}
}
