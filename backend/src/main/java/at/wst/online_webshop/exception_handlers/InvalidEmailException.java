package at.wst.online_webshop.exception_handlers;

public class InvalidEmailException extends FailedSignUpException{
    public InvalidEmailException(String message){
        super(message);
    }
}
