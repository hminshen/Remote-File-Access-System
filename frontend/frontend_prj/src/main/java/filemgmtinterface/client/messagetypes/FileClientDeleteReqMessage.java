package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDeleteReqMessage {
    public int operationCode;
    public int offsetBytes;
    public int bytesToDelete;
    public String filename;

    public int getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }

    public int getOffsetBytes() {
        return offsetBytes;
    }

    public void setOffsetBytes(int offsetBytes) {
        this.offsetBytes = offsetBytes;
    }

    public int getBytesToDelete() {
        return bytesToDelete;
    }

    public void setBytesToDelete(int bytesToDelete) {
        this.bytesToDelete = bytesToDelete;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileClientDeleteReqMessage(int operationCode, int offsetBytes, int bytesToDelete, String filename) {
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.bytesToDelete = bytesToDelete;
        this.filename = filename;
    }
}
