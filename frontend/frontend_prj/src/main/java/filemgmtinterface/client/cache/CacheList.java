package main.java.filemgmtinterface.client.cache;

import main.java.filemgmtinterface.client.messagetypes.FileClientReadReqMessage;

import java.util.HashMap;

public class CacheList {
    public HashMap<FileClientReadReqMessage, CacheFileItem> cacheMap;

    public CacheList(HashMap<FileClientReadReqMessage, CacheFileItem> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public HashMap<FileClientReadReqMessage, CacheFileItem> getCacheMap() {
        return cacheMap;
    }

    public CacheFileItem getCacheFileItem(FileClientReadReqMessage msg){
        return cacheMap.getOrDefault(msg, null);
    }

    public void addCacheFileItem(FileClientReadReqMessage msg, CacheFileItem cacheFile){
        cacheMap.put(msg, cacheFile);
    }

    public void setCacheFileItem(FileClientReadReqMessage msg, CacheFileItem updatedCacheFile){
        cacheMap.replace(msg, updatedCacheFile);
    }
}
