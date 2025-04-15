package com.example.demo.exception;

public class DataMismatchException extends RuntimeException {
    public DataMismatchException(String message) {
        super(message);
    }
}
