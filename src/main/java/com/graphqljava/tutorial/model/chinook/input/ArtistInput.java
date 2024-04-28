package com.graphqljava.tutorial.model.chinook.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtistInput {
    private Integer ArtistId;
    private String Name;
    private int limit;
}
