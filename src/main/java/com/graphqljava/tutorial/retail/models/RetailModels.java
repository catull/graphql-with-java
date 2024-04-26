package com.graphqljava.tutorial.retail.models;

import java.util.UUID;

public class RetailModels {
    public
    record account
            (UUID id,
             String name,
             String created_at,
             String updated_at) {
    }

    public
    record order_detail
            (UUID id,
             UUID order_id,
             UUID product_id,
             Integer units,
             String created_at,
             String updated_at) {
    }

    public
    record order
            (UUID id,
             UUID account_id,
             String status,
             String created_at,
             String updated_at) {
    }

    public
    record product
            (UUID id,
             String name,
             Integer price,
             String created_at,
             String updated_at) {
    }
}
