package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.retail.model.chinook.Invoice;
import com.graphqljava.tutorial.retail.model.chinook.InvoiceLine;
import com.graphqljava.tutorial.retail.model.chinook.Track;
import com.graphqljava.tutorial.retail.model.chinook.input.InvoiceLineInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
    public class InvoiceLineController {
        private final JdbcClient jdbcClient;

    private static final RowMapper<InvoiceLine>
            rowMapper = (rs, rowNum) -> new InvoiceLine(
            rs.getInt("InvoiceLineId"),
            rs.getInt("InvoiceId"),
            rs.getInt("TrackId"),
            rs.getFloat("UnitPrice"),
            rs.getInt("Quantity")
    );

    public InvoiceLineController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<InvoiceLine> InvoiceLines(@Argument InvoiceLineInput input) {
        if (null == input) {
            input = new InvoiceLineInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<InvoiceLine> InvoiceLines(final Invoice invoice, @Argument InvoiceLineInput input) {
        if (null == input) {
            input = new InvoiceLineInput();
        }
        if (null == input.getInvoiceLineId()) {
            input.setInvoiceId(invoice.InvoiceId());
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<InvoiceLine> InvoiceLines(final Track track, @Argument InvoiceLineInput input) {
        if (null == input) {
            input = new InvoiceLineInput();
        }
        if (null == input.getTrackId()) {
            input.setTrackId(track.TrackId());
        }

        return spec(input).query(rowMapper).list();
    }

    private StatementSpec spec(final InvoiceLineInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"InvoiceLine\"";

        Integer invoiceLineId = input.getInvoiceLineId();
        if (null != invoiceLineId) {
            columns.add("InvoiceLineId");
            params.add(invoiceLineId);
        }

        Integer invoiceId = input.getInvoiceId();
        if (null != invoiceId) {
            columns.add("InvoiceId");
            params.add(invoiceId);
        }

        Integer trackId = input.getTrackId();
        if (null != trackId) {
            columns.add("TrackId");
            params.add(trackId);
        }

        Float unitPrice = input.getUnitPrice();
        if (null != unitPrice) {
            columns.add("UnitPrice");
            params.add(unitPrice);
        }

        Integer Quantity = input.getQuantity();
        if (null != Quantity) {
            columns.add("Quantity");
            params.add(Quantity);
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
                .map(w -> "\"InvoiceLine\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
