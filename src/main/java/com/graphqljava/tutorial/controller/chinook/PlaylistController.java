package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.chinook.Playlist;
import com.graphqljava.tutorial.model.chinook.PlaylistTrack;
import com.graphqljava.tutorial.model.chinook.input.PlaylistInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class PlaylistController extends BaseController {

    public PlaylistController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"Playlist\"";
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

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private static final RowMapper<Playlist>
        rowMapper = (rs, rowNum) -> new Playlist (
            rs.getInt("PlaylistId"),
            rs.getString("Name")
    );

    private StatementSpec spec(final PlaylistInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "PlaylistId", input.getPlaylistId());
        extractInputParameterAndValue(columns, params, "Name", input.getName());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
