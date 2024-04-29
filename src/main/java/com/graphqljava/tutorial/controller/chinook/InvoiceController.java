package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
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
public class InvoiceController extends BaseController {

    public InvoiceController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"Invoice\"";
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

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private static final RowMapper<Invoice>
        rowMapper = (rs, rowNum) -> new Invoice (
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

    private StatementSpec spec(final InvoiceInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "InvoiceId", input.getInvoiceId());
        extractInputParameterAndValue(columns, params, "CustomerId", input.getCustomerId());
        extractInputParameterAndValue(columns, params, "InvoiceDate", input.getInvoiceDate());
        extractInputParameterAndValue(columns, params, "BillingAddress", input.getBillingAddress());
        extractInputParameterAndValue(columns, params, "BillingCity", input.getBillingCity());
        extractInputParameterAndValue(columns, params, "BillingState", input.getBillingState());
        extractInputParameterAndValue(columns, params, "BillingCountry", input.getBillingCountry());
        extractInputParameterAndValue(columns, params, "BillingPostalCode", input.getBillingPostalCode());
        extractInputParameterAndValue(columns, params, "Total", input.getTotal());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
