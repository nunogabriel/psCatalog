package com.cgi.pscatalog.web.rest.errors;

public class InsufficientProductQuantityException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InsufficientProductQuantityException(String entityName) {
        super(ErrorConstants.INSUFFICIENT_PRODUCT_QUANTITY, "Insufficient product quantity!", entityName, "insufficientProductQuantity");
    }
}
