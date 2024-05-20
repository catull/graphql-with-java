package com.graphqljava.tutorial.jpa.chinook;

import com.graphqljava.tutorial.models.ChinookModels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<ChinookModels.Employee, Integer> {
}
