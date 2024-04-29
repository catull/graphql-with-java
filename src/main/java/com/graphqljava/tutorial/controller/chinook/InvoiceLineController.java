package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.chinook.Invoice;
import com.graphqljava.tutorial.model.chinook.InvoiceLine;
import com.graphqljava.tutorial.model.chinook.Track;
import com.graphqljava.tutorial.model.chinook.input.InvoiceLineInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
    public class InvoiceLineController extends BaseController {

    public InvoiceLineController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"InvoiceLine\"";
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

    private static final RowMapper<InvoiceLine>
        rowMapper = (rs, rowNum) -> new InvoiceLine (
            rs.getInt("InvoiceLineId"),
            rs.getInt("InvoiceId"),
            rs.getInt("TrackId"),
            rs.getFloat("UnitPrice"),
            rs.getInt("Quantity")
        );

    private StatementSpec spec(final InvoiceLineInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "InvoiceLineId", input.getInvoiceLineId());
        extractInputParameterAndValue(columns, params, "InvoiceId", input.getInvoiceId());
        extractInputParameterAndValue(columns, params, "TrackId", input.getTrackId());
        extractInputParameterAndValue(columns, params, "UnitPrice", input.getUnitPrice());
        extractInputParameterAndValue(columns, params, "Quantity", input.getQuantity());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
