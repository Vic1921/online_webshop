package at.wst.online_webshop.exception_handlers;

public class FailedSignUpException extends RuntimeException{
    public FailedSignUpException(String message){
        super(message);
    }
}
