package main.java.filemgmtinterface.client.messagetypes;

import java.util.Objects;

public class FileClientReadReqMessage {
    private int requestId;
    private int operationCode;
    private int offsetBytes;
    private int bytesToRead;
    private String filename;

    public FileClientReadReqMessage(int requestId, int operationCode, int offsetBytes, int bytesToRead, String filename) {
        this.requestId = requestId;
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.bytesToRead = bytesToRead;
        this.filename = filename;
    }
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    public int getRequestId() {
        return requestId;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getOffsetBytes() {
        return offsetBytes;
    }

    public int getBytesToRead() {
        return bytesToRead;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        FileClientReadReqMessage msg = (FileClientReadReqMessage) obj;
        return this.getOperationCode() == msg.getOperationCode() &&
                this.getOffsetBytes() == msg.getOffsetBytes() &&
                this.getBytesToRead() == msg.getBytesToRead() &&
                this.getFilename().equals(msg.getFilename());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.operationCode, this.offsetBytes, this.bytesToRead, this.filename);
    }
}
