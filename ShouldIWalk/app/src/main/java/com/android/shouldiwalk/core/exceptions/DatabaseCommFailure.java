package com.android.shouldiwalk.core.exceptions;

public class DatabaseCommFailure extends RuntimeException {
    public DatabaseCommFailure(Throwable throwable) {
        super(throwable);
    }
}
