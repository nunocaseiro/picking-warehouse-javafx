package ipleiria.estg.dei.ei.pi.utils.exceptions;

import java.io.Serializable;

public class InvalidNodeException extends Exception implements Serializable {

    public InvalidNodeException() {
    }

    public InvalidNodeException(String message) {
        super(message);
    }
}
