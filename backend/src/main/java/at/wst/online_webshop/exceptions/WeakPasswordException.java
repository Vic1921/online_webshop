package at.wst.online_webshop.exceptions;

public class WeakPasswordException extends FailedSignUpException {
    public WeakPasswordException(String message){
        super(message);
    }
}
