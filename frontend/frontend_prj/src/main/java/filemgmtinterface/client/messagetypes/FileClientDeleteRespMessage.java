package main.java.filemgmtinterface.client.messagetypes;

public class FileClientDeleteRespMessage {
    public int operationCode;
    public int filename_len;
    public int content_deleted_len;
    public String filename;
    public String content_deleted;

    public int getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }

    public int getFilename_len() {
        return filename_len;
    }

    public void setFilename_len(int filename_len) {
        this.filename_len = filename_len;
    }

    public int getContent_deleted_len() {
        return content_deleted_len;
    }

    public void setContent_deleted_len(int content_deleted_len) {
        this.content_deleted_len = content_deleted_len;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent_deleted() {
        return content_deleted;
    }

    public void setContent_deleted(String content_deleted) {
        this.content_deleted = content_deleted;
    }

    public FileClientDeleteRespMessage(int operationCode, int filename_len, int content_deleted_len, String filename, String content_deleted) {
        this.operationCode = operationCode;
        this.filename_len = filename_len;
        this.content_deleted_len = content_deleted_len;
        this.filename = filename;
        this.content_deleted = content_deleted;
    }
}
