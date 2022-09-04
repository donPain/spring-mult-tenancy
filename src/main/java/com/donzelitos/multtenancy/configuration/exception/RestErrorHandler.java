package com.donzelitos.multtenancy.configuration.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler({SolinftecDefaultException.class, Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> processValidationError(WebRequest webRequest, SolinftecDefaultException ex) {
        var err = new DefaultErrorAttributes();
        final var resquetScope = 0;
        webRequest.setAttribute("javax.servlet.error.status_code", HttpStatus.BAD_REQUEST.value(), resquetScope);
        var errorAttributeOptions = new ErrorAttributeOptions.Include[]{
                ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS
        };
        return err.getErrorAttributes(webRequest, ErrorAttributeOptions.of(errorAttributeOptions));
    }
}