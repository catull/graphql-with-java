package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.MediaType;
import com.graphqljava.tutorial.retail.model.chinook.input.MediaTypeInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class MediaTypeController {
    private final JdbcClient jdbcClient;

    private final RowMapper<MediaType>
            rowMapper = (rs, rowNum) -> new MediaType(
            rs.getInt("MediaTypeId"),
            rs.getString("Name")
    );

    public MediaTypeController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<MediaType> MediaTypes(final @Argument MediaTypeInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"MediaType\"") :
                this.jdbcClient.sql("select * from \"MediaType\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
