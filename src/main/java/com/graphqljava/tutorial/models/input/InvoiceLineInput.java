package com.graphqljava.tutorial.models.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceLineInput {
    private Integer InvoiceLineId;
    private Integer InvoiceId;
    private Integer TrackId;
    private Float UnitPrice;
    private Integer Quantity;
    private int limit;
}
