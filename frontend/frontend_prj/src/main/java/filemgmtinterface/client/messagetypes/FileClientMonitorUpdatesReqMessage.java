package main.java.filemgmtinterface.client.messagetypes;

public class FileClientMonitorUpdatesReqMessage {
    public int operationCode;

    public String filename;

    public int monitorInterval;

    public FileClientMonitorUpdatesReqMessage(int operationCode, String filename, int monitorInterval) {
        this.operationCode = operationCode;
        this.filename = filename;
        this.monitorInterval = monitorInterval;
    }

    public int getMonitorInterval() {
        return monitorInterval;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public String getFilename() {
        return filename;
    }
}
