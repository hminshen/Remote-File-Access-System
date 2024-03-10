package main.java.filemgmtinterface.client.messagetypes;

public class ErrorMessage {
    public int errorCode;

    public String errMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
    public ErrorMessage(int errorCode, String errMsg) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }
}
