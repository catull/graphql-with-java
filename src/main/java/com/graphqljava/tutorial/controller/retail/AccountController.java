package com.graphqljava.tutorial.controller.retail;

import java.util.List;
import java.util.UUID;

import com.graphqljava.tutorial.model.retail.account;
import com.graphqljava.tutorial.model.retail.order;
import com.graphqljava.tutorial.model.retail.input.AccountInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

@Controller
public class AccountController {
    private final JdbcClient jdbcClient;

    private final RowMapper<account> rowMapper =
            (rs, rowNum) -> new account
                (UUID.fromString(
                rs.getString("ID")),
                rs.getString("name"),
                rs.getString("created_at"),
                rs.getString("updated_at"));

    public AccountController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @SchemaMapping
    public account account(final order order, final @Argument AccountInput input) {
        UUID accountId = null != input && null != input.getId() ? input.getId() : order.account_id();

        return this.jdbcClient
            .sql("select * from \"account\" where id = ?")
            .param(accountId)
            .query(this.rowMapper)
            .optional()
            .orElse(null);
    }

    @QueryMapping
    public List<account> accounts(final @Argument AccountInput input) {
        int limit = null == input ? 0 : input.getLimit();
        StatementSpec
                spec = limit < 1 ?
                this.jdbcClient.sql("select * from \"account\"") :
                this.jdbcClient.sql("select * from \"account\" limit ?").param(limit);
        return spec.query(this.rowMapper).list();
    }
}
