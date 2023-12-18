package at.wst.online_webshop.exception_handlers;

public class WeakPasswordException extends FailedSignUpException {
    public WeakPasswordException(String message){
        super(message);
    }
}
