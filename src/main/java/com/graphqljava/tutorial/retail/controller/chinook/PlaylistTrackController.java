package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.PlaylistTrack;
import com.graphqljava.tutorial.retail.model.chinook.input.PlaylistTrackInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class PlaylistTrackController {
    private final JdbcClient jdbcClient;

    private final RowMapper<PlaylistTrack>
            rowMapper = (rs, rowNum) -> new PlaylistTrack(
            rs.getInt("PlaylistId"),
            rs.getInt("TrackId")
    );

    public PlaylistTrackController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<PlaylistTrack> PlaylistTracks(final @Argument PlaylistTrackInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"PlaylistTrack\"") :
                this.jdbcClient.sql("select * from \"PlaylistTrack\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
