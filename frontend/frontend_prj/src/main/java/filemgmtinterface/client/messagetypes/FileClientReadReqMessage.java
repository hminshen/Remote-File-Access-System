package main.java.filemgmtinterface.client.messagetypes;

import java.util.Objects;

public class FileClientReadReqMessage {


    public int operationCode;
    public int offsetBytes;
    public int bytesToRead;
    public String filename;

    public FileClientReadReqMessage(int operationCode, int offsetBytes, int bytesToRead, String filename) {
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.bytesToRead = bytesToRead;
        this.filename = filename;
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
