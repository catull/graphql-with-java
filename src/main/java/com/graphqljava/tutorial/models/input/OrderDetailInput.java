package com.graphqljava.tutorial.models.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderDetailInput {
    private UUID id;
    private UUID order_id;
    private UUID product_id;
    private Integer units;
    private String created_at;
    private String updated_at;
    private int limit;
}
