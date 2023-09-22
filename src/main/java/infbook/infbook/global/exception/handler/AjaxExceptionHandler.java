package infbook.infbook.global.exception.handler;

import infbook.infbook.exception.ajax.AjaxRequestException;
import infbook.infbook.global.exception.error.ErrorCode;
import infbook.infbook.global.exception.error.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class AjaxExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {AjaxRequestException.class})
    public ResponseEntity<Object> handleAjaxException(AjaxRequestException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResult errorResult = new ErrorResult(errorCode.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.valueOf(errorCode.getStatus()));
    }
}
