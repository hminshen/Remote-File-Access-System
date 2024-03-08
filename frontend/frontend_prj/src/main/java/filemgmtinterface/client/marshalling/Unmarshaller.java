package main.java.filemgmtinterface.client.marshalling;

import main.java.filemgmtinterface.client.messagetypes.FileClientReadRespMessage;
import main.java.filemgmtinterface.client.utils.FromBytesUtil;
public class Unmarshaller {
    public static int unmarshal_op_code(byte[] msg){
        return FromBytesUtil.bytes_to_int(msg, 0);
    }
    public static FileClientReadRespMessage unmarshallReadResp(byte[] msg){
        int operationCode = FromBytesUtil.bytes_to_int(msg, 0);
        int filenameLen = FromBytesUtil.bytes_to_int(msg, 4);
        int contentLen = FromBytesUtil.bytes_to_int(msg, 8);

        String filename = new String(msg, 12, filenameLen);
        String content = new String(msg, 12 + filenameLen, contentLen);

        FileClientReadRespMessage response = new FileClientReadRespMessage(operationCode, filenameLen, contentLen, filename, content);

        return response;
    }
}
