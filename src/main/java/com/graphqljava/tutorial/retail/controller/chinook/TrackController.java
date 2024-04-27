package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.List;

import com.graphqljava.tutorial.retail.model.chinook.Track;
import com.graphqljava.tutorial.retail.model.chinook.input.TrackInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class TrackController {
    private final JdbcClient jdbcClient;

    private final RowMapper<Track>
            trackMapper = (rs, rowNum) -> new Track(
            rs.getInt("TrackId"),
            rs.getString("Name"),
            rs.getInt("AlbumId"),
            rs.getInt("GenreId"),
            rs.getInt("MediaTypeId"),
            rs.getString("Composer"),
            rs.getInt("Milliseconds"),
            rs.getInt("Bytes"),
            rs.getFloat("UnitPrice")
    );

    public TrackController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Track> Tracks(final @Argument TrackInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"Track\"") :
                this.jdbcClient.sql("select * from \"Track\" limit ?").param(limit);
        return spec.query(this.trackMapper).list();
    }
}
