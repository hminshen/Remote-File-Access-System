package main.java.filemgmtinterface.client.messagetypes;

public class FileClientWriteReqMessage {
    public int operationCode;
    public int offsetBytes;
    public String filename;
    public String writeSequence;

    public FileClientWriteReqMessage(int operationCode, int offsetBytes, String filename, String writeSequence){
        this.operationCode = operationCode;
        this.offsetBytes = offsetBytes;
        this.filename = filename;
        this.writeSequence = writeSequence;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getOffsetBytes() {
        return offsetBytes;
    }

    public String getFilename() {
        return filename;
    }

    public String getWriteSequence() {
        return writeSequence;
    }
}
