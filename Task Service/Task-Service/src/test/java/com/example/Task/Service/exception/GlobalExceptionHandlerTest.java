package com.example.Task.Service.exception;
import com.example.Task.Service.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound_shouldReturnNotFoundResponse() {
        ResourceNotFoundException exception =
                new ResourceNotFoundException("Task was not found");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Task was not found", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleBadRequest_shouldReturnBadRequestResponse() {
        IllegalArgumentException exception =
                new IllegalArgumentException("Invalid task schedule");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleBadRequest(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid task schedule", response.getBody().getMessage());
        assertEquals(400, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleGeneral_shouldReturnInternalServerErrorResponse() {
        Exception exception = new RuntimeException("Database unavailable");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleGeneral(exception);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "An unexpected error occurred. Please try again later.",
                response.getBody().getMessage()
        );

        assertEquals(500, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleValidation_shouldReturnFirstFieldError() {
        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError(
                "createTaskRequest",
                "title",
                "must not be blank"
        );

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(
                "title: must not be blank",
                response.getBody().getMessage()
        );

        assertEquals(400, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleValidation_shouldReturnFallbackMessageWhenNoFieldErrorsExist() {
        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of());

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "Validation error",
                response.getBody().getMessage()
        );
        assertEquals(400, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleMalformedJson_shouldReturnBadRequestResponse() {
        HttpMessageNotReadableException exception =
                mock(HttpMessageNotReadableException.class);

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleMalformedJson(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "Malformed JSON request",
                response.getBody().getMessage()
        );
        assertEquals(400, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

}
