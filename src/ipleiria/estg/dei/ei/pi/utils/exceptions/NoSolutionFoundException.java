package ipleiria.estg.dei.ei.pi.utils.exceptions;

import java.io.Serializable;

public class NoSolutionFoundException extends Exception implements Serializable {

    public NoSolutionFoundException() { }

    public NoSolutionFoundException(String message) {
        super(message);
    }
}
