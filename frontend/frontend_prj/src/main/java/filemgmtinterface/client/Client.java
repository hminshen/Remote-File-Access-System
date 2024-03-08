package main.java.filemgmtinterface.client;
import main.java.filemgmtinterface.client.messagetypes.FileClientReadReqMessage;
import main.java.filemgmtinterface.client.marshalling.Marshaller;
import main.java.filemgmtinterface.client.marshalling.Unmarshaller;
import main.java.filemgmtinterface.client.messagetypes.FileClientReadRespMessage;

import java.net.*;
import java.util.Scanner;

public class Client {
    private static String SERVER_ADDRESS = "127.0.0.1";
    private static int SERVER_PORT = 5000;

    public static void showUI(){
        Scanner myObj = new Scanner(System.in);
        int choice = 0;
        do {
            String UIMessage = "Welcome to the Remote File Management system!\n"
                    + "What would you like to do? (Input your choice number):\n"
                    + "1. Read a file\n"
                    + "2. Write to a file\n"
                    + "3. Append to a file\n"
                    + "4. Delete a file\n"
                    + "0. Exit\n";
            System.out.println(UIMessage);
            try {
                choice = myObj.nextInt();
                // Read operation:
                if (choice == 1) {
                    try{
                        System.out.println("Input the filename that you want to read:");
                        myObj.nextLine();
                        String filename = myObj.nextLine();
                        System.out.println("Input the offset bytes to start reading from:");
                        int offset_bytes = myObj.nextInt();
                        System.out.println("Input the number of bytes to read:");
                        int num_bytes = myObj.nextInt();
                        sendReadRequest(1, offset_bytes, num_bytes, filename);
                    }
                    catch (Exception e){

                    }

                }
                else if (choice == 0){
                    System.out.println("Thank you for using our file management system!");
                }
                else{
                    System.out.println("Please choose a valid number");
                }
            } catch (Exception e) {
                //System.out.println(e);
                System.out.println("Please input a valid choice");
                choice = -1;
                myObj.nextLine();
            }
        } while (choice != 0);

    }
    public static void sendReadRequest(int operationCode, int offsetBytes, int bytesToRead, String filename){
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
            System.out.println("Received opcode: " + op_code + "..... Checking Read Response Message...");
            // Means read response:
            if (op_code == 1){
                // Unmarshal the response
                FileClientReadRespMessage response = Unmarshaller.unmarshallReadResp(buffer);

                // Print the received filename and content
                System.out.println(response.getFilename() + " Recieved!\n");
                System.out.println("File Contents: " + response.getContent());
                System.out.println("\n\n");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
