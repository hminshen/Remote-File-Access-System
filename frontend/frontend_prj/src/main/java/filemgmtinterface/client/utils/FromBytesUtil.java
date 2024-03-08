package main.java.filemgmtinterface.client.utils;

public class FromBytesUtil {
    public static int bytes_to_int(byte[] value, int offset){
        return (value[offset] & 0xFF) << 24 |
                (value[offset + 1] & 0xFF) << 16 |
                (value[offset + 2] & 0xFF) << 8 |
                (value[offset + 3] & 0xFF);
    }
}
