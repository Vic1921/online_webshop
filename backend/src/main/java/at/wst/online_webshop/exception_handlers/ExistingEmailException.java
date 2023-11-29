package at.wst.online_webshop.exception_handlers;

public class ExistingEmailException extends RuntimeException{
    public ExistingEmailException(String message){
        super(message);
    }
}
