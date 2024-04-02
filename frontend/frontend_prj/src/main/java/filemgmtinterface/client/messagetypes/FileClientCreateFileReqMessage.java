package main.java.filemgmtinterface.client.messagetypes;

public class FileClientCreateFileReqMessage {
    private int requestId;
    private int operationCode;
    private String filename;
    private String content;

    public FileClientCreateFileReqMessage(int requestId, int operationCode, String filename, String content){
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.filename = filename;
        this.content = content;
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

    public String getContent() {
        return content;
    }
}
