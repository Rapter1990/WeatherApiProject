package com.skyapi.weatherforecast.exception;

public class GeolocationException extends Exception {

    public GeolocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeolocationException(String message) {
        super(message);
    }

}
