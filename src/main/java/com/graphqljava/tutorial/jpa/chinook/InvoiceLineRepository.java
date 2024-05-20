package com.graphqljava.tutorial.jpa.chinook;

import com.graphqljava.tutorial.models.ChinookModels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceLineRepository extends JpaRepository<ChinookModels.InvoiceLine, Integer> {
}
