package com.graphqljava.tutorial.controller.retail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.graphqljava.tutorial.controller.BaseController;
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
public class AccountController extends BaseController {

    public AccountController(final JdbcClient jdbcClient)
    {
        super (jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"account\"";
    }

    @QueryMapping
    public List<account> accounts(@Argument AccountInput input) {
        if (null == input) {
            input = new AccountInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public CompletableFuture<account> account(final order order, @Argument AccountInput input) {
        if (null == input) {
            input = new AccountInput();
        }
        if (null == input.getId()) {
            input.setId(order.account_id());
        }

        return CompletableFuture.completedFuture(spec(input).query(rowMapper).optional().orElse(null));
    }

    private static final RowMapper<account> rowMapper =
        (rs, rowNum) -> new account (
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("created_at"),
            rs.getString("updated_at")
        );

    private StatementSpec spec(final AccountInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "id", input.getId());
        extractInputParameterAndValue(columns, params, "name", input.getName());
        extractInputParameterAndValue(columns, params, "created_at", input.getCreated_at());
        extractInputParameterAndValue(columns, params, "updated_at", input.getUpdated_at());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
