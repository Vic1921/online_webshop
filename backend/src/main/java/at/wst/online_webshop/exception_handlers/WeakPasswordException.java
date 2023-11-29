package at.wst.online_webshop.exception_handlers;

public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException(String message){
        super(message);
    }
}
