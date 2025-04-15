package com.example.demo.exception;



public class DailyBatchLimitReachedException extends RuntimeException {

    public DailyBatchLimitReachedException(String message) {
        super(message);
    }
}

