package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.Album;
import com.graphqljava.tutorial.retail.model.chinook.input.AlbumInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class AlbumController {
    private final JdbcClient jdbcClient;

    private final RowMapper<Album>
            rowMapper = (rs, rowNum) -> new Album(
            rs.getInt("AlbumId"),
            rs.getString("Title"),
            rs.getInt("ArtistId")
    );

    public AlbumController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Album> Albums(final @Argument AlbumInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"Album\"") :
                this.jdbcClient.sql("select * from \"Album\" limit ?").param(limit);

        return spec.query(this.rowMapper).list();
    }
}
