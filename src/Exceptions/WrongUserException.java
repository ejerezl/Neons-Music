package Exceptions;

import java.io.Serializable;

public class WrongUserException extends LoginException implements Serializable {

    public WrongUserException(String message){
        super(message);
    }
}
