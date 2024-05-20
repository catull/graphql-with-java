package com.graphqljava.tutorial.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ChinookModels {
    @Entity
    @Table(name = "\"Album\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Album {
        @Id
        private Integer AlbumId;
        private String Title;
        private Integer ArtistId;
    }

    @Entity
    @Table(name = "\"Artist\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Artist {
        @Id
        private Integer ArtistId;
        private String Name;
    }

    @Entity
    @Table(name = "\"Customer\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Customer {
        @Id
        private Integer CustomerId;
        private String FirstName;
        private String LastName;
        private String Company;
        private String Address;
        private String City;
        private String State;
        private String Country;
        private String PostalCode;
        private String Phone;
        private String Fax;
        private String Email;
        private Integer SupportRepId;
    }

    @Entity
    @Table(name = "\"Employee\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Employee {
        @Id
        private Integer EmployeeId;
        private String LastName;
        private String FirstName;
        private String Title;
        private Integer ReportsTo;
        private String BirthDate;
        private String HireDate;
        private String Address;
        private String City;
        private String State;
        private String Country;
        private String PostalCode;
        private String Phone;
        private String Fax;
        private String Email;
    }

    @Entity
    @Table(name = "\"Genre\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Genre {
        @Id
        private Integer GenreId;
        private String Name;
    }

    @Entity
    @Table(name = "\"Invoice\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Invoice {
        @Id
        private Integer InvoiceId;
        private Integer CustomerId;
        private String InvoiceDate;
        private String BillingAddress;
        private String BillingCity;
        private String BillingState;
        private String BillingCountry;
        private String BillingPostalCode;
        private Float Total;
    }

    @Entity
    @Table(name = "\"InvoiceLine\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class InvoiceLine {
        @Id
        private Integer InvoiceLineId;
        private Integer InvoiceId;
        private Integer TrackId;
        private Float UnitPrice;
        private Integer Quantity;
    }

    @Entity
    @Table(name = "\"MediaType\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class MediaType {
        @Id
        private Integer MediaTypeId;
        private String Name;
    }

    @Entity
    @Table(name = "\"Playlist\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Playlist {
        @Id
        private Integer PlaylistId;
        private String Name;
    }

    @Entity
    @Table(name = "\"PlaylistTrack\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PlaylistTrack {
        @Id
        private Integer PlaylistId;
        private Integer TrackId;
    }

    @Entity
    @Table(name = "\"Track\"")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Track {
        @Id
        private Integer TrackId;
        private String Name;
        private Integer AlbumId;
        private Integer MediaTypeId;
        private Integer GenreId;
        private String Composer;
        private Integer Milliseconds;
        private Integer Bytes;
        private Float UnitPrice;
    }
}
