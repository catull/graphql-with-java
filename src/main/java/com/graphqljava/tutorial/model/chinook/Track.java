package com.graphqljava.tutorial.model.chinook;

public record Track(
        Integer TrackId,
        String Name,
        Integer AlbumId,
        Integer GenreId,
        Integer MediaTypeId,
        String Composer,
        Integer Milliseconds,
        Integer Bytes,
        Float UnitPrice
) {
}
