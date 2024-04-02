package main.java.filemgmtinterface.client.messagetypes;

public class FileClientWriteReqMessage {
    private int requestId;
    private int operationCode;
    private int offsetBytes;
    private String filename;
    private String writeSequence;

    public FileClientWriteReqMessage(int requestId, int operationCode, int offsetBytes, String filename, String writeSequence){
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.filename = filename;
        this.writeSequence = writeSequence;
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

    public String getFilename() {
        return filename;
    }

    public String getWriteSequence() {
        return writeSequence;
    }
}
