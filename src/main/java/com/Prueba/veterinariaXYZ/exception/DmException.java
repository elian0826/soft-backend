package com.Prueba.veterinariaXYZ.exception;

public class DmException extends Exception {

    public DmException() {
        super();
    }

    public DmException(String message) {
        super(message);
    }

    public DmException(String message, Throwable cause) {
        super(message, cause);
    }

    public DmException(Throwable cause) {
        super(cause);
    }

    protected DmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
