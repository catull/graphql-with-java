package com.graphqljava.tutorial.models.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegionInput {
    private String value;
    private String description;
    private int limit;
}
