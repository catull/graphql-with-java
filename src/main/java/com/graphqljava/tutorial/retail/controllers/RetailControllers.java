package com.graphqljava.tutorial.retail.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.graphql.data.ArgumentValue;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

import com.graphqljava.tutorial.retail.models.RetailModels.account;
import com.graphqljava.tutorial.retail.models.RetailModels.order;
import com.graphqljava.tutorial.retail.models.RetailModels.order_detail;
import com.graphqljava.tutorial.retail.models.RetailModels.product;

public class RetailControllers {
    @Controller
    public static class AccountController {
        private JdbcClient jdbcClient;
        private final RowMapper<account>
                accountMapper = (rs, rowNum) -> new account
                (UUID.fromString(rs.getString("ID")),
                        rs.getString("name"),
                        rs.getString("created_at"),
                        rs.getString("updated_at"));

        @SchemaMapping
        account
        account(order order) {
            return
                    jdbcClient
                            .sql("select * from account where id = ? limit 1")
                            .param(order.account_id())
                            .query(accountMapper)
                            .optional()
                            .get();
        }

        @QueryMapping
        List<account>
        account(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from account") :
                    jdbcClient.sql("select * from account limit ?").param(limit.value());
            return
                    spec
                            .query(accountMapper)
                            .list();
        }

        @QueryMapping
        account
        account_by_pk(@Argument String id) {
            return
                    jdbcClient
                            .sql("select * from account where id = ? limit 1")
                            .param(UUID.fromString(id))
                            .query(accountMapper)
                            .optional()
                            .get();
        }
    }

    @Controller
    public static class OrderController {
        private JdbcClient jdbcClient;
        private final RowMapper<order>
                orderMapper = (rs, rowNum) -> new order
                (UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("account_id")),
                        rs.getString("status"),
                        rs.getString("created_at"),
                        rs.getString("updated_at"));

        @SchemaMapping
        order
        order(order_detail order_detail) {
            return
                    jdbcClient
                            .sql("select * from \"order\" where id = ? limit 1")
                            .param(order_detail.order_id())
                            .query(orderMapper)
                            .optional()
                            .get();
        }

        @SchemaMapping
        List<order>
        orders(account account, @Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"order\" where account_id = ?").param(account.id()) :
                    jdbcClient.sql("select * from \"order\" where account_id = ? limit ?").param(account.id()).param(limit.value());
            return
                    spec
                            .query(orderMapper)
                            .list();
        }

        @QueryMapping
        List<order>
        order(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"order\"") :
                    jdbcClient.sql("select * from \"order\" limit ?").param(limit.value());
            return
                    spec
                            .query(orderMapper)
                            .list();
        }

        @QueryMapping
        order
        order_by_pk(@Argument String id) {
            return
                    jdbcClient
                            .sql("select * from \"order\" where id = ? limit 1")
                            .param(UUID.fromString(id))
                            .query(orderMapper)
                            .optional()
                            .get();
        }
    }

    @Controller
    public static class OrderDetailController {
        private JdbcClient jdbcClient;
        private final RowMapper<order_detail>
                orderDetailMapper = (rs, rowNum) -> new order_detail
                (UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("order_id")),
                        UUID.fromString(rs.getString("product_id")),
                        rs.getInt("units"),
                        rs.getString("created_at"),
                        rs.getString("updated_at"));

        @SchemaMapping
        List<order_detail>
        order_details(order order, @Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from order_detail where order_id = ?").param(order.id()) :
                    jdbcClient.sql("select * from order_detail where order_id = ? limit ?").param(order.id()).param(limit.value());
            return
                    spec
                            .query(orderDetailMapper)
                            .list();
        }

        @SchemaMapping
        List<order_detail>
        order_details(product product, @Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from order_detail where product_id = ?").param(product.id()) :
                    jdbcClient.sql("select * from order_detail where product_id = ? limit ?").param(product.id()).param(limit.value());
            return
                    spec
                            .query(orderDetailMapper)
                            .list();
        }

        @QueryMapping
        List<order_detail>
        order_detail(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from order_detail") :
                    jdbcClient.sql("select * from order_detail limit ?").param(limit.value());
            return
                    spec
                            .query(orderDetailMapper)
                            .list();
        }

        @QueryMapping
        order_detail
        order_detail_by_pk(@Argument String id) {
            return
                    jdbcClient
                            .sql("select * from order_detail where id = ? limit 1")
                            .param(UUID.fromString(id))
                            .query(orderDetailMapper)
                            .optional()
                            .get();
        }
    }

    @Controller
    public static class ProductController {
        private JdbcClient jdbcClient;
        private final RowMapper<product>
                productMapper = (rs, rowNum) -> new product
                (UUID.fromString(rs.getString("id")),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("created_at"),
                        rs.getString("updated_at"));

        @SchemaMapping
        product
        product(order_detail order_detail) {
            return
                    jdbcClient
                            .sql("select * from product where id = ? limit 1")
                            .param(order_detail.product_id())
                            .query(productMapper)
                            .optional()
                            .get();
        }

        @QueryMapping
        List<product>
        product(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from product") :
                    jdbcClient.sql("select * from product limit ?").param(limit.value());
            return
                    spec
                            .query(productMapper)
                            .list();
        }

        @QueryMapping
        product
        product_by_pk(@Argument String id) {
            return
                    jdbcClient
                            .sql("select * from product where id = ? limit 1")
                            .param(UUID.fromString(id))
                            .query(productMapper)
                            .optional()
                            .get();
        }
    }
}
