package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.Invoice;
import com.graphqljava.tutorial.retail.model.chinook.input.InvoiceInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class InvoiceController {
    private final JdbcClient jdbcClient;

    private final RowMapper<Invoice>
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

    public InvoiceController(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Invoice> Invoices(final @Argument InvoiceInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"Invoice\"") :
                this.jdbcClient.sql("select * from \"Invoice\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
