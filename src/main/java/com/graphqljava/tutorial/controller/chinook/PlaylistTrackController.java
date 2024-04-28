package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.model.chinook.PlaylistTrack;
import com.graphqljava.tutorial.model.chinook.Track;
import com.graphqljava.tutorial.model.chinook.Playlist;
import com.graphqljava.tutorial.model.chinook.input.PlaylistTrackInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class PlaylistTrackController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<PlaylistTrack>
            rowMapper = (rs, rowNum) -> new PlaylistTrack(
            rs.getInt("PlaylistId"),
            rs.getInt("TrackId")
    );

    public PlaylistTrackController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<PlaylistTrack> PlaylistTracks(@Argument PlaylistTrackInput input) {
        if (null == input) {
            input = new PlaylistTrackInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<PlaylistTrack> PlaylistTracks(final Playlist playlist, @Argument PlaylistTrackInput input) {
        if (null == input) {
            input = new PlaylistTrackInput();
        }
        if (null == input.getPlaylistId()) {
            input.setPlaylistId(playlist.PlaylistId());
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<PlaylistTrack> PlaylistTracks(final Track track, @Argument PlaylistTrackInput input) {
        if (null == input) {
            input = new PlaylistTrackInput();
        }
        if (null == input.getTrackId()) {
            input.setTrackId(track.TrackId());
        }

        return spec(input).query(rowMapper).list();
    }

    private StatementSpec spec(final PlaylistTrackInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"PlaylistTrack\"";

        Integer PlaylistId = input.getPlaylistId();
        if (null != PlaylistId) {
            columns.add("PlaylistId");
            params.add(PlaylistId);
        }

        Integer trackId = input.getTrackId();
        if (null != trackId) {
            columns.add("TrackId");
            params.add(trackId);
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
                .map(w -> "\"PlaylistTrack\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
