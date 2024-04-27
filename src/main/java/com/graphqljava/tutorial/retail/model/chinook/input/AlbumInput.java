package com.graphqljava.tutorial.retail.model.chinook.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumInput {
    private Integer AlbumId;
    private String Title;
    private Integer ArtistId;
    private int limit;
}
