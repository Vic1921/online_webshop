package at.wst.online_webshop.exceptions;

public class InvalidEmailException extends FailedSignUpException{
    public InvalidEmailException(String message){
        super(message);
    }
}
