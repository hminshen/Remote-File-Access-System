package main.java.filemgmtinterface.client.messagetypes;

public class FileClientMonitorUpdatesReqMessage {
    private int requestId;
    private int operationCode;
    private String filename;
    private int monitorInterval;

    public FileClientMonitorUpdatesReqMessage(int requestId, int operationCode, String filename, int monitorInterval) {
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.filename = filename;
        this.monitorInterval = monitorInterval;
    }

    public int getRequestId() {
        return requestId;
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
