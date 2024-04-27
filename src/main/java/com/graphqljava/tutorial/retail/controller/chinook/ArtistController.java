package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.Artist;
import com.graphqljava.tutorial.retail.model.chinook.input.ArtistInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class ArtistController {
    private final JdbcClient jdbcClient;

    private final RowMapper<Artist>
            rowMapper = (rs, rowNum) -> new Artist(rs.getInt("ArtistId"),
            rs.getString("Name"));

    public ArtistController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Artist> Artists(final @Argument ArtistInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"Artist\"") :
                this.jdbcClient.sql("select * from \"Artist\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
