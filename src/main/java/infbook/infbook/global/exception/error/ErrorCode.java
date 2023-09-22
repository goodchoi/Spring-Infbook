package infbook.infbook.global.exception.error;

public enum ErrorCode {

    MEMBER_NOT_FOUND(400, "AJX001", "MEMBER_NOT_FOUND"),
    CART_NOT_FOUND(400,"AJX002","CART_NOT_FOUND"),
    ITEM_NOT_FOUND(400,"AJX003","ITEM_NOT_FOUND"),
    SI_NOT_FOUND(400,"AJX004","SI__NOT_FOUND");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(int status, String code, String message) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
