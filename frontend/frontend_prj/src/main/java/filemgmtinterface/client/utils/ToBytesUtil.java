package main.java.filemgmtinterface.client.utils;

public class ToBytesUtil {
    // Util to convert integer to bytes via bit shifting and bit masking:
    public static byte[] int_to_bytes(int value){
        byte[] bytes = new byte[Integer.BYTES];
        // Extracts the MSB to place it at lowest mem addr (big endian) --> need shift 24 bits since int is 4 bytes:
        bytes[0] = (byte) ((value >> 24) & 0xFF); //Mask with 0xFF to only retain the lowest 8 bits/1 byte
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        // LSB Stored at last pos
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }

}
