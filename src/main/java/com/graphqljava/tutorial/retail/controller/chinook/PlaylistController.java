package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.retail.model.chinook.Playlist;
import com.graphqljava.tutorial.retail.model.chinook.PlaylistTrack;
import com.graphqljava.tutorial.retail.model.chinook.input.PlaylistInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class PlaylistController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<Playlist>
            rowMapper = (rs, rowNum) -> new Playlist(
            rs.getInt("PlaylistId"),
            rs.getString("Name")
    );

    public PlaylistController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Playlist> Playlists(@Argument PlaylistInput input) {
        if (null == input) {
            input = new PlaylistInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Playlist Playlist(final PlaylistTrack playlistTrack, @Argument PlaylistInput input) {
        if (null == input) {
            input = new PlaylistInput();
        }
        if (null == input.getPlaylistId()) {
            input.setPlaylistId(playlistTrack.PlaylistId());
        }

        return spec(input).query(rowMapper).single();
    }

    private StatementSpec spec(final PlaylistInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"Playlist\"";

        Integer PlaylistId = input.getPlaylistId();
        if (null != PlaylistId) {
            columns.add("PlaylistId");
            params.add(PlaylistId);
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
                .map(w -> "\"Playlist\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
