package com.graphqljava.tutorial.retail.model.chinook.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrackInput {
   private Integer TrackId;
   private String Name;
   private Integer AlbumId;
   private Integer GenreId;
   private Integer MediaTypeId;
   private String Composer;
   private Integer Milliseconds;
   private Integer Bytes;
   private Float UnitPrice;
   private int limit;
}
