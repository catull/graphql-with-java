package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
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
public class CustomerController extends BaseController {

    public CustomerController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"Customer\"";
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

        return spec(input).query(rowMapper).optional().orElse(null);
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

    private StatementSpec spec(final CustomerInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "CustomerId", input.getCustomerId());
        extractInputParameterAndValue(columns, params, "LastName", input.getLastName());
        extractInputParameterAndValue(columns, params, "FirstName", input.getFirstName());
        extractInputParameterAndValue(columns, params, "Company", input.getCompany());
        extractInputParameterAndValue(columns, params, "Address", input.getAddress());
        extractInputParameterAndValue(columns, params, "City", input.getCity());
        extractInputParameterAndValue(columns, params, "State", input.getState());
        extractInputParameterAndValue(columns, params, "Country", input.getCountry());
        extractInputParameterAndValue(columns, params, "PostalCode", input.getPostalCode());
        extractInputParameterAndValue(columns, params, "Phone", input.getPhone());
        extractInputParameterAndValue(columns, params, "Fax", input.getFax());
        extractInputParameterAndValue(columns, params, "Email", input.getEmail());
        extractInputParameterAndValue(columns, params, "SupportRepId", input.getSupportRepId());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
