package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDeleteReqMessage {
    public int operationCode;
    public int offsetBytes;
    public int bytesToDelete;
    public String filename;

    public FileClientDeleteReqMessage(int operationCode, int offsetBytes, int bytesToDelete, String filename) {
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.bytesToDelete = bytesToDelete;
        this.filename = filename;
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
