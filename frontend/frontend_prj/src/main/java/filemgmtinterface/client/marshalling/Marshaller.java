package main.java.filemgmtinterface.client.marshalling;

import main.java.filemgmtinterface.client.messagetypes.*;
import main.java.filemgmtinterface.client.utils.ToBytesUtil;


public class Marshaller {
    public static byte[] marshal(FileClientReadReqMessage msg){
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
        byte[] messageBytes = new byte[
                Integer.BYTES * 4 + filenameBytes.length
                ];

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

    public static byte[] marshal(FileClientWriteReqMessage msg){
        int operationCode = msg.getOperationCode();
        int offsetBytes = msg.getOffsetBytes();
        String filename = msg.getFilename();
        String writeSequence = msg.getWriteSequence();

        // Convert integers to bytes (big-endian)
        byte[] operationCodeBytes = ToBytesUtil.int_to_bytes(operationCode);
        byte[] offsetBytesBytes = ToBytesUtil.int_to_bytes(offsetBytes);
        byte[] filenameLenBytes = ToBytesUtil.int_to_bytes(filename.length());
        byte[] writeSequenceLenBytes = ToBytesUtil.int_to_bytes(writeSequence.length());

        // Convert filename and write sequence to UTF-8 bytes
        byte[] filenameBytes = filename.getBytes();
        byte[] writeSequenceBytes = writeSequence.getBytes();

        // Combine all parts into a single byte array
        byte[] messageBytes = new byte[
                Integer.BYTES * 4 + filenameBytes.length + writeSequenceBytes.length
                ];
        
        // Need to do this manually to add the bytes after each other in the byte array:
        int offset = 0;
        System.arraycopy(operationCodeBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(offsetBytesBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameLenBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(writeSequenceLenBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameBytes, 0, messageBytes, offset, filenameBytes.length);
        offset += filenameBytes.length;
        System.arraycopy(writeSequenceBytes, 0, messageBytes, offset, writeSequenceBytes.length);

        return messageBytes;
    }

    public static byte[] marshal(FileClientDeleteReqMessage msg){
        int operationCode = msg.getOperationCode();
        int offsetBytes = msg.getOffsetBytes();
        int bytesToDelete = msg.getBytesToDelete();
        String filename = msg.getFilename();

        // Convert integers to bytes (big-endian)
        byte[] operationCodeBytes = ToBytesUtil.int_to_bytes(operationCode);
        byte[] offsetBytesBytes = ToBytesUtil.int_to_bytes(offsetBytes);
        byte[] bytesToDeleteBytes = ToBytesUtil.int_to_bytes(bytesToDelete);
        byte[] filenameLenBytes = ToBytesUtil.int_to_bytes(filename.length());

        // Convert filename to UTF-8 bytes
        byte[] filenameBytes = filename.getBytes();

        // Combine all parts into a single byte array
        byte[] messageBytes = new byte[
                Integer.BYTES * 4 + filenameBytes.length
                ];
        
        // Need to do this manually to add the bytes after each other in the byte array:
        int offset = 0;
        System.arraycopy(operationCodeBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(offsetBytesBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(bytesToDeleteBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameLenBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameBytes, 0, messageBytes, offset, filenameBytes.length);

        return messageBytes;
    }

    public static byte[] marshal(FileClientCreateFileReqMessage msg){
        int operationCode = msg.getOperationCode();
        String filename = msg.getFilename();
        String content = msg.getContent();

        // Convert integers to bytes (big-endian)
        byte[] operationCodeBytes = ToBytesUtil.int_to_bytes(operationCode);
        byte[] filenameLenBytes = ToBytesUtil.int_to_bytes(filename.length());
        byte[] contentLenBytes = ToBytesUtil.int_to_bytes(content.length());

        // Convert filename and content to UTF-8 bytes
        byte[] filenameBytes = filename.getBytes();
        byte[] contentBytes = content.getBytes();

        // Combine all parts into a single byte array
        byte[] messageBytes = new byte[
                Integer.BYTES * 3 + filenameBytes.length + contentBytes.length
                ];
        
        // Need to do this manually to add the bytes after each other in the byte array:
        int offset = 0;
        System.arraycopy(operationCodeBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameLenBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(contentLenBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameBytes, 0, messageBytes, offset, filenameBytes.length);
        offset += filenameBytes.length;
        System.arraycopy(contentBytes, 0, messageBytes, offset, contentBytes.length);

        return messageBytes;
    }

    public static byte[] marshal(FileClientDeleteFileReqMessage msg){
        int operationCode = msg.getOperationCode();
        String filename = msg.getFilename();

        // Convert integers to bytes (big-endian)
        byte[] operationCodeBytes = ToBytesUtil.int_to_bytes(operationCode);
        byte[] filenameLenBytes = ToBytesUtil.int_to_bytes(filename.length());

        // Convert filename to UTF-8 bytes
        byte[] filenameBytes = filename.getBytes();

        // Combine all parts into a single byte array
        byte[] messageBytes = new byte[
                Integer.BYTES * 2 + filenameBytes.length
                ];
        
        // Need to do this manually to add the bytes after each other in the byte array:
        int offset = 0;
        System.arraycopy(operationCodeBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameLenBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameBytes, 0, messageBytes, offset, filenameBytes.length);

        return messageBytes;
    }

    public static byte[] marshal(FileClientMonitorUpdatesReqMessage msg){
        int operationCode = msg.getOperationCode();
        String filename = msg.getFilename();
        int monitorInterval = msg.getMonitorInterval();

        // Convert integers to bytes (big-endian)
        byte[] operationCodeBytes = ToBytesUtil.int_to_bytes(operationCode);
        byte[] filenameLenBytes = ToBytesUtil.int_to_bytes(filename.length());
        byte[] monitorIntervalBytes = ToBytesUtil.int_to_bytes((monitorInterval));


        // Convert filename to UTF-8 bytes
        byte[] filenameBytes = filename.getBytes();

        // Combine all parts into a single byte array
        byte[] messageBytes = new byte[
                Integer.BYTES * 3 + filenameBytes.length
                ];

        // Need to do this manually to add the bytes after each other in the byte array:
        int offset = 0;
        System.arraycopy(operationCodeBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(monitorIntervalBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameLenBytes, 0, messageBytes, offset, Integer.BYTES);
        offset += Integer.BYTES;
        System.arraycopy(filenameBytes, 0, messageBytes, offset, filenameBytes.length);

        return messageBytes;
    }
}
