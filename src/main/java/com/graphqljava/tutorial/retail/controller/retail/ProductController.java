package com.graphqljava.tutorial.retail.controller.retail;

import java.util.List;
import java.util.UUID;

import com.graphqljava.tutorial.retail.model.retail.order_detail;
import com.graphqljava.tutorial.retail.model.retail.product;
import com.graphqljava.tutorial.retail.model.retail.input.ProductInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;


@Controller
public class ProductController {
    private final JdbcClient jdbcClient;

    private final RowMapper<product>
            rowMapper = (rs, rowNum) -> new product
            (UUID.fromString(rs.getString("id")),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("created_at"),
                    rs.getString("updated_at"));

    public ProductController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @SchemaMapping
    public product product(final order_detail order_detail, final @Argument ProductInput input) {
        UUID id = null != input && null != input.getId() ? input.getId() : order_detail.product_id();
        return
                this.jdbcClient
                        .sql("select * from \"product\" where id = ?")
                        .param(id)
                        .query(this.rowMapper)
                        .single();
    }

    @QueryMapping
    public List<product> products(final @Argument ProductInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"product\"") :
                this.jdbcClient.sql("select * from \"product\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
