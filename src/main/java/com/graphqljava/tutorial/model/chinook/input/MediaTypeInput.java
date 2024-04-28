package com.graphqljava.tutorial.model.chinook.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MediaTypeInput {
    private Integer MediaTypeId;
    private String Name;
    private int limit;
}
