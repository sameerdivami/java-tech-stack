package com.familyleague.exception;

public class SeasonClosedException extends RuntimeException {
    public SeasonClosedException(String message) {
        super(message);
    }
}
