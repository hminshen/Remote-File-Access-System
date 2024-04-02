package main.java.filemgmtinterface.client.messagetypes;

public class FileClientMonitorUpdatesRespMessage {
    private int operationCode;
    private String filename;
    private String updateTime;
    private String updatedContents;

    public FileClientMonitorUpdatesRespMessage(int operationCode, String filename, String updateTime, String updatedContents) {
        this.operationCode = operationCode;
        this.filename = filename;
        this.updateTime = updateTime;
        this.updatedContents = updatedContents;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public String getFilename() {
        return filename;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getUpdatedContents() {
        return updatedContents;
    }
}
