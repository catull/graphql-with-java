package com.graphqljava.tutorial.retail.model.chinook;

public record Customer(
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
