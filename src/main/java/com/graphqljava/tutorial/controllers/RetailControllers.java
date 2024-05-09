package com.graphqljava.tutorial.controllers;

import com.graphqljava.tutorial.models.RetailModels;
import com.graphqljava.tutorial.models.input.AccountInput;
import com.graphqljava.tutorial.models.input.OrderDetailInput;
import com.graphqljava.tutorial.models.input.OrderInput;
import com.graphqljava.tutorial.models.input.ProductInput;
import com.graphqljava.tutorial.models.input.RegionInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RetailControllers {
    @Controller
    public static class AccountController extends BaseController {

        public AccountController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"account\"";
        }

        @QueryMapping
        public List<RetailModels.account> accounts(@Argument AccountInput input) {
            if (null == input) {
                input = new AccountInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public CompletableFuture<RetailModels.account> account(final RetailModels.order order, @Argument AccountInput input) {
            if (null == input) {
                input = new AccountInput();
            }
            if (null == input.getId()) {
                input.setId(order.account_id());
            }

            return CompletableFuture.completedFuture(spec(input).query(rowMapper).optional().orElse(null));
        }

        private static final RowMapper<RetailModels.account> rowMapper =
                (rs, rowNum) -> new RetailModels.account(
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

    @Controller
    public static class OrderController extends BaseController {

        public OrderController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"order\"";
        }

        @QueryMapping
        public List<RetailModels.order> orders(@Argument OrderInput input) {
            if (null == input) {
                input = new OrderInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public RetailModels.order order(final RetailModels.order_detail order_detail, @Argument OrderInput input) {
            if (null == input) {
                input = new OrderInput();
            }
            if (null == input.getId()) {
                input.setId(order_detail.order_id());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        @SchemaMapping
        public List<RetailModels.order> orders(final RetailModels.account account, @Argument OrderInput input) {
            if (null == input) {
                input = new OrderInput();
            }
            if (null == input.getId()) {
                input.setId(account.id());
            }

            return spec(input).query(rowMapper).list();
        }

        @BatchMapping
        public Map<RetailModels.account, List<RetailModels.order>> ordersMulti(final List<RetailModels.account> accounts) {
            Map<UUID, RetailModels.account> accountMap = accounts.stream().collect(Collectors.toMap(RetailModels.account::id, Function.identity()));
            Set<UUID> ids = accountMap.keySet();
            String sql = "SELECT * FROM \"order\" WHERE \"account_id\" IN (?)";
            Map<UUID, List<RetailModels.order>> orders = this.jdbcClient
                    .sql(sql)
                    .param(ids)
                    .query(rowMapper)
                    .stream()
                    .collect(Collectors.groupingBy(RetailModels.order::account_id));
            return orders
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(entry -> accountMap.get(entry.getKey()), Map.Entry::getValue));
        }

        private static final RowMapper<RetailModels.order>
                rowMapper = (rs, rowNum) -> new RetailModels.order(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("account_id")),
                rs.getString("status"),
                rs.getString("region"),
                rs.getString("created_at"),
                rs.getString("updated_at")
        );

        private StatementSpec spec(final OrderInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "id", input.getId());
            extractInputParameterAndValue(columns, params, "account_id", input.getAccount_id());

            // TODO: Still gives a runtime exception about invalid SQL Grammar!!
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

    @Controller
    public static class OrderDetailController extends BaseController {

        public OrderDetailController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"order_detail\"";
        }

        @QueryMapping
        public List<RetailModels.order_detail> order_details(@Argument OrderDetailInput input) {
            if (null == input) {
                input = new OrderDetailInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<RetailModels.order_detail> order_details(final RetailModels.order order, @Argument OrderDetailInput input) {
            if (null == input) {
                input = new OrderDetailInput();
            }
            if (null == input.getOrder_id()) {
                input.setOrder_id(order.id());
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<RetailModels.order_detail> order_details(final RetailModels.product product, @Argument OrderDetailInput input) {
            if (null == input) {
                input = new OrderDetailInput();
            }
            if (null == input.getProduct_id()) {
                input.setProduct_id(product.id());
            }

            return spec(input).query(rowMapper).list();
        }

        private static final RowMapper<RetailModels.order_detail> rowMapper =
                (rs, rowNum) -> new RetailModels.order_detail(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("order_id")),
                        UUID.fromString(rs.getString("product_id")),
                        rs.getInt("units"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );

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

    @Controller
    public static class ProductController extends BaseController {

        public ProductController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"product\"";
        }

        @QueryMapping
        public List<RetailModels.product> products(@Argument ProductInput input) {
            if (null == input) {
                input = new ProductInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public RetailModels.product product(final RetailModels.order_detail order_detail, @Argument ProductInput input) {
            if (null == input) {
                input = new ProductInput();
            }
            if (null == input.getId()) {
                input.setId(order_detail.product_id());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<RetailModels.product>
                rowMapper = (rs, rowNum) -> new RetailModels.product(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("created_at"),
                rs.getString("updated_at")
        );

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

    @Controller
    public static class RegionController extends BaseController {

        public RegionController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"region\"";
        }

        @QueryMapping
        public List<RetailModels.region> regions(@Argument RegionInput input) {
            if (null == input) {
                input = new RegionInput();
            }

            return spec(input).query(rowMapper).list();
        }

        private static final RowMapper<RetailModels.region>
                rowMapper = (rs, rowNum) -> new RetailModels.region(
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

}
