package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.retail.model.chinook.Album;
import com.graphqljava.tutorial.retail.model.chinook.Genre;
import com.graphqljava.tutorial.retail.model.chinook.InvoiceLine;
import com.graphqljava.tutorial.retail.model.chinook.MediaType;
import com.graphqljava.tutorial.retail.model.chinook.PlaylistTrack;
import com.graphqljava.tutorial.retail.model.chinook.Track;
import com.graphqljava.tutorial.retail.model.chinook.input.TrackInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class TrackController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<Track>
            rowMapper = (rs, rowNum) -> new Track(
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

        return spec(input).query(rowMapper).single();
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

        return spec(input).query(rowMapper).single();
    }

    private StatementSpec spec(final TrackInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"Track\"";

        Integer trackId = input.getTrackId();
        if (null != trackId) {
            columns.add("TrackId");
            params.add(trackId);
        }

        String name = input.getName();
        if (null != name) {
            columns.add("Name");
            params.add(name);
        }

        Integer albumId = input.getAlbumId();
        if (null != albumId) {
            columns.add("AlbumId");
            params.add(albumId);
        }

        Integer mediaTypeId = input.getMediaTypeId();
        if (null != mediaTypeId) {
            columns.add("MediaTypeId");
            params.add(mediaTypeId);
        }

        Integer genreId = input.getGenreId();
        if (null != genreId) {
            columns.add("GenreId");
            params.add(genreId);
        }

        String composer = input.getComposer();
        if (null != composer) {
            columns.add("Composer");
            params.add(composer);
        }

        Integer milliseconds = input.getMilliseconds();
        if (null != milliseconds) {
            columns.add("Milliseconds");
            params.add(milliseconds);
        }

        Integer Bytes = input.getBytes();
        if (null != Bytes) {
            columns.add("Bytes");
            params.add(Bytes);
        }

        Float unitPrice = input.getUnitPrice();
        if (null != unitPrice) {
            columns.add("UnitPrice");
            params.add(unitPrice);
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
                .map(w -> "\"Track\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }

}
