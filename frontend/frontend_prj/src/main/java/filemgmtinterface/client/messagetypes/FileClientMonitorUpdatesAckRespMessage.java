package main.java.filemgmtinterface.client.messagetypes;

public class FileClientMonitorUpdatesAckRespMessage {
    public int operationCode;
    public int monitoringInterval;
    public String startTime;

    public String filename;

    public FileClientMonitorUpdatesAckRespMessage(int operationCode, int monitoringInterval, String startTime, String filename) {
        this.operationCode = operationCode;
        this.monitoringInterval = monitoringInterval;
        this.startTime = startTime;
        this.filename = filename;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getMonitoringInterval() {
        return monitoringInterval;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFilename() {
        return filename;
    }
}
