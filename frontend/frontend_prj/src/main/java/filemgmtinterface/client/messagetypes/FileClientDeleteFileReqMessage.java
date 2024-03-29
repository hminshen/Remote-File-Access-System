package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDeleteFileReqMessage {
    private int operationCode;
    private String filename;

    public FileClientDeleteFileReqMessage(int operationCode, String filename){
        this.operationCode = operationCode;
        this.filename = filename;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public String getFilename() {
        return filename;
    }
}
