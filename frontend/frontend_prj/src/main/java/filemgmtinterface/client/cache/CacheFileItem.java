package main.java.filemgmtinterface.client.cache;

import java.time.LocalDateTime;

public class CacheFileItem {
    public String filename;
    public LocalDateTime lastSyncTime;
    public LocalDateTime lastModifiedTime;
    public String content;

    public CacheFileItem(String filename, LocalDateTime lastSyncTime, LocalDateTime lastModifiedTime, String content) {
        this.filename = filename;
        this.lastSyncTime = lastSyncTime;
        this.lastModifiedTime = lastModifiedTime;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(LocalDateTime lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
