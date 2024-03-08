package main.java.filemgmtinterface.client.messagetypes;

public class FileClientReadRespMessage {
    public int operationCode;
    public int filename_len;
    public int content_len;
    public String filename;
    public String content;

    public int getOperationCode() {
        return operationCode;
    }

    public int getFilename_len() {
        return filename_len;
    }

    public int getContent_len() {
        return content_len;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public FileClientReadRespMessage(int operationCode, int filename_len, int content_len, String filename, String content) {
        this.operationCode = operationCode;
        this.filename_len = filename_len;
        this.content_len = content_len;
        this.filename = filename;
        this.content = content;
    }
}
