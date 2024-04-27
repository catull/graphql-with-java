package com.graphqljava.tutorial.retail.model.chinook.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaylistTrackInput {
    private Integer PlaylistId;
    private Integer TrackId;
    private int limit;
}
