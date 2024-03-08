package main.java.filemgmtinterface.client.utils;

public class ToBytesUtil {
    public static byte[] int_to_bytes(int value){
        byte[] bytes = new byte[Integer.BYTES];
        bytes[0] = (byte) ((value >> 24) & 0xFF);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }

}
