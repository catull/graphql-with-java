package com.graphqljava.tutorial.jpa.retail;

import com.graphqljava.tutorial.models.RetailModels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<RetailModels.order_detail, UUID> {
}
