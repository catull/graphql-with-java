package com.graphqljava.tutorial.controller.retail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.retail.order_detail;
import com.graphqljava.tutorial.model.retail.product;
import com.graphqljava.tutorial.model.retail.input.ProductInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController extends BaseController {

    private static final RowMapper<product>
        rowMapper = (rs, rowNum) -> new product (
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("created_at"),
            rs.getString("updated_at")
    );

    public ProductController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"product\"";
    }

    @QueryMapping
    public List<product> products(@Argument ProductInput input) {
        if (null == input) {
            input = new ProductInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public product product(final order_detail order_detail, @Argument ProductInput input) {
        if (null == input) {
            input = new ProductInput();
        }
        if (null == input.getId()) {
            input.setId(order_detail.product_id());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    private StatementSpec spec(final ProductInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "id", input.getId());
        extractInputParameterAndValue(columns, params, "name", input.getName());
        extractInputParameterAndValue(columns, params, "price", input.getPrice());
        extractInputParameterAndValue(columns, params, "created_at", input.getCreated_at());
        extractInputParameterAndValue(columns, params, "updated_at", input.getUpdated_at());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
