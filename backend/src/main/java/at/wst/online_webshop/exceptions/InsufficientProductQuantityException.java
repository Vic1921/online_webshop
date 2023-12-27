package at.wst.online_webshop.exceptions;

public class InsufficientProductQuantityException extends RuntimeException{
    public InsufficientProductQuantityException(String message){
        super(message);
    }
}
