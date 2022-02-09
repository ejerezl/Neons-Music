package Exceptions;

import java.io.Serializable;

public class WrongAnswersException extends LoginException implements Serializable {
     public WrongAnswersException(String message){
        super(message);
    }
}
