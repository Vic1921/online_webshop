package at.wst.online_webshop.exception_handlers;

public class InsufficientProductQuantityException extends RuntimeException{
    public InsufficientProductQuantityException(String message){
        super(message);
    }
}
