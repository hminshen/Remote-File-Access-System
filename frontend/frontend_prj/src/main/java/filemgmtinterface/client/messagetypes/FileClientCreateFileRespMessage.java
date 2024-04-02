package main.java.filemgmtinterface.client.messagetypes;

public class FileClientCreateFileRespMessage {
    private int operationCode;
    private int filenameLen;
    private int contentLen;
    private String filename;
    
    public FileClientCreateFileRespMessage(int operationCode, int filenameLen, int contentLen, String filename){
        this.operationCode = operationCode;
        this.filenameLen = filenameLen;
        this.contentLen = contentLen;
        this.filename = filename;
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
}
