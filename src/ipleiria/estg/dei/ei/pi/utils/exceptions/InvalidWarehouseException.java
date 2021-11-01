package ipleiria.estg.dei.ei.pi.utils.exceptions;

import java.io.Serializable;

public class InvalidWarehouseException  extends Exception implements Serializable {

    public InvalidWarehouseException() {
    }

    public InvalidWarehouseException(String message) {
        super(message);
    }
}
