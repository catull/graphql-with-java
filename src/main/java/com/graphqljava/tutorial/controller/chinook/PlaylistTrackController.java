package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
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
public class PlaylistTrackController extends BaseController {

    public PlaylistTrackController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"PlaylistTrack\"";
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

    private static final RowMapper<PlaylistTrack>
        rowMapper = (rs, rowNum) -> new PlaylistTrack (
            rs.getInt("PlaylistId"),
            rs.getInt("TrackId")
        );

    private StatementSpec spec(final PlaylistTrackInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "PlaylistId", input.getPlaylistId());
        extractInputParameterAndValue(columns, params, "TrackId", input.getTrackId());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
