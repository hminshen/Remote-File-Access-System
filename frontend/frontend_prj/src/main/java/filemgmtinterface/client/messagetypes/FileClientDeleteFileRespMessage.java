package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDeleteFileRespMessage {
    private int operationCode;
    private int filenameLen;
    private String filename;
    
    public FileClientDeleteFileRespMessage(int operationCode, int filenameLen, String filename){
        this.operationCode = operationCode;
        this.filenameLen = filenameLen;
        this.filename = filename;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getFilenameLen() {
        return filenameLen;
    }

    public String getFilename() {
        return filename;
    }
}
