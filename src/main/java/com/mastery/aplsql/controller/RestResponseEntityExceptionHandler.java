package com.mastery.aplsql.controller;

import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@EnableWebMvc
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DuplicateEntryException.class})
    protected ResponseEntity<Object> handleDuplicateEntry(
            DuplicateEntryException ex, WebRequest request
    ) {
        String bodyOfResponse = "The entry you want to add is already present!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request
    ) {
        String bodyOfResponse = "The entry you were searching for is non-existent!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {TypeMismatchException.class, NumberFormatException.class})
    protected ResponseEntity<Object> handleTypeMismatch(
            Exception ex, WebRequest request
    ) {
        String bodyOfResponse = "Type no good bruh";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointer(
            NullPointerException ex, WebRequest request
    ) {
        String bodyOfResponse = "Yeah Levi is trash and did not handled the null pointer.Shame on him!!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {MalformedQueryException.class})
    protected ResponseEntity<Object> handleMalformedQuery(
            MalformedQueryException ex, WebRequest request
    ) {
        String bodyOfResponse = "The query you tried to execute is not properly formatted!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
