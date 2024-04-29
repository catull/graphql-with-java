package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.chinook.Album;
import com.graphqljava.tutorial.model.chinook.Genre;
import com.graphqljava.tutorial.model.chinook.InvoiceLine;
import com.graphqljava.tutorial.model.chinook.MediaType;
import com.graphqljava.tutorial.model.chinook.PlaylistTrack;
import com.graphqljava.tutorial.model.chinook.Track;
import com.graphqljava.tutorial.model.chinook.input.TrackInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class TrackController extends BaseController {

    public TrackController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"Track\"";
    }

    @QueryMapping
    public List<Track> Tracks(@Argument TrackInput input) {
        if (null == input) {
            input = new TrackInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<Track> Tracks(final Album album, @Argument TrackInput input) {
        if (null == input) {
            input = new TrackInput();
        }
        if (null == input.getAlbumId()) {
            input.setAlbumId(album.AlbumId());
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<Track> Tracks(final Genre genre, @Argument TrackInput input) {
        if (null == input) {
            input = new TrackInput();
        }
        if (null == input.getGenreId()) {
            input.setGenreId(genre.GenreId());
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Track Track(final InvoiceLine invoiceLine, @Argument TrackInput input) {
        if (null == input) {
            input = new TrackInput();
        }
        if (null == input.getTrackId()) {
            input.setTrackId(invoiceLine.TrackId());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    @SchemaMapping
    public List<Track> Tracks(final MediaType mediaType, @Argument TrackInput input) {
        if (null == input) {
            input = new TrackInput();
        }
        if (null == input.getMediaTypeId()) {
            input.setMediaTypeId(mediaType.MediaTypeId());
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Track Track(final PlaylistTrack playlistTrack, @Argument TrackInput input) {
        if (null == input) {
            input = new TrackInput();
        }
        if (null == input.getTrackId()) {
            input.setTrackId(playlistTrack.TrackId());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private static final RowMapper<Track>
        rowMapper = (rs, rowNum) -> new Track (
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

    protected StatementSpec spec(final TrackInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "TrackId", input.getTrackId());
        extractInputParameterAndValue(columns, params, "Name", input.getName());
        extractInputParameterAndValue(columns, params, "AlbumId", input.getAlbumId());
        extractInputParameterAndValue(columns, params, "MediaTypeId", input.getMediaTypeId());
        extractInputParameterAndValue(columns, params, "GenreId", input.getGenreId());
        extractInputParameterAndValue(columns, params, "Composer", input.getComposer());
        extractInputParameterAndValue(columns, params, "Milliseconds", input.getComposer());
        extractInputParameterAndValue(columns, params, "Bytes", input.getBytes());
        extractInputParameterAndValue(columns, params, "UnitPrice", input.getUnitPrice());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
