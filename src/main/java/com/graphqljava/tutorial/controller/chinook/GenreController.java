package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.chinook.Track;
import com.graphqljava.tutorial.model.chinook.Genre;
import com.graphqljava.tutorial.model.chinook.input.GenreInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class GenreController extends BaseController {

    public GenreController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"Genre\"";
    }

    @QueryMapping
    public List<Genre> Genres(@Argument GenreInput input) {
        if (null == input) {
            input = new GenreInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public Genre Genre(final Track track, @Argument GenreInput input) {
        if (null == input) {
            input = new GenreInput();
        }
        if (null == input.getGenreId()) {
            input.setGenreId(track.GenreId());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private static final RowMapper<Genre>
        rowMapper = (rs, rowNum) -> new Genre(
            rs.getInt("GenreId"),
            rs.getString("Name")
        );

    private StatementSpec spec(final GenreInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "GenreId", input.getGenreId());
        extractInputParameterAndValue(columns, params, "Name", input.getName());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
