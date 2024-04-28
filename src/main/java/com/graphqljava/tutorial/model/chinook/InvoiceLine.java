package com.graphqljava.tutorial.model.chinook;

public record InvoiceLine(
        Integer InvoiceLineId,
        Integer InvoiceId,
        Integer TrackId,
        Float UnitPrice,
        Integer Quantity
) {
}
