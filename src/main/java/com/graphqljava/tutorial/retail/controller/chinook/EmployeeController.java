package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.Employee;
import com.graphqljava.tutorial.retail.model.chinook.input.EmployeeInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class EmployeeController {
    private final JdbcClient jdbcClient;

    private final RowMapper<Employee>
            rowMapper = (rs, rowNum) -> new Employee(
            rs.getInt("EmployeeId"),
            rs.getString("LastName"),
            rs.getString("FirstName"),
            rs.getString("Title"),
            rs.getInt("ReportsTo"),
            rs.getString("BirthDate"),
            rs.getString("HireDate"),
            rs.getString("Address"),
            rs.getString("City"),
            rs.getString("State"),
            rs.getString("Country"),
            rs.getString("PostalCode"),
            rs.getString("Phone"),
            rs.getString("Fax"),
            rs.getString("Email")
    );

    public EmployeeController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Employee> Employees(final @Argument EmployeeInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"Employee\"") :
                this.jdbcClient.sql("select * from \"Employee\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
