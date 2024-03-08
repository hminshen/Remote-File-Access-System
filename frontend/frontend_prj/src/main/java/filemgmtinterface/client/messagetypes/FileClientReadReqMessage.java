package main.java.filemgmtinterface.client.messagetypes;

public class FileClientReadReqMessage {
    public int operationCode;
    public int offsetBytes;

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

    public int bytesToRead;
    public String filename;
    public FileClientReadReqMessage(int opCode, int offBytes, int bytes2Read, String file_name){
        operationCode = opCode;
        offsetBytes = offBytes;
        bytesToRead = bytes2Read;
        filename = file_name;
    }
}
