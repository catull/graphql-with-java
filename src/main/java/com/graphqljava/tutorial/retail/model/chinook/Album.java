package com.graphqljava.tutorial.retail.model.chinook;

public record Album(
        Integer AlbumId,
        String Title,
        Integer ArtistId
) {
}
