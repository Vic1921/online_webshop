package at.wst.online_webshop.exception_handlers;

public class ShoppingCartNotFoundException extends RuntimeException {

            private static final long serialVersionUID = 1L;

            public ShoppingCartNotFoundException(Long id) {
                super("ShoppingCart with ID " + id + " not found.");
            }

            public ShoppingCartNotFoundException(String message) {
                super(message);
            }
}
