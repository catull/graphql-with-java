package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.chinook.Customer;
import com.graphqljava.tutorial.model.chinook.Employee;
import com.graphqljava.tutorial.model.chinook.input.EmployeeInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class EmployeeController extends BaseController {

    public EmployeeController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"Employee\"";
    }

    @QueryMapping
    public List<Employee> Employees(@Argument EmployeeInput input) {
        if (null == input) {
            input = new EmployeeInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Employee SupportRep(final Customer customer, @Argument EmployeeInput input) {
        if (null == input) {
            input = new EmployeeInput();
        }
        if (null == input.getEmployeeId()) {
            input.setEmployeeId(customer.SupportRepId());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    @SchemaMapping
    public Employee Manager(final Employee employee, @Argument EmployeeInput input) {
        if (null == input) {
            input = new EmployeeInput();
        }
        if (null == input.getEmployeeId()) {
            input.setEmployeeId(employee.ReportsTo());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    @SchemaMapping
    public Employee Reports(final Employee employee, @Argument EmployeeInput input) {
        if (null == input) {
            input = new EmployeeInput();
        }
        if (null == input.getReportsTo()) {
            input.setReportsTo(employee.ReportsTo());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private static final RowMapper<Employee>
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

    private StatementSpec spec(final EmployeeInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "EmployeeId", input.getEmployeeId());
        extractInputParameterAndValue(columns, params, "LastName", input.getLastName());
        extractInputParameterAndValue(columns, params, "FirstName", input.getFirstName());
        extractInputParameterAndValue(columns, params, "Title", input.getTitle());
        extractInputParameterAndValue(columns, params, "ReportsTo", input.getReportsTo());
        extractInputParameterAndValue(columns, params, "BirthDate", input.getBirthDate());
        extractInputParameterAndValue(columns, params, "HireDate", input.getHireDate());
        extractInputParameterAndValue(columns, params, "Address", input.getAddress());
        extractInputParameterAndValue(columns, params, "City", input.getCity());
        extractInputParameterAndValue(columns, params, "State", input.getState());
        extractInputParameterAndValue(columns, params, "Country", input.getCountry());
        extractInputParameterAndValue(columns, params, "PostalCode", input.getPostalCode());
        extractInputParameterAndValue(columns, params, "Phone", input.getPhone());
        extractInputParameterAndValue(columns, params, "Fax", input.getFax());
        extractInputParameterAndValue(columns, params, "Email", input.getEmail());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
