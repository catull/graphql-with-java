package com.graphqljava.tutorial.models.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderInput {
    private UUID id;
    private UUID account_id;
    private String status;
    private String region;
    private String created_at;
    private String updated_at;
    private int limit;
}
