package com.btxdev.kardex.config;

import com.btxdev.kardex.dto.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@ControllerAdvice
@Configuration
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {
    private static final String DEFAULT_KEY_STATUS = "status";
    private static final String DEFAULT_KEY_ERROR = "error";
    private static final String DEFAULT_KEY_MESSAGE = "message";
    private static final String DEFAULT_KEY_PATH = "path";
    private static final String DEFAULT_KEY_EXCEPTION = "exception";
    private static final String INTERNAL_ERROR_MSG = "Internal error";

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Map<String ,Object> defaultMap = super.getErrorAttributes(webRequest, options);

                return ErrorResponse.builder()
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .status((Integer) defaultMap.get(DEFAULT_KEY_STATUS))
                        .error((String) defaultMap.get(DEFAULT_KEY_ERROR))
                        .path((String) defaultMap.get(DEFAULT_KEY_PATH))
                        .exception((String) defaultMap.get(DEFAULT_KEY_EXCEPTION))
                        .message((String) defaultMap.get(DEFAULT_KEY_MESSAGE))
                        .build().toMap();
            }
        };
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(INTERNAL_ERROR_MSG, ex);
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        return new ResponseEntity<>(new ErrorResponse(status, request, rootCause, rootCause.getMessage()), status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(
            HttpServletRequest request, Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(INTERNAL_ERROR_MSG, ex);
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        return new ResponseEntity<>(new ErrorResponse(status, request, rootCause, rootCause.getMessage()), status);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(
            HttpServletRequest request, RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(INTERNAL_ERROR_MSG, ex);

        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        return new ResponseEntity<>(new ErrorResponse(status, request, rootCause, rootCause.getMessage()), status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status, request, ex
                , ex.getName()+" parameter type mismatch"), status);
    }

    @ExceptionHandler(SecurityException.class)
    protected ResponseEntity<Object> handleSecurityException(
            HttpServletRequest request, SecurityException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(new ErrorResponse(status, request, ex, ex.getMessage()), status);
    }

    @ExceptionHandler({EntityExistsException.class, EntityNotFoundException.class
            , IllegalArgumentException.class})
    protected ResponseEntity<Object> handleToBadRequest(
            HttpServletRequest request, Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status, request, ex, ex.getMessage()), status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName+" "+errorMessage);
        });

        String errorsString = errors.toString();
        String msg = errorsString.substring(1, errorsString.length()-1);

        return new ResponseEntity<>(new ErrorResponse(status, request, ex, msg), status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> constraintViolationException(
            HttpServletRequest request, ConstraintViolationException ex) {
        log.error(INTERNAL_ERROR_MSG, ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(constraintViolation ->
            errors.add(constraintViolation.getPropertyPath().toString()+" "+constraintViolation.getMessage())
        );

        String errorsString = errors.toString();
        String msg = errorsString.substring(1, errorsString.length()-1);

        return new ResponseEntity<>(new ErrorResponse(status, request
                , MethodArgumentNotValidException.class.getSimpleName(), msg), status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex
            , @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(status, request, ex
                , "Parameter with name "+ex.getParameterName()+" is missing"), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if(rootCause instanceof InvalidFormatException){
            InvalidFormatException invalidFormatException = (InvalidFormatException) rootCause;
            return new ResponseEntity<>(new ErrorResponse(status, request, ex
                    , invalidFormatException.getTargetType().getSimpleName()+" "
                    +invalidFormatException.getValue().toString()+" has an incorrect format"), status);
        }

        if(rootCause instanceof DateTimeParseException){
            InvalidFormatException invalidFormatException = (InvalidFormatException)
                    ExceptionUtils.getThrowableList(ex).get(ExceptionUtils.indexOfType(ex, InvalidFormatException.class));

            return new ResponseEntity<>(new ErrorResponse(status, request, ex
                    , invalidFormatException.getValue().toString()+ " has an incorrect datetime format"), status);
        }

        if(rootCause instanceof UnrecognizedPropertyException){
            UnrecognizedPropertyException unrecognizedPropertyException = (UnrecognizedPropertyException) rootCause;

            return new ResponseEntity<>(new ErrorResponse(status, request, ex
                    , "Unrecognized field "+unrecognizedPropertyException.getPropertyName()), status);
        }

        if(rootCause instanceof JsonParseException){
            JsonParseException jsonParseException = (JsonParseException) rootCause;
            String msg = jsonParseException.getMessage();
            if(msg.contains("\n at")){
                msg = jsonParseException.getMessage().split("\n at")[0];
            }
            return new ResponseEntity<>(new ErrorResponse(status, request, ex, msg), status);
        }

        return new ResponseEntity<>(new ErrorResponse(status, request, rootCause, rootCause.getMessage()), status);
    }
}