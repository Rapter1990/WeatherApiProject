package com.skyapi.weatherforecast.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // handleMissingServletRequestParameter : triggers when there are missing parameters
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> details = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMessage() + " is not supported");
        details.add(builder.toString());

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        ErrorDTO error = new ErrorDTO.ErrorDTOBuilder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .errorDetails(details)
                .path(request.getContextPath())
                .httpStatus(httpStatus)
                .build();

        LOGGER.error("GlobalExceptionHandler | handleHttpRequestMethodNotSupported | ex : " + ex );

        return ResponseEntity.status(status).body(error);

    }

    // handleMethodArgumentNotValid : triggers when @Valid fails
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        LOGGER.error("GlobalExceptionHandler | handleMethodArgumentNotValid | ex : " + ex );

        return ResponseEntity.badRequest()
                .body(errors);
    }

    // handleMissingServletRequestParameter : triggers when there are missing parameters
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {


        List<String> details = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getParameterName());
        details.add(builder.toString());

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        ErrorDTO error = new ErrorDTO.ErrorDTOBuilder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .errorDetails(details)
                .path(request.getContextPath())
                .httpStatus(httpStatus)
                .build();

        LOGGER.error("GlobalExceptionHandler | handleMissingServletRequestParameter | ex : " + ex );

        return ResponseEntity.status(status).body(error);

    }

    // handleHttpMessageNotReadable : triggers when the JSON is malformed
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> details = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMessage());
        details.add(builder.toString());

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        ErrorDTO error = new ErrorDTO.ErrorDTOBuilder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .errorDetails(details)
                .path(request.getContextPath())
                .httpStatus(httpStatus)
                .build();

        LOGGER.error("GlobalExceptionHandler | handleHttpMessageNotReadable | ex : " + ex );

        return ResponseEntity.status(status).body(error);

    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(HttpServletRequest request, Exception ex) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ErrorDTO error = new ErrorDTO.ErrorDTOBuilder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorDetails(details)
                .path(request.getServletPath())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        LOGGER.error("GlobalExceptionHandler | handleHttpMessageNotReadable | ex : " + ex );

        return error;
    }
}
