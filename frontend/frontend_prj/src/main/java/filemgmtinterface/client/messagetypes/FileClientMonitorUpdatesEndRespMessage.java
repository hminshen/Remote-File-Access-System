package main.java.filemgmtinterface.client.messagetypes;

public class FileClientMonitorUpdatesEndRespMessage {
    private int operationCode;
    private String filename;
    private String endTime;

    public FileClientMonitorUpdatesEndRespMessage(int operationCode, String filename, String endTime) {
        this.operationCode = operationCode;
        this.filename = filename;
        this.endTime = endTime;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public String getFilename() {
        return filename;
    }

    public String getEndTime() {
        return endTime;
    }
}
