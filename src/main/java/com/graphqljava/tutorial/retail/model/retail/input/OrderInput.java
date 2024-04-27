package com.graphqljava.tutorial.retail.model.retail.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderInput {
    private UUID id;
    private UUID account_id;
    private String status;
    private String created_at;
    private String updated_at;
    private int limit;
}
