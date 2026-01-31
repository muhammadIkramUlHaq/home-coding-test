package dev.mikram.homeassignment.common.error;

public class DownstreamApiException extends RuntimeException {
    public DownstreamApiException(String message) {
        super(message);
    }

    public DownstreamApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
