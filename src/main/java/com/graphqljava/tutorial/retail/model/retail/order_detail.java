package com.graphqljava.tutorial.retail.model.retail;

import java.util.UUID;

public record order_detail(
    UUID id,
    UUID order_id,
    UUID product_id,
    Integer units,
    String created_at,
    String updated_at
) {
}
