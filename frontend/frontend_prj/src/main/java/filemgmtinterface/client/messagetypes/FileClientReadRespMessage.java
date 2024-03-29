package main.java.filemgmtinterface.client.messagetypes;

public class FileClientReadRespMessage {
    public int operationCode;
    public int filenameLen;
    public int contentLen;
    public String filename;
    public String content;

    public FileClientReadRespMessage(int operationCode, int filenameLen, int contentLen, String filename, String content) {
        this.operationCode = operationCode;
        this.filenameLen = filenameLen;
        this.contentLen = contentLen;
        this.filename = filename;
        this.content = content;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getFilenameLen() {
        return filenameLen;
    }

    public int getContentLen() {
        return contentLen;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }
}
