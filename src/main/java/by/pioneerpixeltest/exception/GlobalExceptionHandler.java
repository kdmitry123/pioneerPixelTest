package by.pioneerpixeltest.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {


    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<BaseErrorResponse> handleUserValidationException(UserValidationException ex) {
        log.error("Something went wrong: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new BaseErrorResponse(
                        ZonedDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "error_user_validation",
                        ex.getMessage()
                ),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Something went wrong: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new BaseErrorResponse(
                        ZonedDateTime.now(),
                        HttpStatus.FORBIDDEN.value(),
                        "error_user_authorization",
                        ex.getMessage()
                ),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseErrorResponse> handleDifferentException(Exception ex) {
        log.error("Something went wrong: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new BaseErrorResponse(
                        ZonedDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "some_error",
                        ex.getMessage()
                ),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }
}
