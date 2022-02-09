package Exceptions;

import java.io.Serializable;

public abstract class LoginException extends Exception implements Serializable{

    public LoginException(String message){
        super(message);
    }
}
