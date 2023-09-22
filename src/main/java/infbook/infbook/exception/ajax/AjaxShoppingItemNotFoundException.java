package infbook.infbook.exception.ajax;

import infbook.infbook.global.exception.error.ErrorCode;

public class AjaxShoppingItemNotFoundException extends AjaxRequestException{

    public AjaxShoppingItemNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AjaxShoppingItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
