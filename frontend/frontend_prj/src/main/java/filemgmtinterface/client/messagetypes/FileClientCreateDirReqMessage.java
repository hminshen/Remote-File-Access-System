package main.java.filemgmtinterface.client.messagetypes;

public class FileClientCreateDirReqMessage {
    private int operationCode;
    private String dir_name;
    
    public FileClientCreateDirReqMessage(int operationCode, String dir_name) {
        this.operationCode = operationCode;
        this.dir_name = dir_name;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }

    public String getDir_name() {
        return dir_name;
    }

    public void setDir_name(String dir_name) {
        this.dir_name = dir_name;
    }

}
