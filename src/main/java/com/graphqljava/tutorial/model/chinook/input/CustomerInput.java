package com.graphqljava.tutorial.model.chinook.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerInput {
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
    private int limit;
}
