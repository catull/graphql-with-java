package com.graphqljava.tutorial.controller.retail;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.retail.input.RegionInput;
import com.graphqljava.tutorial.model.retail.region;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegionController extends BaseController {

    public RegionController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"region\"";
    }

    @QueryMapping
    public List<region> regions(@Argument RegionInput input) {
        if (null == input) {
            input = new RegionInput();
        }

        return spec(input).query(rowMapper).list();
    }

    private static final RowMapper<region>
        rowMapper = (rs, rowNum) -> new region (
            rs.getString("value"),
            rs.getString("description")
        );

    private StatementSpec spec(final RegionInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "value", input.getValue());
        extractInputParameterAndValue(columns, params, "description", input.getDescription());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
