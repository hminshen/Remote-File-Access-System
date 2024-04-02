package main.java.filemgmtinterface.client.messagetypes;

public class ErrorMessage {
    private int errorCode;
    private String errMsg;

    public ErrorMessage(int errorCode, String errMsg) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
