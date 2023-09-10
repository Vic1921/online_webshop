package at.wst.online_webshop.exception_handlers;

public class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrderNotFoundException(Long id) {
        super("Order with ID " + id + " not found.");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
