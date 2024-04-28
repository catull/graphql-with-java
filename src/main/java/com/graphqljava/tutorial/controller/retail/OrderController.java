package com.graphqljava.tutorial.controller.retail;

import java.util.List;
import java.util.UUID;

import com.graphqljava.tutorial.model.retail.account;
import com.graphqljava.tutorial.model.retail.input.AccountInput;
import com.graphqljava.tutorial.model.retail.order;
import com.graphqljava.tutorial.model.retail.order_detail;
import com.graphqljava.tutorial.model.retail.input.OrderInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {
    private final JdbcClient jdbcClient;

    private final RowMapper<order>
            rowMapper = (rs, rowNum) -> new order
            (UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("account_id")),
                    rs.getString("status"),
                    rs.getString("created_at"),
                    rs.getString("updated_at"));

    public OrderController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @SchemaMapping
    public order order(final order_detail order_detail, final @Argument OrderInput input) {
        UUID orderId = null != input && null != input.getId() ? input.getId() : order_detail.order_id();
        return
                this.jdbcClient
                        .sql("select * from \"order\" where id = ? limit 1")
                        .param(orderId)
                        .query(this.rowMapper)
                        .single();
    }

    @SchemaMapping
    public List<order> orders(final account account, final @Argument AccountInput input) {
        UUID accountId = null != input && null != input.getId() ? input.getId() : account.id();
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"order\" where account_id = ?").param(accountId) :
                this.jdbcClient.sql("select * from \"order\" where account_id = ? limit ?").param(accountId).param(limit);
        return
                spec
                        .query(this.rowMapper)
                        .list();
    }

    @QueryMapping
    public List<order> orders(final @Argument OrderInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"order\"") :
                this.jdbcClient.sql("select * from \"order\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
