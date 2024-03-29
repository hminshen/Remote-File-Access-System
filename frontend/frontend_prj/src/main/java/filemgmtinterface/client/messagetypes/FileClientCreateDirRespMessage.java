package main.java.filemgmtinterface.client.messagetypes;

import java.util.Objects;

public class FileClientCreateDirRespMessage {
    private int operationCode;
    private int dirNameLen;
    private String dirName;

    public FileClientCreateDirRespMessage(int operationCode, int dirNameLen, String dirName) {
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
