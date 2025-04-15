package com.example.demo.exception;

import com.example.demo.resp_dto.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {

        List<String> list = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // error:
            String message = error.getDefaultMessage();
            list.add(message);
        });

        // List<String> list= Arrays.asList("lucky","rushi","anil");
        String delim = ",";
        String res = list.stream().map(Object::toString)
                .collect(Collectors.joining(delim));
        //    System.out.println("javax exception: "+res);


        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, res, request.getDescription(false)
        );

        return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        String message = ex.getMessage();
        ex.printStackTrace();
        ApiResponse apiResponse = ApiResponse.builder()
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .result(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingDataException.class)
    public ResponseEntity<ApiResponse> handleMissingDataException(MissingDataException ex, WebRequest request) {
        String message = ex.getMessage();
        ex.printStackTrace();
        ApiResponse apiResponse = ApiResponse.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .result(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataMismatchException.class)
    public ResponseEntity<ApiResponse> handleDataMismatchException(DataMismatchException ex, WebRequest request) {
        // Log the stack trace (optional)
        ex.printStackTrace();

        // Create ApiResponse object
        ApiResponse apiResponse = ApiResponse.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)  // BAD_REQUEST because data mismatch is a client-side error
                .message(ex.getMessage())  // Send the error message from the exception
                .result(null)  // Optionally, provide any additional details in the result if necessary
                .build();

        // Return ResponseEntity with the response and status
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ApiResponse> handleCustomApiException(CustomApiException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null));
    }



    @ExceptionHandler(ResourceExhaustedException.class)
    public ResponseEntity<ApiResponse> handleResourceExhaustedException(ResourceExhaustedException ex, WebRequest request) {
        // Log the stack trace (optional)
        ex.printStackTrace();

        // Create ApiResponse object
        ApiResponse apiResponse = ApiResponse.builder()
                .httpStatusCode(HttpStatus.OK)  // BAD_REQUEST because data mismatch is a client-side error
                .message(ex.getMessage())  // Send the error message from the exception
                .result(null)  // Optionally, provide any additional details in the result if necessary
                .build();

        // Return ResponseEntity with the response and status
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Exception caught: {}", ex.getMessage(), ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred.")
                .result(null)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // Handle AllQuestionsProcessedException
    @ExceptionHandler(AllQuestionsProcessedException.class)
    public ResponseEntity<ApiResponse> handleAllQuestionsProcessedException(AllQuestionsProcessedException ex) {
        log.warn("🛑 All questions processed: {}", ex.getMessage());
        ApiResponse response = new ApiResponse(
                HttpStatus.OK,
                "🛑 All questions are processed. No more questions left.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Handle DailyBatchLimitReachedException
    @ExceptionHandler(DailyBatchLimitReachedException.class)
    public ResponseEntity<ApiResponse> handleDailyBatchLimitReachedException(DailyBatchLimitReachedException ex) {
        log.info("Daily batch limit reached: {}", ex.getMessage());
        ApiResponse response = new ApiResponse(
                HttpStatus.OK,
                "Daily batch limit reached. No more questions for today's batch.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
