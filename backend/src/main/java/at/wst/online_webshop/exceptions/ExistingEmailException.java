package at.wst.online_webshop.exceptions;

public class ExistingEmailException extends FailedSignUpException {
    public ExistingEmailException(String message){
        super(message);
    }
}
