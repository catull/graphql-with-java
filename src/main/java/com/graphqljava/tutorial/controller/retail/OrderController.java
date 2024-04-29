package com.graphqljava.tutorial.controller.retail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.graphqljava.tutorial.controller.BaseController;
import com.graphqljava.tutorial.model.retail.account;
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
public class OrderController extends BaseController {

    private static final RowMapper<order>
        rowMapper = (rs, rowNum) -> new order(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("account_id")),
            rs.getString("status"),
            rs.getString("region"),
            rs.getString("created_at"),
            rs.getString("updated_at")
        );

    public OrderController(final JdbcClient jdbcClient) {
        super(jdbcClient);
    }

    @Override
    public String getTablePrefix() {
        return "\"order\"";
    }

    @QueryMapping
    public List<order> orders(@Argument OrderInput input) {
        if (null == input) {
            input = new OrderInput();
        }

        return spec(input).query(rowMapper).list();
    }

    @SchemaMapping
    public order order(final order_detail order_detail, @Argument OrderInput input) {
        if (null == input) {
            input = new OrderInput();
        }
        if (null == input.getId()) {
            input.setId(order_detail.order_id());
        }

        return spec(input).query(rowMapper).optional().orElse(null);
    }

    @SchemaMapping
    public List<order> orders(final account account, @Argument OrderInput input) {
        if (null == input) {
            input = new OrderInput();
        }
        if (null == input.getId()) {
            input.setId(account.id());
        }

        return spec(input).query(rowMapper).list();
    }

    private StatementSpec spec(final OrderInput input) {
        List<String> columns = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        extractInputParameterAndValue(columns, params, "id", input.getId());
        extractInputParameterAndValue(columns, params, "account_id", input.getAccount_id());

        String status = input.getStatus();
        if (null != status) {
            String statusValue = "any (enum_range('" + status + "'::public.status, '" + status + "'::public.status))";
            extractInputParameterAndValue(columns, params, "status", statusValue);
        }

        extractInputParameterAndValue(columns, params, "region", input.getRegion());
        extractInputParameterAndValue(columns, params, "created_at", input.getCreated_at());
        extractInputParameterAndValue(columns, params, "updated_at", input.getUpdated_at());

        return createJdbcSpec(columns, params, input.getLimit());
    }
}
