package com.graphqljava.tutorial.model.retail.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountInput {
    private UUID id;
    private String name;
    private String created_at;
    private String updated_at;
    private int limit;
}
