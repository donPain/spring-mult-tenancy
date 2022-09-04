package com.donzelitos.multtenancy.configuration.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SolinftecDefaultException extends Exception {

    public SolinftecDefaultException() {
        super();
    }

    public SolinftecDefaultException(String message) {
        super(message);
    }

    public SolinftecDefaultException(String message, Throwable cause) {
        super(message, cause);
    }

    public SolinftecDefaultException(String message, String cause) {
        super(message, new Throwable(cause));
    }

    public SolinftecDefaultException(Throwable cause) {
        super(cause);
    }
}
