package com.graphqljava.tutorial.retail.model.chinook.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaylistInput {
    private Integer PlaylistId;
    private String Name;
    private int limit;
}
