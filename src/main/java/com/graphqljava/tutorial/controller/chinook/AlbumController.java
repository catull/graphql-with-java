package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.chinook.Album;
import com.graphqljava.tutorial.model.chinook.Track;
import com.graphqljava.tutorial.model.chinook.Artist;
import com.graphqljava.tutorial.model.chinook.input.AlbumInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class AlbumController extends BaseController {

    public AlbumController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"Album\"";
    }

    @QueryMapping
    public List<Album> Albums(@Argument AlbumInput input) {
        if (null == input) {
            input = new AlbumInput();
        }
        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<Album> Albums(final Artist artist, @Argument AlbumInput input) {
        if (null == input) {
            input = new AlbumInput();
        }
        if (null == input.getAlbumId()) {
            input.setAlbumId(artist.ArtistId());
        }
        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Album Album(final Track track, @Argument AlbumInput input) {
        if (null == input) {
            input = new AlbumInput();
        }
        if (null == input.getAlbumId()) {
            input.setAlbumId(track.AlbumId());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private static final RowMapper<Album>
        rowMapper = (rs, rowNum) -> new Album (
            rs.getInt("AlbumId"),
            rs.getString("Title"),
            rs.getInt("ArtistId")
        );

    private StatementSpec spec(final AlbumInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "AlbumId", input.getAlbumId());
        extractInputParameterAndValue(columns, params, "Title", input.getTitle());
        extractInputParameterAndValue(columns, params, "ArtistId", input.getArtistId());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
