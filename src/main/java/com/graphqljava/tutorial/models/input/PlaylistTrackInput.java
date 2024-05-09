package com.graphqljava.tutorial.models.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaylistTrackInput {
    private Integer PlaylistId;
    private Integer TrackId;
    private int limit;
}
