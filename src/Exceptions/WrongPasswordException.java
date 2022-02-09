package Exceptions;

import java.io.Serializable;

public class WrongPasswordException extends LoginException implements Serializable {
    public WrongPasswordException(String message){
        super(message);
    }
}
