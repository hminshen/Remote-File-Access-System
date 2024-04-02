package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDirReqMessage {
    private int requestId;
    private int operationCode;
    private int dirNameLen;
    private String dirName;

    public FileClientDirReqMessage(int requestId, int operationCode, int dirNameLen, String dirName) {
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.dirNameLen = dirNameLen;
        this.dirName = dirName;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public int getOperationCode() {
        return this.operationCode;
    }

    public int getDirNameLen() {
        return this.dirNameLen;
    }

    public String getDirName() {
        return this.dirName;
    }
}
