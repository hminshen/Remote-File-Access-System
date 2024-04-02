package main.java.filemgmtinterface.client.messagetypes;

public class FileClientReadRespMessage {
    private int operationCode;
    private int filenameLen;
    private int contentLen;
    public int modifiedTimeLen;

    private String filename;
    private String content;
    public String modifiedTime;


    public FileClientReadRespMessage(int operationCode, int filenameLen, int contentLen, int modifiedTimeLen, String filename, String content, String modifiedTime) {
        this.operationCode = operationCode;
        this.filenameLen = filenameLen;
        this.contentLen = contentLen;
        this.modifiedTimeLen = modifiedTimeLen;
        this.filename = filename;
        this.content = content;
        this.modifiedTime = modifiedTime;
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
    public int getModifiedTimeLen() {return modifiedTimeLen;}
    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public String getModifiedTime() {return modifiedTime;}
}
