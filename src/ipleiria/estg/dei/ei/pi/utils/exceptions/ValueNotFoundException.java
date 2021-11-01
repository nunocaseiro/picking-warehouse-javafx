package ipleiria.estg.dei.ei.pi.utils.exceptions;

import java.io.Serializable;

public class ValueNotFoundException extends Exception implements Serializable {

    public ValueNotFoundException() {
    }

    public ValueNotFoundException(String message) {
        super(message);
    }
}
