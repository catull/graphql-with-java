package com.graphqljava.tutorial.controller.chinook;

import java.util.ArrayList;
import java.util.List;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.chinook.Track;
import com.graphqljava.tutorial.model.chinook.MediaType;
import com.graphqljava.tutorial.model.chinook.input.MediaTypeInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class MediaTypeController extends BaseController {

    public MediaTypeController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"MediaType\"";
    }

    @QueryMapping
    public List<MediaType> MediaTypes(@Argument MediaTypeInput input) {
        if (null == input) {
            input = new MediaTypeInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public MediaType MediaType(final Track track, @Argument MediaTypeInput input) {
        if (null == input) {
            input = new MediaTypeInput();
        }
        if (null == input.getMediaTypeId()) {
            input.setMediaTypeId(track.MediaTypeId());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private static final RowMapper<MediaType>
        rowMapper = (rs, rowNum) -> new MediaType (
            rs.getInt("MediaTypeId"),
            rs.getString("Name")
        );

    private StatementSpec spec(final MediaTypeInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "MediaTypeId", input.getMediaTypeId());
        extractInputParameterAndValue(columns, params, "Name", input.getName());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
