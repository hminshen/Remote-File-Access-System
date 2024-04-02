package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDeleteReqMessage {
    private int requestId;
    private int operationCode;
    private int offsetBytes;
    private int bytesToDelete;
    private String filename;

    public FileClientDeleteReqMessage(int requestId, int operationCode, int offsetBytes, int bytesToDelete, String filename) {
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.bytesToDelete = bytesToDelete;
        this.filename = filename;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getOffsetBytes() {
        return offsetBytes;
    }

    public int getBytesToDelete() {
        return bytesToDelete;
    }

    public String getFilename() {
        return filename;
    }
}
