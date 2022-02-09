package Exceptions;

import java.io.Serializable;

public class BannedUserException extends LoginException implements Serializable {
    public BannedUserException(String message){
        super(message);
    }
}
