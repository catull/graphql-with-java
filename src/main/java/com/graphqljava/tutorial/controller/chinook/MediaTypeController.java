package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.model.chinook.Track;
import com.graphqljava.tutorial.model.chinook.MediaType;
import com.graphqljava.tutorial.model.chinook.input.MediaTypeInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class MediaTypeController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<MediaType>
            rowMapper = (rs, rowNum) -> new MediaType(
            rs.getInt("MediaTypeId"),
            rs.getString("Name")
    );

    public MediaTypeController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<MediaType> MediaTypes(@Argument MediaTypeInput input) {
        if (null == input) {
            input = new MediaTypeInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public MediaType MediaType(final Track track, @Argument MediaTypeInput input) {
        if (null == input) {
            input = new MediaTypeInput();
        }
        if (null == input.getMediaTypeId()) {
            input.setMediaTypeId(track.MediaTypeId());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private StatementSpec spec(final MediaTypeInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"MediaType\"";

        Integer mediaTypeId = input.getMediaTypeId();
        if (null != mediaTypeId) {
            columns.add("MediaTypeId");
            params.add(mediaTypeId);
        }

        String name = input.getName();
        if (null != name) {
            columns.add("Name");
            params.add(name);
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
                .map(w -> "\"MediaType\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
