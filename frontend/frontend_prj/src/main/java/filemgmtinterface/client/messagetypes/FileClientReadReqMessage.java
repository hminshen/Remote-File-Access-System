package main.java.filemgmtinterface.client.messagetypes;

public class FileClientReadReqMessage {
    public int operationCode;
    public int offsetBytes;
    public int bytesToRead;
    public String filename;

    public FileClientReadReqMessage(int operationCode, int offsetBytes, int bytesToRead, String filename){
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.bytesToRead = bytesToRead;
        this.filename = filename;
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
