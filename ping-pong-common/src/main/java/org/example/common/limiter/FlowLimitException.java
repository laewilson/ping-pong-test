package org.example.common.limiter;

public class FlowLimitException extends RuntimeException {
    public FlowLimitException(String message) {
        super(message);
    }
}
