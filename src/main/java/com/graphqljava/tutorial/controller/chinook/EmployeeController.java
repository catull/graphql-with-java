package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class EmployeeController {
    private final JdbcClient jdbcClient;

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

    public EmployeeController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
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
            input.setEmployeeId(customer.CustomerId());
        }

        return spec(input).query(rowMapper).single();
    }

    //Manager(input: EmployeeInput): Employee
    @SchemaMapping
    public Employee Manager(final Employee employee, @Argument EmployeeInput input) {
        if (null == input) {
            input = new EmployeeInput();
        }
        if (null == input.getEmployeeId()) {
            input.setEmployeeId(employee.ReportsTo());
        }

        return spec(input).query(rowMapper).single();
    }

    @SchemaMapping
    public Employee Reports(final Employee employee, @Argument EmployeeInput input) {
        if (null == input) {
            input = new EmployeeInput();
        }
        if (null == input.getReportsTo()) {
            input.setReportsTo(employee.ReportsTo());
        }

        return spec(input).query(rowMapper).single();
    }

    private StatementSpec spec(final EmployeeInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"Employee\"";

        Integer employeeId = input.getEmployeeId();
        if (null != employeeId) {
            columns.add("EmployeeId");
            params.add(employeeId);
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

        String title = input.getTitle();
        if (null != title) {
            columns.add("Title");
            params.add(title);
        }

        Integer reportsTo = input.getReportsTo();
        if (null != reportsTo) {
            columns.add("ReportsTo");
            params.add(reportsTo);
        }

        String birthDate = input.getBirthDate();
        if (null != birthDate) {
            columns.add("BirthDate");
            params.add(birthDate);
        }

        String hireDate = input.getHireDate();
        if (null != hireDate) {
            columns.add("HireDate");
            params.add(hireDate);
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
                .map(w -> "\"Employee\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
