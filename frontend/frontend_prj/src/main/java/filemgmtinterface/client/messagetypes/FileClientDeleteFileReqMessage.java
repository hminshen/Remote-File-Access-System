package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDeleteFileReqMessage {
    private int requestId;
    private int operationCode;
    private String filename;

    public FileClientDeleteFileReqMessage(int requestId, int operationCode, String filename){
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.filename = filename;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public String getFilename() {
        return filename;
    }
}
