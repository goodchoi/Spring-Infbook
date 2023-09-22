package infbook.infbook.exception.ajax;

import infbook.infbook.global.exception.error.ErrorCode;

public class AjaxItemNotFoundException extends AjaxRequestException{

    public AjaxItemNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AjaxItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
