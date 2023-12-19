package at.wst.online_webshop.exceptions;

public class FailedOrderException extends RuntimeException {
        public FailedOrderException(String message) {
            super(message);
        }
}
