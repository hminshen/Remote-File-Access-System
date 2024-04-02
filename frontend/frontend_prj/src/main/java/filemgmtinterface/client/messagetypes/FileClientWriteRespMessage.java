package main.java.filemgmtinterface.client.messagetypes;

public class FileClientWriteRespMessage {
    private int operationCode;
    private int filenameLen;
    private int contentLen;
    private String filename;

    public FileClientWriteRespMessage(int operationCode, int filenameLen, int contentLen, String filename) {
        this.operationCode = operationCode;
        this.filenameLen = filenameLen;
        this.contentLen = contentLen;
        this.filename = filename;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getFilename_len() {
        return filenameLen;
    }

    public int getContent_len() {
        return contentLen;
    }

    public String getFilename() {
        return filename;
    }
}
