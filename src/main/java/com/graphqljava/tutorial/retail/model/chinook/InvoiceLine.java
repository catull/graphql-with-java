package com.graphqljava.tutorial.retail.model.chinook;

public record InvoiceLine(
        Integer InvoiceLineId,
        Integer InvoiceId,
        Integer TrackId,
        Float UnitPrice,
        Integer Quantity
) {
}
