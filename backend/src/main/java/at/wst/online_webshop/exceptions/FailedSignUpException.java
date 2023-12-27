package at.wst.online_webshop.exceptions;

public class FailedSignUpException extends RuntimeException{
    public FailedSignUpException(String message){
        super(message);
    }
}
