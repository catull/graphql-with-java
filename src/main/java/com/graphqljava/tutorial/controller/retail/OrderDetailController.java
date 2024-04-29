package com.graphqljava.tutorial.controller.retail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.retail.order;
import com.graphqljava.tutorial.model.retail.order_detail;
import com.graphqljava.tutorial.model.retail.product;
import com.graphqljava.tutorial.model.retail.input.OrderDetailInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class OrderDetailController extends BaseController {

    private static final RowMapper<order_detail> rowMapper =
        (rs, rowNum) -> new order_detail (
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("order_id")),
            UUID.fromString(rs.getString("product_id")),
            rs.getInt("units"),
            rs.getString("created_at"),
            rs.getString("updated_at")
        );

    public OrderDetailController(final JdbcClient jdbcClient) {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"order_detail\"";
    }

    @QueryMapping
    public List<order_detail> order_details(@Argument OrderDetailInput input) {
        if (null == input) {
            input = new OrderDetailInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<order_detail> order_details(final order order, @Argument OrderDetailInput input) {
        if (null == input) {
            input = new OrderDetailInput();
        }
        if (null == input.getOrder_id()) {
            input.setOrder_id(order.id());
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public List<order_detail> order_details(final product product, @Argument OrderDetailInput input) {
        if (null == input) {
            input = new OrderDetailInput();
        }
        if (null == input.getProduct_id()) {
            input.setProduct_id(product.id());
        }

        return spec(input).query(rowMapper).list();
    }

    private StatementSpec spec(final OrderDetailInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "id", input.getId());
        extractInputParameterAndValue(columns, params, "order_id", input.getOrder_id());
        extractInputParameterAndValue(columns, params, "product_id", input.getProduct_id());
        extractInputParameterAndValue(columns, params, "units", input.getUnits());
        extractInputParameterAndValue(columns, params, "created_at", input.getCreated_at());
        extractInputParameterAndValue(columns, params, "updated_at", input.getUpdated_at());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
