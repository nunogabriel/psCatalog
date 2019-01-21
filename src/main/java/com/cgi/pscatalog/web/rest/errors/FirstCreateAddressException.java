package com.cgi.pscatalog.web.rest.errors;

public class FirstCreateAddressException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public FirstCreateAddressException(String entityName) {
        super(ErrorConstants.FIRST_CREATE_ADDRESS, "First create addresses!", entityName, "firstCreateAddress");
    }
}
