package main.java.filemgmtinterface.client;
import main.java.filemgmtinterface.client.messagetypes.*;
import main.java.filemgmtinterface.client.marshalling.*;
import java.net.*;

public class Client {
    public String SERVER_ADDRESS;
    public int SERVER_PORT;

    private final int TIMEOUT = 1000;   // 1000 ms
    private final int MAX_RETRIES = 3;

    private int requestId = 0;

    public Client(String addr, int port) {
        SERVER_ADDRESS = addr;
        SERVER_PORT = port;
    }

    private byte[] sendRequestToServer(DatagramSocket clientSocket, DatagramPacket requestPacket) {
        int numberOfTries = 0;
        byte[] buffer = new byte[1024];
        while (numberOfTries < MAX_RETRIES) {
            try {
                // Send the request to the server
                System.out.println("Sending request from client at IP address: " + clientSocket.getLocalAddress() + ", Port: " + clientSocket.getLocalPort());
                System.out.println("Client request ID: " + requestId);
                clientSocket.send(requestPacket);

                // Set the timeout for socket
                clientSocket.setSoTimeout(TIMEOUT);

                // Receive response from the server
                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(responsePacket);

                // int op_code = Unmarshaller.unmarshal_op_code(buffer);
                // System.out.println("Received opcode: " + op_code);
                requestId += 1; // Increment request ID
                return buffer;

            } catch (SocketTimeoutException e) {
                numberOfTries += 1;

                System.out.println("Server Response Timeout; Number of Tries: " + numberOfTries);
                System.out.println("Retrying...");

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        // Error encountered
        int errorCode = 901;
        String errorString = "Maxmum number of retries reached. Connection aborted; Please retry operation";
        ErrorMessage errMsg = new ErrorMessage(errorCode, errorString);
        byte[] marshalledMessage = Marshaller.marshal(errMsg);
        requestId += 1; // Increment request ID

        return marshalledMessage;
    }
    

    public void sendReadRequest(int operationCode, int offsetBytes, int bytesToRead, String filename) {
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file read request for " + bytesToRead + " bytes from file name: " + filename
                    + " with offset bytes of " + offsetBytes + "...\n");

            // Create the FileClientReadMessage object
            FileClientReadReqMessage msg = new FileClientReadReqMessage(requestId, operationCode, offsetBytes, bytesToRead,
                    filename);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Initalise buffer
            byte[] buffer = new byte[1024];
            // Send request packet to server through socket
            buffer = sendRequestToServer(clientSocket, requestPacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);
            // Means read response:
            if (op_code == 1) {
                System.out.println("Checking File Read Response...");
                // Unmarshal the response
                FileClientReadRespMessage response = Unmarshaller.unmarshallReadResp(buffer);

                // Print the received filename and content
                System.out.println(response.getFilename() + " Received!\n");
                System.out.println("File Contents: " + response.getContent());
                System.out.println("\n\n");
            } else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void sendWriteRequest(int operationCode, int offsetBytes, String filename, String writeSequence) {
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file write request for file name: " + filename
                    + " starting from offset bytes of " + offsetBytes + "...\n");

            // Create the FileClientWriteMessage object
            FileClientWriteReqMessage msg = new FileClientWriteReqMessage(requestId, operationCode, offsetBytes, filename,
                    writeSequence);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Initalise buffer
            byte[] buffer = new byte[1024];
            // Send request packet to server through socket
            buffer = sendRequestToServer(clientSocket, requestPacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);

            // Means write response:
            if (op_code == 2) {
                System.out.println("Checking File Write Response...");
                // Unmarshal the response
                FileClientWriteRespMessage response = Unmarshaller.unmarshallWriteResp(buffer);

                // Print response
                System.out.println("Successfully wrote " + response.getContent_len() + " bytes to "
                        + response.getFilename() + "!\n");
                System.out.println("\n\n");
            } else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void sendMonitorRequest(int operationCode, String filename, int monitorInterval) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending monitor file updates request for file name: " + filename + "...\n");

            // Create the FileClientDeleteFileMessage object
            FileClientMonitorUpdatesReqMessage msg = new FileClientMonitorUpdatesReqMessage(requestId, operationCode, filename,
                    monitorInterval);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Initalise buffer
            byte[] buffer = new byte[1024];
            // Send request packet to server through socket
            buffer = sendRequestToServer(clientSocket, requestPacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);
            // Means success monitoring ack response:
            if (op_code == 3) {
                System.out.println("Checking Monitoring File Ack Response...");
                // Unmarshal the response
                FileClientMonitorUpdatesAckRespMessage response = Unmarshaller.unmarshallMonitorUpdatesAckResp(buffer);

                // Print response
                System.out.println("Successfully setup monitoring updates for File:" + response.getFilename()
                        + " at time: " + response.getStartTime() + "!\n");
                System.out.println("Monitoring Interval (in seconds): " + response.getMonitoringInterval());
                System.out.println("\n\n");
                while (op_code != 311) {
                    buffer = new byte[1024];
                    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                    // Disable timeout for socket just for receiving monitoring updates:
                    clientSocket.setSoTimeout(0);
                    clientSocket.receive(responsePacket);
                    op_code = Unmarshaller.unmarshal_op_code(buffer);
                    // Means monitoring update received:
                    if (op_code == 310) {
                        System.out.println("Checking Monitoring File Update Response...");
                        // Unmarshal the response
                        FileClientMonitorUpdatesRespMessage updateResponse = Unmarshaller
                                .unmarshallMonitorUpdatesResp(buffer);
                        System.out.println("Filename:" + updateResponse.getFilename() + " Updated at "
                                + updateResponse.getUpdateTime() + ".");
                        System.out.println("Updated Contents:" + updateResponse.getUpdatedContents());
                        System.out.println("\n\n");
                    }
                    // Means end of monitoring updates - just show the end time here
                    else if (op_code == 311) {
                        FileClientMonitorUpdatesEndRespMessage updateResponse = Unmarshaller
                                .unmarshallMonitorUpdatesEndResp(buffer);
                        System.out.println("End of Monitoring Updates for File:" + updateResponse.getFilename()
                                + " at Time:" + updateResponse.getEndTime());
                        System.out.println("\n\n");
                        break;
                    }
                    // This handles cases when the file has been deleted
                    else {
                        ErrorMessage ErrorResponse = Unmarshaller.unmarshallErrorResp(buffer);
                        System.out.println("Error Code: " + ErrorResponse.getErrorCode());
                        System.out.println("Error Message: " + ErrorResponse.getErrMsg());
                        System.out.println("\n\n");
                        break;
                    }
                }

            } else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void sendDeleteRequest(int operationCode, int offsetBytes, int bytesToDelete, String filename) {
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending delete content request for " + bytesToDelete + " bytes from file name: "
                    + filename + " starting from offset bytes of " + offsetBytes + "...\n");

            // Create the FileClientDeleteMessage object
            FileClientDeleteReqMessage msg = new FileClientDeleteReqMessage(requestId, operationCode, offsetBytes, bytesToDelete,
                    filename);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Initalise buffer
            byte[] buffer = new byte[1024];
            // Send request packet to server through socket
            buffer = sendRequestToServer(clientSocket, requestPacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);
            // Means delete response:
            if (op_code == 4) {
                System.out.println("Checking File Delete contents Response...");
                // Unmarshal the response
                FileClientDeleteRespMessage response = Unmarshaller.unmarshallDeleteResp(buffer);

                // Print response
                System.out.println("Successfully deleted " + response.getDeletedContentLen() + " bytes from "
                        + response.getFilename() + "!\n");
                System.out.println("File Contents Deleted: " + response.getDeletedContent());
                System.out.println("\n\n");
            } else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void sendCreateFileRequest(int operationCode, String filename, String content) {
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file create request for file name: " + filename + "...\n");

            // Create the FileClientCreateFileMessage object
            FileClientCreateFileReqMessage msg = new FileClientCreateFileReqMessage(requestId, operationCode, filename, content);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Initalise buffer
            byte[] buffer = new byte[1024];
            // Send request packet to server through socket
            buffer = sendRequestToServer(clientSocket, requestPacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);
            // Means create file response:
            if (op_code == 5) {
                System.out.println("Checking File Create Response...");
                // Unmarshal the response
                FileClientCreateFileRespMessage response = Unmarshaller.unmarshallCreateFileResp(buffer);

                // Print response
                System.out.println("Successfully created file: " + response.getFilename() + " with "
                        + response.getContentLen() + " bytes written!\n");
                System.out.println("\n\n");
            } else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendDeleteFileRequest(int operationCode, String filename) {
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending delete file request for file name: " + filename + "...\n");

            // Create the FileClientDeleteFileMessage object
            FileClientDeleteFileReqMessage msg = new FileClientDeleteFileReqMessage(requestId, operationCode, filename);

            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Initalise buffer
            byte[] buffer = new byte[1024];
            // Send request packet to server through socket
            buffer = sendRequestToServer(clientSocket, requestPacket);

            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);
            // Means delete file response:
            if (op_code == 6) {
                System.out.println("Checking File Delete Response...");
                // Unmarshal the response
                FileClientDeleteFileRespMessage response = Unmarshaller.unmarshallDeleteFileResp(buffer);

                // Print response
                System.out.println("Successfully deleted file: " + response.getFilename() + " from server!\n");
                System.out.println("\n\n");
            } else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void sendDirRequest(int opCode, int dirNameLen, String dirName) {
        // Create UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending create/list directory request...");
            if (dirNameLen != 0) {
                System.out.println("Directory Name: " + dirName + "; Directory String Length: " + dirNameLen);
            }

            // Create the FileClientDirReqMessage object
            FileClientDirReqMessage msg = new FileClientDirReqMessage(requestId, opCode, dirNameLen, dirName);
            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);
            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Initalise buffer
            byte[] buffer = new byte[1024];
            // Send request packet to server through socket
            buffer = sendRequestToServer(clientSocket, requestPacket);
            
            int op_code = Unmarshaller.unmarshal_op_code(buffer);
            System.out.println("Received opcode: " + op_code);

            // Means Create directory:
            if (op_code == 7) {
                System.out.println("Checking Dir Create Response...");
                // Unmarshal the response
                FileClientDirRespMessage response = Unmarshaller.unmarshallCreateDirRespo(buffer);

                // Print the received filename and content
                System.out.println("Created directory " + response.getDirName());
                System.out.println("\n\n");
            }
            
            // Means List directory:
            else if (op_code == 8) {
                System.out.println("Checking Dir List Response...");
                // Unmarshal the response
                FileClientDirRespMessage response = Unmarshaller.unmarshallCreateDirRespo(buffer);

                // Print the received filename and content
                System.out.println("Listing Directories...\n" + response.getDirName());
                System.out.println("\n\n");
            }

            else {
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
