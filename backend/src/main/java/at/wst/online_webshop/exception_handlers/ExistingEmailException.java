package at.wst.online_webshop.exception_handlers;

public class ExistingEmailException extends FailedSignUpException {
    public ExistingEmailException(String message){
        super(message);
    }
}
