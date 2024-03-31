package main.java.filemgmtinterface.client;
import main.java.filemgmtinterface.client.messagetypes.*;
import main.java.filemgmtinterface.client.marshalling.*;

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
                System.out.println(response.getFilename() + " Received!\n");
                System.out.println("File Contents: " + response.getContent());
                System.out.println("\n\n");
            }
            else{
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void sendWriteRequest(int operationCode, int offsetBytes, String filename, String writeSequence){
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file write request for file name: " + filename + " starting from offset bytes of " + offsetBytes + "...\n");

            // Create the FileClientWriteMessage object
            FileClientWriteReqMessage msg = new FileClientWriteReqMessage(operationCode, offsetBytes, filename, writeSequence);

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
            // Means write response:
            if (op_code == 2){
                System.out.println("Checking File Write Response...");
                // Unmarshal the response
                FileClientWriteRespMessage response = Unmarshaller.unmarshallWriteResp(buffer);

                // Print response
                System.out.println("Successfully wrote " + response.getContent_len() + " bytes to " + response.getFilename() + "!\n");
                System.out.println("\n\n");
            }
            else{
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void sendMonitorRequest(int operationCode, String filename, int monitorInterval){
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending monitor file updates request for file name: " + filename + "...\n");

            // Create the FileClientDeleteFileMessage object
            FileClientMonitorUpdatesReqMessage msg = new FileClientMonitorUpdatesReqMessage(operationCode, filename, monitorInterval);

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
            // Means success monitoring ack response:
            if (op_code == 3){
                System.out.println("Checking Monitoring File Ack Response...");
                // Unmarshal the response
                FileClientMonitorUpdatesAckRespMessage response = Unmarshaller.unmarshallMonitorUpdatesAckResp(buffer);

                // Print response
                System.out.println("Successfully setup monitoring updates for File:" + response.getFilename() + " at time: " + response.getStartTime() + "!\n");
                System.out.println("Monitoring Interval: " + response.getMonitoringInterval());
                System.out.println("\n\n");
            }
            else{
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
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
            System.out.println("Sending delete content request for " + bytesToDelete + " bytes from file name: " + filename + " starting from offset bytes of " + offsetBytes + "...\n");

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

                // Print response
                System.out.println("Successfully deleted " + response.getDeletedContentLen() + " bytes from " + response.getFilename() + "!\n");
                System.out.println("File Contents Deleted: " + response.getDeletedContent());
                System.out.println("\n\n");
            }
            else{
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void sendCreateFileRequest(int operationCode, String filename, String content){
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file create request for file name: " + filename + "...\n");

            // Create the FileClientCreateFileMessage object
            FileClientCreateFileReqMessage msg = new FileClientCreateFileReqMessage(operationCode, filename, content);

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
            // Means create file response:
            if (op_code == 5){
                System.out.println("Checking File Create Response...");
                // Unmarshal the response
                FileClientCreateFileRespMessage response = Unmarshaller.unmarshallCreateFileResp(buffer);

                // Print response
                System.out.println("Successfully created file: " + response.getFilename() + " with " + response.getContentLen() + " bytes written!\n");
                System.out.println("\n\n");
            }
            else{
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void sendDeleteFileRequest(int operationCode, String filename){
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending delete file request for file name: " + filename + "...\n");

            // Create the FileClientDeleteFileMessage object
            FileClientDeleteFileReqMessage msg = new FileClientDeleteFileReqMessage(operationCode, filename);

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
            // Means delete file response:
            if (op_code == 6){
                System.out.println("Checking File Delete Response...");
                // Unmarshal the response
                FileClientDeleteFileRespMessage response = Unmarshaller.unmarshallDeleteFileResp(buffer);

                // Print response
                System.out.println("Successfully deleted file: " + response.getFilename() + " from server!\n");
                System.out.println("\n\n");
            }
            else{
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
