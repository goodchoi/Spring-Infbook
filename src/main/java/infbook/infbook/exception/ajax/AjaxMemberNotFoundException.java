package infbook.infbook.exception.ajax;

import infbook.infbook.global.exception.error.ErrorCode;

public class AjaxMemberNotFoundException extends AjaxRequestException{

    public AjaxMemberNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AjaxMemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
