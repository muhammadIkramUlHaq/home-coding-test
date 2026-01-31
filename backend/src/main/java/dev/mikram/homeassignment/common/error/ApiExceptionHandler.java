package dev.mikram.homeassignment.common.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static Map<String, Object> body(int status, String error, String message) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status,
                "error", error,
                "message", message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                body(400, "Bad Request", ex.getMessage()),
                jsonHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DownstreamApiException.class)
    public ResponseEntity<Map<String, Object>> handleDownstream(DownstreamApiException ex) {
        return new ResponseEntity<>(
                body(502, "Bad Gateway", ex.getMessage() == null ? "External API error" : ex.getMessage()),
                jsonHeaders(),
                HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handle(Exception ex) {
        return new ResponseEntity<>(
                body(500, "Internal Server Error",
                        ex.getMessage() == null ? "Unexpected server error" : ex.getMessage()),
                jsonHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
