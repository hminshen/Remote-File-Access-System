package main.java.filemgmtinterface.client;
import main.java.filemgmtinterface.client.messagetypes.*;
import main.java.filemgmtinterface.client.marshalling.Marshaller;
import main.java.filemgmtinterface.client.marshalling.Unmarshaller;

import java.net.*;
import java.util.Scanner;

public class Client {
    public String SERVER_ADDRESS;
    public int SERVER_PORT;
    public Client(String addr, int port){
        SERVER_ADDRESS = addr;
        SERVER_PORT = port;
    }

    public void sendReadRequest(int operationCode, int offsetBytes, int bytesToRead, String filename){
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file read request for " + bytesToRead + " bytes from file name: " + filename + " with offset bytes of " + offsetBytes + "...\n");

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
            // Means read response:
            if (op_code == 1){
                System.out.println("Checking File Read Response...");
                // Unmarshal the response
                FileClientReadRespMessage response = Unmarshaller.unmarshallReadResp(buffer);

                // Print the received filename and content
                System.out.println(response.getFilename() + " Recieved!\n");
                System.out.println("File Contents: " + response.getContent());
                System.out.println("\n\n");
            }
            else{
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code:" + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void sendDeleteRequest(int operationCode, int offsetBytes, int bytesToDelete, String filename){
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file delete request for " + bytesToDelete + " bytes from file name: " + filename + " starting from offset bytes of " + offsetBytes + "...\n");

            // Create the FileClientDeleteMessage object
            FileClientDeleteReqMessage msg = new FileClientDeleteReqMessage(operationCode, offsetBytes, bytesToDelete, filename);

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
            // Means delete response:
            if (op_code == 4){
                System.out.println("Checking File Delete contents Response...");
                // Unmarshal the response
                FileClientDeleteRespMessage response = Unmarshaller.unmarshallDeleteResp(buffer);

                // Print the received filename and content
                System.out.println("Successful deletion of content from " + response.getFilename() + "!\n");
                System.out.println("File Contents Deleted: " + response.getContent_deleted());
                System.out.println("\n\n");
            }
            else{
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code:" + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
