package com.graphqljava.tutorial.retail.models;

public class ChinookModels {
    public
    record Album(
            Integer AlbumId,
            String Title,
            Integer ArtistId
    ) {
    }

    public
    record Artist(
            Integer ArtistId,
            String Name
    ) {
    }

    public
    record Customer(
            Integer CustomerId,
            String FirstName,
            String LastName,
            String Company,
            String Address,
            String City,
            String State,
            String Country,
            String PostalCode,
            String Phone,
            String Fax,
            String Email,
            Integer SupportRepId
    ) {
    }

    public
    record Employee(
            Integer EmployeeId,
            String LastName,
            String FirstName,
            String Title,
            Integer ReportsTo,
            String BirthDate,
            String HireDate,
            String Address,
            String City,
            String State,
            String Country,
            String PostalCode,
            String Phone,
            String Fax,
            String Email
    ) {
    }

    public
    record Genre(
            Integer GenreId,
            String Name
    ) {
    }

    public
    record Invoice(
            Integer InvoiceId,
            Integer CustomerId,
            String InvoiceDate,
            String BillingAddress,
            String BillingCity,
            String BillingState,
            String BillingCountry,
            String BillingPostalCode,
            Float Total
    ) {
    }

    public
    record InvoiceLine(
            Integer InvoiceLineId,
            Integer InvoiceId,
            Integer TrackId,
            Float UnitPrice,
            Integer Quantity
    ) {
    }


    public
    record MediaType(
            Integer MediaTypeId,
            String Name
    ) {
    }


    public
    record Playlist(
            Integer PlaylistId,
            String Name
    ) {
    }

    public
    record PlaylistTrack(
            Integer PlaylistId,
            Integer TrackId
    ) {
    }

    public
    record Track(
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
}
