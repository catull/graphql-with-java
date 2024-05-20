package com.graphqljava.tutorial.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class RetailModels {

	@Entity
	@Table(name = "\"account\"")
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class account {
		@Id
		private UUID id;
		private String name;
		private String created_at;
		private String updated_at;
	}

	@Entity
	@Table(name = "\"order_detail\"")
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class order_detail {
		@Id
		private UUID id;
		private UUID order_id;
		private UUID product_id;
		private Integer units;
		private String created_at;
		private String updated_at;
	}

	@Entity
	@Table(name = "\"order\"")
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class order {
		@Id
		private UUID id;
		private UUID account_id;
		private String status;
		private String region;
		private String created_at;
		private String updated_at;
	}

	@Entity
	@Table(name = "\"product\"")
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class product {
		@Id
		private UUID id;
		private String name;
		private Integer price;
		private String created_at;
		private String updated_at;
	}

	@Entity
	@Table(name = "\"region\"")
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class region {
		@Id
		private String value;
		private String description;
	}
}
