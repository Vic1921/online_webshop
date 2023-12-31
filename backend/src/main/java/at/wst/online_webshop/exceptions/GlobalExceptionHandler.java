package at.wst.online_webshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedOrderException.class)
    public ResponseEntity<Object> handleFailedOrderException(FailedOrderException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExistingEmailException.class)
    public ResponseEntity<Object> handleExistingEmailException(ExistingEmailException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Object> handleInvalidEmailException(InvalidEmailException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedSignUpException.class)
    public ResponseEntity<Object> handleFailedSignUpException(FailedSignUpException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<Object> handleWeakPasswordException(WeakPasswordException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedReviewException.class)
    public ResponseEntity<Object> handleFailedReviewException(FailedReviewException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ResponseEntity<Object> handleShoppingCartNotFoundException(ShoppingCartNotFoundException ex, WebRequest request) {
        Map<String, Object> responseBody = createErrorResponse(ex, request);
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> createErrorResponse(Exception ex, WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("message", ex.getMessage());
        responseBody.put("path", request.getDescription(true));
        return responseBody;
    }

}
