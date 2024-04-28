package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.retail.model.chinook.Album;
import com.graphqljava.tutorial.retail.model.chinook.Artist;
import com.graphqljava.tutorial.retail.model.chinook.Track;
import com.graphqljava.tutorial.retail.model.chinook.input.AlbumInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class AlbumController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<Album>
            rowMapper = (rs, rowNum) -> new Album(
            rs.getInt("AlbumId"),
            rs.getString("Title"),
            rs.getInt("ArtistId")
    );

    public AlbumController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
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

        return spec(input).query(rowMapper).single();
    }

    private StatementSpec spec(final AlbumInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"Album\"";

        Integer albumId = input.getAlbumId();
        if (null != albumId) {
            columns.add("AlbumId");
            params.add(albumId);
        }

        Integer artistId = input.getArtistId();
        if (null != artistId) {
            columns.add("ArtistId");
            params.add(artistId);
        }

        String title = input.getTitle();
        if (null != title) {
            columns.add("Title");
            params.add(title);
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
                .map(w -> "\"Album\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
