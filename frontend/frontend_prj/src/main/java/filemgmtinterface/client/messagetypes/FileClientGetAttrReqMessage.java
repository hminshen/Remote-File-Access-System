package main.java.filemgmtinterface.client.messagetypes;

public class FileClientGetAttrReqMessage {
    private int requestId;
    private int operationCode;

    private String filename;
    private String fileAttribute;
    public FileClientGetAttrReqMessage(int requestId, int operationCode, String filename, String fileAttribute) {
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.filename = filename;
        this.fileAttribute = fileAttribute;
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

    public String getFileAttribute() {
        return fileAttribute;
    }
}
