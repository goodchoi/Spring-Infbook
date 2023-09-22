package infbook.infbook.exception.ajax;

import infbook.infbook.global.exception.error.ErrorCode;

public class AjaxCartNotFoundException extends AjaxRequestException{

    public AjaxCartNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AjaxCartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
