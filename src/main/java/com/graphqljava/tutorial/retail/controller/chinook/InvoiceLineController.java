package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.InvoiceLine;
import com.graphqljava.tutorial.retail.model.chinook.input.InvoiceLineInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
    public class InvoiceLineController {
        private final JdbcClient jdbcClient;

    private final RowMapper<InvoiceLine>
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
    public List<InvoiceLine> InvoiceLines(final @Argument InvoiceLineInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"InvoiceLine\"") :
                this.jdbcClient.sql("select * from \"InvoiceLine\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
