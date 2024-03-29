package main.java.filemgmtinterface.client.messagetypes;

public class FileClientWriteRespMessage {
    public int operationCode;
    public int filenameLen;
    public int contentLen;
    public String filename;

    public FileClientWriteRespMessage(int operationCode, int filenameLen, int contentLen, String filename, String content) {
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
