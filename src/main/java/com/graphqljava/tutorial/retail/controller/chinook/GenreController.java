package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.Genre;
import com.graphqljava.tutorial.retail.model.chinook.input.GenreInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class GenreController {
    private final JdbcClient jdbcClient;

    private final RowMapper<Genre>
            rowMapper = (rs, rowNum) -> new Genre(
            rs.getInt("GenreId"),
            rs.getString("Name")
    );

    public GenreController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Genre> Genres(final @Argument GenreInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"Genre\"") :
                this.jdbcClient.sql("select * from \"Genre\" limit ?").param(limit);
        return spec .query(this.rowMapper).list();
    }
}
