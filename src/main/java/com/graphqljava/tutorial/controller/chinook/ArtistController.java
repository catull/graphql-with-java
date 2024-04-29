package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.chinook.Album;
import com.graphqljava.tutorial.model.chinook.Artist;
import com.graphqljava.tutorial.model.chinook.input.ArtistInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class ArtistController extends BaseController {

    public ArtistController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"Artist\"";
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

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private static final RowMapper<Artist>
        rowMapper = (rs, rowNum) -> new Artist (
            rs.getInt("ArtistId"),
            rs.getString("Name")
        );

    private StatementSpec spec(final ArtistInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "ArtistId", input.getArtistId());
        extractInputParameterAndValue(columns, params, "Name", input.getName());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
