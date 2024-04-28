package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.model.chinook.Customer;
import com.graphqljava.tutorial.model.chinook.Employee;
import com.graphqljava.tutorial.model.chinook.Invoice;
import com.graphqljava.tutorial.model.chinook.input.CustomerInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class CustomerController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<Customer>
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
    public List<Customer> Customers(@Argument CustomerInput input) {
        if (null == input) {
            input = new CustomerInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Customer Customer(final Invoice invoice, @Argument CustomerInput input) {
        if (null == input) {
            input = new CustomerInput();
        }
        if (null == input.getCustomerId()) {
            input.setCustomerId(invoice.CustomerId());
        }

        return spec(input).query(rowMapper).single();
    }

    @SchemaMapping
    public List<Customer> Customers(final Employee employee, @Argument CustomerInput input) {
        if (null == input) {
            input = new CustomerInput();
        }
        if (null == input.getCustomerId()) {
            input.setCustomerId(employee.EmployeeId());
        }

        return spec(input).query(rowMapper).list();
    }

    private StatementSpec spec(final CustomerInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"Customer\"";


        Integer customerId = input.getCustomerId();
        if (null != customerId) {
            columns.add("CustomerId");
            params.add(customerId);
        }

        String firstName = input.getFirstName();
        if (null != firstName) {
            columns.add("FirstName");
            params.add(firstName);
        }

        String lastName = input.getLastName();
        if (null != lastName) {
            columns.add("LastName");
            params.add(lastName);
        }

        String company = input.getCompany();
        if (null != company) {
            columns.add("Company");
            params.add(customerId);
        }

        String address = input.getAddress();
        if (null != address) {
            columns.add("Address");
            params.add(address);
        }

        String city = input.getCity();
        if (null != city) {
            columns.add("City");
            params.add(city);
        }

        String state = input.getState();
        if (null != state) {
            columns.add("State");
            params.add(state);
        }

        String country = input.getCountry();
        if (null != country) {
            columns.add("Country");
            params.add(country);
        }

        String postalCode = input.getPostalCode();
        if (null != postalCode) {
            columns.add("PostalCode");
            params.add(postalCode);
        }

        String phone = input.getPhone();
        if (null != phone) {
            columns.add("Phone");
            params.add(phone);
        }

        String fax = input.getFax();
        if (null != fax) {
            columns.add("Fax");
            params.add(fax);
        }

        String email = input.getEmail();
        if (null != email) {
            columns.add("Email");
            params.add(email);
        }

        Integer supportRepId = input.getSupportRepId();
        if (null != supportRepId) {
            columns.add("SupportRepId");
            params.add(supportRepId);
        }

        int limit = input.getLimit();
        boolean withLimit = limit > 0;
        if (!withLimit && params.isEmpty()) {
            return this.jdbcClient.sql(select);
        }

        if (withLimit) {
            params.add(limit);
        }

        if (withLimit && 1 == params.size()) {
            select += " limit ?";
            return this.jdbcClient.sql(select).param(limit);
        }

        select += " where " + columns.stream()
                .map(w -> "\"Customer\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
