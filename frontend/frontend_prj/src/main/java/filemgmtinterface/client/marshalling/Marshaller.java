package main.java.filemgmtinterface.client.marshalling;

import main.java.filemgmtinterface.client.messagetypes.FileClientCreateDirReqMessage;
import main.java.filemgmtinterface.client.messagetypes.FileClientReadReqMessage;
import main.java.filemgmtinterface.client.utils.ToBytesUtil;

public class Marshaller {
    public static byte[] marshal(FileClientReadReqMessage msg) {
        int operationCode = msg.getOperationCode();
        int offsetBytes = msg.getOffsetBytes();
        int bytesToRead = msg.getBytesToRead();
        String filename = msg.getFilename();

        // Convert integers to bytes (big-endian)
        byte[] operationCodeBytes = ToBytesUtil.int_to_bytes(operationCode);
        byte[] offsetBytesBytes = ToBytesUtil.int_to_bytes(offsetBytes);
        byte[] bytesToReadBytes = ToBytesUtil.int_to_bytes(bytesToRead);
        byte[] filenameLenBytes = ToBytesUtil.int_to_bytes(filename.length());

        // Convert filename to UTF-8 bytes
        byte[] filenameBytes = filename.getBytes();

        // Combine all parts into a single byte array
        byte[] messageBytes = new byte[Integer.BYTES * 4 + filenameBytes.length];
        // Need to do this manually to add the bytes after each other in the byte array:
        int offset = 0;
        System.arraycopy(operationCodeBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(offsetBytesBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(bytesToReadBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameLenBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameBytes, 0, messageBytes, offset, filenameBytes.length);

        return messageBytes;
    }

    public static byte[] marshal(FileClientCreateDirReqMessage msg) {
        int opCode = msg.getOperationCode();
        int dirNameLen = msg.getDirNameLen();
        String dirName = msg.getDirName();

        // Convert integers to bytes (Big-endian)
        byte[] opCodeBytes = ToBytesUtil.int_to_bytes(opCode);
        byte[] dirNameLenBytes = ToBytesUtil.int_to_bytes(dirNameLen);

        // Convert strings to bytes (UTF-8)
        byte[] dirNameBytes = dirName.getBytes();

        // Initalise messageByte with fixed length
        byte[] messageByte = new byte[Integer.BYTES * 4 + dirNameBytes.length];
        // Need to do this manually to add the bytes after each other in the byte array:
        int offset = 0;
        System.arraycopy(opCodeBytes, 0, messageByte, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(dirNameLenBytes, 0, messageByte, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(dirNameBytes, 0, messageByte, offset, dirNameBytes.length);

        return messageByte;
    }
}

// Create "Create direcotry "marshall function to the server
