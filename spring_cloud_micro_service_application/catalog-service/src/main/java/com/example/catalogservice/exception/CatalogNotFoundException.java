package com.example.catalogservice.exception;

public class CatalogNotFoundException extends RuntimeException {
    public CatalogNotFoundException() {
        super();
    }

    public CatalogNotFoundException(String message) {
        super(message);
    }

    public CatalogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CatalogNotFoundException(Throwable cause) {
        super(cause);
    }
}
