package main.java.filemgmtinterface.client;

import main.java.filemgmtinterface.client.messagetypes.ErrorMessage;
import main.java.filemgmtinterface.client.messagetypes.FileClientCreateDirReqMessage;
import main.java.filemgmtinterface.client.messagetypes.FileClientCreateDirRespMessage;
import main.java.filemgmtinterface.client.messagetypes.FileClientReadReqMessage;
import main.java.filemgmtinterface.client.marshalling.Marshaller;
import main.java.filemgmtinterface.client.marshalling.Unmarshaller;
import main.java.filemgmtinterface.client.messagetypes.FileClientReadRespMessage;

import java.net.*;
import java.util.Scanner;

public class Client {
    public String SERVER_ADDRESS;
    public int SERVER_PORT;

    public Client(String addr, int port) {
        SERVER_ADDRESS = addr;
        SERVER_PORT = port;
    }

    public void sendReadRequest(int operationCode, int offsetBytes, int bytesToRead, String filename) {
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file read request for " + bytesToRead + " bytes from file name: " + filename
                    + " with offset bytes of " + offsetBytes + "...\n");

            // Create the FileClientReadMessage object
            FileClientReadReqMessage msg = new FileClientReadReqMessage(operationCode, offsetBytes, bytesToRead,
                    filename);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Send the request to the server
            clientSocket.send(requestPacket);

            // Receive response from the server
            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(responsePacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);
            // Means read response:
            if (op_code == 1) {
                System.out.println("Checking File Read Response...");
                // Unmarshal the response
                FileClientReadRespMessage response = Unmarshaller.unmarshallReadResp(buffer);

                // Print the received filename and content
                System.out.println(response.getFilename() + " Recieved!\n");
                System.out.println("File Contents: " + response.getContent());
                System.out.println("\n\n");
            } else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code:" + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    // Send create directory request

    public void sendCreateDirRequest(int opCode, int dirNameLen, String dirName) {
        // Create UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending create directory request...");
            System.out.println("Directory Name: " + dirName + " ;Directory String Length: " + dirNameLen);

            // Create the FileClientReadMessage object
            FileClientCreateDirReqMessage msg = new FileClientCreateDirReqMessage(opCode, dirNameLen, dirName);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Send the request to the server
            clientSocket.send(requestPacket);

            // Receive response from the server
            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(responsePacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);

            // Means Create directory:
            if (op_code == 3) {
                System.out.println("Checking File Read Response...");
                // Unmarshal the response
                FileClientCreateDirRespMessage response = Unmarshaller.unmarshallCreateDirRespo(buffer);

                // Print the received filename and content
                System.out.println("Created directory " + response.getDirName());
                System.out.println("\n\n");
            } else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code:" + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
