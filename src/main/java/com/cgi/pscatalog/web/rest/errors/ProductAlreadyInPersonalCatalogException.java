package com.cgi.pscatalog.web.rest.errors;

public class ProductAlreadyInPersonalCatalogException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ProductAlreadyInPersonalCatalogException(String errorMessage) {
        super(errorMessage);
    }
}
