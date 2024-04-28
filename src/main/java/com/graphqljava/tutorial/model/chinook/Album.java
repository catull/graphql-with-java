package com.graphqljava.tutorial.model.chinook;

public record Album(
        Integer AlbumId,
        String Title,
        Integer ArtistId
) {
}
