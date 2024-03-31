package main.java.filemgmtinterface.client.marshalling;

import main.java.filemgmtinterface.client.messagetypes.*;
import main.java.filemgmtinterface.client.utils.FromBytesUtil;


public class Unmarshaller {
    // Op code can be used to identify the kindof response message, so we first unmarshal just the opcode:
    public static int unmarshal_op_code(byte[] msg){
        return FromBytesUtil.bytes_to_int(msg, 0);
    }

    // Unmarshalling for when opcode is 1 --> file read response:
    public static ErrorMessage unmarshallErrorResp(byte[] msg) {
        int errorCode = FromBytesUtil.bytes_to_int(msg, 0);
        int errMsgLen = FromBytesUtil.bytes_to_int(msg, 4);

        String errMsg = new String(msg, 8, errMsgLen);

        ErrorMessage response = new ErrorMessage(errorCode, errMsg);

        return response;
    }

    public static FileClientReadRespMessage unmarshallReadResp(byte[] msg){
        int operationCode = FromBytesUtil.bytes_to_int(msg, 0);
        int filenameLen = FromBytesUtil.bytes_to_int(msg, 4);
        int contentLen = FromBytesUtil.bytes_to_int(msg, 8);

        String filename = new String(msg, 12, filenameLen);
        // Content stored after filename bytes:
        String content = new String(msg, 12 + filenameLen, contentLen);

        FileClientReadRespMessage response = new FileClientReadRespMessage(operationCode, filenameLen, contentLen, filename, content);

        return response;
    }

    public static FileClientWriteRespMessage unmarshallWriteResp(byte[] msg){
        int operationCode = FromBytesUtil.bytes_to_int(msg, 0);
        int filenameLen = FromBytesUtil.bytes_to_int(msg, 4);
        int contentLen = FromBytesUtil.bytes_to_int(msg, 8);

        String filename = new String(msg, 12, filenameLen);

        FileClientWriteRespMessage response = new FileClientWriteRespMessage(operationCode, filenameLen, contentLen, filename);

        return response;
    }

    public static FileClientMonitorUpdatesAckRespMessage unmarshallMonitorUpdatesAckResp(byte[] msg){
        int operationCode = FromBytesUtil.bytes_to_int(msg, 0);
        int monitoringInterval = FromBytesUtil.bytes_to_int(msg, 4);
        int filenameLen = FromBytesUtil.bytes_to_int(msg, 8);
        int startTimeLen = FromBytesUtil.bytes_to_int(msg, 12);

        String filename = new String(msg, 16, filenameLen);
        String startTime = new String(msg, 16 + filenameLen, startTimeLen);

        FileClientMonitorUpdatesAckRespMessage response = new FileClientMonitorUpdatesAckRespMessage(operationCode, monitoringInterval, startTime, filename);

        return response;
    }

    public static FileClientDeleteRespMessage unmarshallDeleteResp(byte[] msg){
        int operationCode = FromBytesUtil.bytes_to_int(msg, 0);
        int filenameLen = FromBytesUtil.bytes_to_int(msg, 4);
        int content_deletedLen = FromBytesUtil.bytes_to_int(msg, 8);

        String filename = new String(msg, 12, filenameLen);
        // Content deleted:
        String content_deleted = new String(msg, 12 + filenameLen, content_deletedLen);

        FileClientDeleteRespMessage response = new FileClientDeleteRespMessage(operationCode, filenameLen, content_deletedLen, filename, content_deleted);

        return response;
    }

    public static FileClientCreateFileRespMessage unmarshallCreateFileResp(byte[] msg){
        int operationCode = FromBytesUtil.bytes_to_int(msg, 0);
        int filenameLen = FromBytesUtil.bytes_to_int(msg, 4);
        int contentLen = FromBytesUtil.bytes_to_int(msg, 8);
        String filename = new String(msg, 8, filenameLen);

        FileClientCreateFileRespMessage response = new FileClientCreateFileRespMessage(operationCode, filenameLen, contentLen, filename);

        return response;
    }

    public static FileClientDeleteFileRespMessage unmarshallDeleteFileResp(byte[] msg){
        int operationCode = FromBytesUtil.bytes_to_int(msg, 0);
        int filenameLen = FromBytesUtil.bytes_to_int(msg, 4);
        String filename = new String(msg, 8, filenameLen);

        FileClientDeleteFileRespMessage response = new FileClientDeleteFileRespMessage(operationCode, filenameLen, filename);

        return response;
    }
}
