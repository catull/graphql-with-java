package com.graphqljava.tutorial.retail.model.chinook.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeInput {
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
    private int limit;
}
