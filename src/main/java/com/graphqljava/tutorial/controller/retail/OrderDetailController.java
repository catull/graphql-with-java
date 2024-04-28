package com.graphqljava.tutorial.controller.retail;

import java.util.List;
import java.util.UUID;

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
public class OrderDetailController {
    private final JdbcClient jdbcClient;

    private final RowMapper<order_detail> rowMapper =
            (rs, rowNum) -> new order_detail
                    (UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("order_id")),
                    UUID.fromString(rs.getString("product_id")),
                    rs.getInt("units"),
                    rs.getString("created_at"),
                    rs.getString("updated_at"));

    public OrderDetailController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @SchemaMapping
    public List<order_detail> order_details(final order order, @Argument OrderDetailInput input) {
        UUID orderId = null != input && null != input.getOrder_id() ? input.getOrder_id() : order.id();
        int limit = null != input ? input.getLimit() : -1;
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"order_detail\" where order_id = ?").param(orderId) :
                this.jdbcClient.sql("select * from \"order_detail\" where order_id = ? limit ?").param(orderId).param(limit);
        return spec.query(this.rowMapper).list();
    }

    @SchemaMapping
    public List<order_detail> order_details(final product product, @Argument OrderDetailInput input) {
        UUID productId = null != input && null != input.getProduct_id() ? input.getProduct_id() : product.id();
        int limit = null != input ? input.getLimit() : -1;
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"order_detail\" where product_id = ?").param(productId) :
                this.jdbcClient.sql("select * from \"order_detail\" where product_id = ? limit ?").param(productId).param(limit);
        return spec.query(this.rowMapper).list();
    }

    @QueryMapping
    public List<order_detail> order_details(final @Argument OrderDetailInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"order_detail\"") :
                this.jdbcClient.sql("select * from \"order_detail\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
