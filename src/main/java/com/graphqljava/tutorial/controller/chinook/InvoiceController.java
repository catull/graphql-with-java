package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.model.chinook.Customer;
import com.graphqljava.tutorial.model.chinook.Invoice;
import com.graphqljava.tutorial.model.chinook.InvoiceLine;
import com.graphqljava.tutorial.model.chinook.input.InvoiceInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class InvoiceController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<Invoice>
            rowMapper = (rs, rowNum) -> new Invoice(
            rs.getInt("InvoiceId"),
            rs.getInt("CustomerId"),
            rs.getString("InvoiceDate"),
            rs.getString("BillingAddress"),
            rs.getString("BillingCity"),
            rs.getString("BillingState"),
            rs.getString("BillingCountry"),
            rs.getString("BillingPostalCode"),
            rs.getFloat("Total")
    );

    public InvoiceController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Invoice> Invoices(@Argument InvoiceInput input) {
        if (null == input) {
            input = new InvoiceInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<Invoice> Invoices(final Customer customer, @Argument InvoiceInput input) {
        if (null == input) {
            input = new InvoiceInput();
        }
        if (null == input.getCustomerId()) {
            input.setCustomerId(customer.CustomerId());
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Invoice Invoice(final InvoiceLine invoiceLine, @Argument InvoiceInput input) {
        if (null == input) {
            input = new InvoiceInput();
        }
        if (null == input.getInvoiceId()) {
            input.setInvoiceId(invoiceLine.InvoiceId());
        }

        return spec(input).query(rowMapper).single();
    }

    private StatementSpec spec(final InvoiceInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"Invoice\"";

        Integer invoiceId = input.getInvoiceId();
        if (null != invoiceId) {
            columns.add("InvoiceId");
            params.add(invoiceId);
        }

        Integer customerId = input.getCustomerId();
        if (null != customerId) {
            columns.add("CustomerId");
            params.add(customerId);
        }

        String invoiceDate = input.getInvoiceDate();
        if (null != invoiceDate) {
            columns.add("InvoiceDate");
            params.add(invoiceDate);
        }

        String billingAddress = input.getBillingAddress();
        if (null != billingAddress) {
            columns.add("BillingAddress");
            params.add(billingAddress);
        }

        String billingCity = input.getBillingCity();
        if (null != billingCity) {
            columns.add("BillingCity");
            params.add(billingCity);
        }

        String billingState = input.getBillingState();
        if (null != billingState) {
            columns.add("BillingState");
            params.add(billingState);
        }

        String billingCountry = input.getBillingCountry();
        if (null != billingCountry) {
            columns.add("BillingCountry");
            params.add(billingCountry);
        }

        String billingPostalCode = input.getBillingPostalCode();
        if (null != billingPostalCode) {
            columns.add("BillingPostalCode");
            params.add(billingPostalCode);
        }

        Float total = input.getTotal();
        if (null != total) {
            columns.add("Total");
            params.add(total);
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
                .map(w -> "\"Invoice\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
