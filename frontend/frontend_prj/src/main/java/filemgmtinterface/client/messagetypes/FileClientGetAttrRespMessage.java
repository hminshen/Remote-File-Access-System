package main.java.filemgmtinterface.client.messagetypes;

public class FileClientGetAttrRespMessage {
    public int operationCode;

    public String filename;
    public String fileAttribute;

    public String fileAttributeValue;

    public FileClientGetAttrRespMessage(int operationCode, String filename, String fileAttribute, String fileAttributeValue) {
        this.operationCode = operationCode;
        this.filename = filename;
        this.fileAttribute = fileAttribute;
        this.fileAttributeValue = fileAttributeValue;
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

    public String getFileAttributeValue() {
        return fileAttributeValue;
    }
}
