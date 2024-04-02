package main.java.filemgmtinterface.client.messagetypes;

public class FileClientGetAttrReqMessage {
    public int operationCode;

    public String filename;
    public String fileAttribute;
    public FileClientGetAttrReqMessage(int operationCode, String filename, String fileAttribute) {
        this.operationCode = operationCode;
        this.filename = filename;
        this.fileAttribute = fileAttribute;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public String getFilename() {
        return filename;
    }

    public String getFileAttribute() {
        return fileAttribute;
    }
}
