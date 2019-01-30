package com.cgi.pscatalog.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "psCatalogApp";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", "error." + errorKey);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }

    public static HttpHeaders createEntityAddBasketAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".addBasket", param);
    }

    public static HttpHeaders createEntityAddPersonalAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".addPersonal", param);
    }

    public static HttpHeaders createEntityAddPersonalAlreadyAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".addPersonalAlready", param);
    }

    public static HttpHeaders createEntityDeletePersonalDoesNotExistAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deletePersonalDoesNotExist", param);
    }

    public static HttpHeaders createEntityDeletePersonalAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deletePersonal", param);
    }

    public static HttpHeaders createEntityOrderAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

}
