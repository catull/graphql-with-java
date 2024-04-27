package com.graphqljava.tutorial.retail.model.retail.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductInput {
    private UUID id;
    private String name;
    private Integer price;
    private String created_at;
    private String updated_at;
    private int limit;
}
