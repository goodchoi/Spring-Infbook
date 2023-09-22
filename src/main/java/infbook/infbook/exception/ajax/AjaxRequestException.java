package infbook.infbook.exception.ajax;

import infbook.infbook.global.exception.error.ErrorCode;

public class AjaxRequestException extends RuntimeException{

    private ErrorCode errorCode;

    public AjaxRequestException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AjaxRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
