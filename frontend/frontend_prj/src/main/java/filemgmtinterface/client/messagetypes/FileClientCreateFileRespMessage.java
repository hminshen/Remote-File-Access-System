package main.java.filemgmtinterface.client.messagetypes;

public class FileClientCreateFileRespMessage {
    public int operationCode;
    public int filenameLen;
    public int contentLen;
    public String filename;
    
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
