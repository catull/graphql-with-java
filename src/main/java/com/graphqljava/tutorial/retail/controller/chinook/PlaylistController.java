package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.Playlist;
import com.graphqljava.tutorial.retail.model.chinook.input.PlaylistInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class PlaylistController {
    private final JdbcClient jdbcClient;

    private final RowMapper<Playlist>
            rowMapper = (rs, rowNum) -> new Playlist(
            rs.getInt("PlaylistId"),
            rs.getString("Name")
    );

    public PlaylistController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Playlist> Playlists(final @Argument PlaylistInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"Playlist\"") :
                this.jdbcClient.sql("select * from \"Playlist\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
