package com.cgi.pscatalog.web.rest.errors;

public class FirstCreateCustomerException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public FirstCreateCustomerException(String entityName) {
        super(ErrorConstants.FIRST_CREATE_CUSTOMER, "First create customer!", entityName, "firstCreateCustomer");
    }
}
