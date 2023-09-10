package at.wst.online_webshop.exception_handlers;

public class FailedOrderException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public FailedOrderException(Long id) {
            super("Order with ID " + id + " failed.");
        }

        public FailedOrderException(String message) {
            super(message);
        }
}
