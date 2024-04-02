package main.java.filemgmtinterface.client.messagetypes;

public class FileClientReadReqMessage {
    private int requestId;
    private int operationCode;
    private int offsetBytes;
    private int bytesToRead;
    private String filename;

    public FileClientReadReqMessage(int requestId, int operationCode, int offsetBytes, int bytesToRead, String filename) {
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.bytesToRead = bytesToRead;
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

    public int getBytesToRead() {
        return bytesToRead;
    }

    public String getFilename() {
        return filename;
    }
}
