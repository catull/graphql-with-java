package com.graphqljava.tutorial.model.chinook;

public record Invoice(
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
