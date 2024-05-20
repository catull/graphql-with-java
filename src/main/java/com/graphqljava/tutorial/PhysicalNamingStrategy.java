package com.graphqljava.tutorial;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.HashMap;

public class PhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {
    private final static HashMap<String, String> TABLE_NAME_MAP = new HashMap<>() {{
        // Retail
        //put("account", "account");
        //put("order_detail", "order_detail");
        //put("order", "order");
        //put("product", "product");
        //put("region", "region");

        // Retail
        put("album", "Album");
        put("artist", "Artist");
        put("customer", "Customer");
        put("employee", "Employee");
        put("genre", "Genre");
        put("invoice", "Invoice");
        put("invoiceline", "InvoiceLine");
        put("mediatype", "MediaType");
        put("playlist", "Playlist");
        put("playlisttrack", "PlaylistTrack");
        put("track", "Track");
    }};

    @Override
    public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
        String mappedName = TABLE_NAME_MAP.get(name.getText().toLowerCase());

        if (mappedName == null) {
            return name;
        }

        return Identifier.toIdentifier(mappedName, true);
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
        if (name.isQuoted()) {
            return name;
        }

        return Identifier.toIdentifier(name.getText(), true);
    }
}
