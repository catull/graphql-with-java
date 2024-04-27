package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.Customer;
import com.graphqljava.tutorial.retail.model.chinook.input.CustomerInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class CustomerController {
    private final JdbcClient jdbcClient;

    private final RowMapper<Customer>
            rowMapper = (rs, rowNum) -> new Customer(
            rs.getInt("CustomerId"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Company"),
            rs.getString("Address"),
            rs.getString("City"),
            rs.getString("State"),
            rs.getString("Country"),
            rs.getString("PostalCode"),
            rs.getString("Phone"),
            rs.getString("Fax"),
            rs.getString("Email"),
            rs.getInt("SupportRepId")
    );

    public CustomerController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Customer> Customers(final @Argument CustomerInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"Customer\"") :
                this.jdbcClient.sql("select * from \"Customer\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
