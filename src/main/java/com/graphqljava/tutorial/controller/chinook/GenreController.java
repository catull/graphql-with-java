package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.model.chinook.Track;
import com.graphqljava.tutorial.model.chinook.Genre;
import com.graphqljava.tutorial.model.chinook.input.GenreInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class GenreController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<Genre>
            rowMapper = (rs, rowNum) -> new Genre(
            rs.getInt("GenreId"),
            rs.getString("Name")
    );

    public GenreController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Genre> Genres(@Argument GenreInput input) {
        if (null == input) {
            input = new GenreInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Genre Genre(final Track track, @Argument GenreInput input) {
        if (null == input) {
            input = new GenreInput();
        }
        if (null == input.getGenreId()) {
            input.setGenreId(track.GenreId());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private StatementSpec spec(final GenreInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"Genre\"";

        Integer GenreId = input.getGenreId();
        if (null != GenreId) {
            columns.add("GenreId");
            params.add(GenreId);
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
                .map(w -> "\"Genre\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
