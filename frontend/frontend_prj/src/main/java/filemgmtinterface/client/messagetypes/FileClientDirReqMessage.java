package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDirReqMessage {
    private int operationCode;
    private int dirNameLen;
    private String dirName;

    public FileClientDirReqMessage(int operationCode, int dirNameLen, String dirName) {
        this.operationCode = operationCode;
        this.dirNameLen = dirNameLen;
        this.dirName = dirName;
    }

    public int getOperationCode() {
        return this.operationCode;
    }

    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }

    public int getDirNameLen() {
        return this.dirNameLen;
    }

    public void setDirNameLen(int dirNameLen) {
        this.dirNameLen = dirNameLen;
    }

    public String getDirName() {
        return this.dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

}
