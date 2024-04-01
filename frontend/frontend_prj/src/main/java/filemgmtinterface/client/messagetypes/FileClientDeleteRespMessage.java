package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDeleteRespMessage {
    public int operationCode;
    public int filenameLen;
    public int deletedContentLen;
    public String filename;
    public String deletedContent;

    public FileClientDeleteRespMessage(int operationCode, int filenameLen, int deletedContentLen, String filename, String deletedContent) {
        this.operationCode = operationCode;
        this.filenameLen = filenameLen;
        this.deletedContentLen = deletedContentLen;
        this.filename = filename;
        this.deletedContent = deletedContent;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getFilenameLen() {
        return filenameLen;
    }

    public int getDeletedContentLen() {
        return deletedContentLen;
    }

    public String getFilename() {
        return filename;
    }

    public String getDeletedContent() {
        return deletedContent;
    }
}
