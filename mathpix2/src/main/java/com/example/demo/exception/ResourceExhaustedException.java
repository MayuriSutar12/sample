package com.example.demo.exception;

public class ResourceExhaustedException extends RuntimeException {
    public ResourceExhaustedException(String message) {
        super(message);
    }
}

