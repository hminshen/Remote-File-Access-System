package main.java.filemgmtinterface.client;
import main.java.filemgmtinterface.client.messagetypes.FileClientReadReqMessage;
import main.java.filemgmtinterface.client.marshalling.Marshaller;
import main.java.filemgmtinterface.client.marshalling.Unmarshaller;
import main.java.filemgmtinterface.client.messagetypes.FileClientReadRespMessage;

import java.net.*;

public class Client {
    private static String SERVER_ADDRESS = "127.0.0.1";
    private static int SERVER_PORT = 5000;

    public static void showUI(){
        System.out.println("Hello world!");
        System.out.println(SERVER_ADDRESS);
    }
    public static void sendRequest(int operation_code){
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            // Operation code for file read
            int operationCode = operation_code;
            int offsetBytes = 2;
            int bytesToRead = 4;
            String filename = "test.txt";

            System.out.println("Sending file read request for " + bytesToRead + " bytes from file name: " + filename + " with offset bytes of " + offsetBytes + "...");

            // Create the FileClientReadMessage object
            FileClientReadReqMessage msg = new FileClientReadReqMessage(operationCode, offsetBytes, bytesToRead, filename);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length, InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Send the request to the server
            clientSocket.send(requestPacket);

            // Receive response from the server
            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(responsePacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);
            // Means read:
            if (op_code == 1){
                // Unmarshal the response
                FileClientReadRespMessage response = Unmarshaller.unmarshallReadResp(buffer);

                // Print the received filename and content
                System.out.println("Received filename: " + response.getFilename());
                System.out.println("File Contents: " + response.getContent());
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
