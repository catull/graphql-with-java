package com.graphqljava.tutorial.controller;

import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseController {

    protected final JdbcClient jdbcClient;

    public BaseController(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public abstract String getTablePrefix();

    protected JdbcClient.StatementSpec createJdbcSpec(
        final List<String> columns,
        final List<Object> params,
        final int limit)
    {
        String select = "SELECT * FROM " + getTablePrefix();

        boolean withLimit = limit > 0;
        if (!withLimit && params.isEmpty()) {
            return this.jdbcClient.sql(select);
        }

        if (withLimit) {
            params.add(limit);
        }

        if (withLimit && 1 == params.size()) {
            select += " limit ?";
            return this.jdbcClient.sql(select).param(limit);
        }

        select += " WHERE " + columns.stream()
                .map(w -> getTablePrefix() + ".\"" + w + "\" = ?")
                .collect(Collectors.joining(" AND "));

        if (withLimit) {
            select += " limit ?";
        }

        return this.jdbcClient.sql(select).params(params);
    }

    protected void extractInputParameterAndValue(
            final List<String> columns,
            final List<Object> params,
            final String columName,
            final Object value)
    {
        if (null == value) {
            return;
        }

        columns.add(columName);
        params.add(value);
    }
}
