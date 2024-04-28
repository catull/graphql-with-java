package com.graphqljava.tutorial.retail.controller.chinook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.graphqljava.tutorial.retail.model.chinook.Album;
import com.graphqljava.tutorial.retail.model.chinook.Artist;
import com.graphqljava.tutorial.retail.model.chinook.input.ArtistInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class ArtistController {
    private final JdbcClient jdbcClient;

    private static final RowMapper<Artist>
            rowMapper = (rs, rowNum) -> new Artist(
                    rs.getInt("ArtistId"),
                    rs.getString("Name")
    );

    public ArtistController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @QueryMapping
    public List<Artist> Artists(@Argument ArtistInput input) {
        if (null == input) {
            input = new ArtistInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Artist Artist(final Album album, @Argument ArtistInput input) {
        if (null == input) {
            input = new ArtistInput();
        }
        if (null == input.getArtistId()) {
            input.setArtistId(album.ArtistId());
        }

        return spec(input).query(rowMapper).single();
    }

    private StatementSpec spec(final ArtistInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        String select = "select * from \"Artist\"";

        Integer artistId = input.getArtistId();
        if (null != artistId) {
            columns.add("ArtistId");
            params.add(artistId);
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
                .map(w -> "\"Artist\".\"" + w + "\" = ?")
                .collect(Collectors.joining(" and "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }
}
