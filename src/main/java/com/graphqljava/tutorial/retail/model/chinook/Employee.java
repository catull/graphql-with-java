package com.graphqljava.tutorial.retail.model.chinook;

public record Employee(
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
