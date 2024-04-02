package main.java.filemgmtinterface.client;

import main.java.filemgmtinterface.client.cache.CacheFileItem;
import main.java.filemgmtinterface.client.cache.CacheList;
import main.java.filemgmtinterface.client.messagetypes.*;
import main.java.filemgmtinterface.client.marshalling.*;

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Client {
    public String SERVER_ADDRESS;
    public int SERVER_PORT;

    // Set a timeout of 1000 ms
    private final int TIMEOUT = 1000;
    private final int MAX_RETRIES = 3;

    public Client(String addr, int port) {
        SERVER_ADDRESS = addr;
        SERVER_PORT = port;
    }

    private byte[] sendRequestToServer(DatagramSocket clientSocket, DatagramPacket requestPacket) {

        int numberOfTries = 0;
        byte[] buffer = new byte[1024];
        while (numberOfTries < 3) {
            try {
                // Set the timeout for socket
                clientSocket.setSoTimeout(TIMEOUT);

                // Send the request to the server
                clientSocket.send(requestPacket);

                // Receive response from the server

                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(responsePacket);

                // int op_code = Unmarshaller.unmarshal_op_code(buffer);
                // System.out.println("Received opcode: " + op_code);
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
        String errorString = "Maxmum number of retries reached. Connection Timeout; Please retry operation";
        ErrorMessage errMsg = new ErrorMessage(errorCode, errorString);
        byte[] marshalledMessage = Marshaller.marshal(errMsg);
        return marshalledMessage;

    }

    public void sendReadRequest(int operationCode, int offsetBytes, int bytesToRead, String filename, CacheList cacheList, int freshness) {
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            // if file last modified time later than in cache, means it is not valid, do the steps below:
            System.out.println("Sending file read request for " + bytesToRead + " bytes from file name: " + filename
                    + " with offset bytes of " + offsetBytes + "...\n");

            // Create the FileClientReadMessage object
            FileClientReadReqMessage msg = new FileClientReadReqMessage(operationCode, offsetBytes, bytesToRead,
                    filename);

            /* First check if file content exist in cache -- use request message to check */
            if (cacheList.getCacheFileItem(msg) != null){
                CacheFileItem cacheFileItem = cacheList.getCacheFileItem(msg);
                // Get current time and check if cache is still valid:
                LocalDateTime now = LocalDateTime.now();
                System.out.println("Last Sync Time for Cached File Contents:" + cacheFileItem.getLastSyncTime());
                System.out.println("Current Time in Client:" + now);

                if (now.isBefore(cacheFileItem.getLastSyncTime().plusSeconds(freshness))){
                    // Means Cache is still valid:
                    System.out.println("Cache content for file:" + filename + " is still valid, reading file contents from cache...");
                    // Read file contents in cache:
                    System.out.println("File Contents: " + cacheFileItem.getContent() + "\n\n");
                    return;
                }
                else{
                    // Means cache is invalid - make req to server to check last modified time:
                    System.out.println("Cache content for file:" + filename + " is no longer valid, sending request to server to sync " +
                            "cache...\n");

                    // Create the FileClientGetAttrReqMessage object
                    FileClientGetAttrReqMessage attrMsg = new FileClientGetAttrReqMessage(10,
                            filename, "Last Modified Time");

                    // Marshal the message
                    byte[] marshalledMessage = Marshaller.marshal(attrMsg);

                    // Create a DatagramPacket with the marshalled message
                    DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                            InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

                    // Send request packet to server through socket
                    byte[] buffer = sendRequestToServer(clientSocket, requestPacket);

                    int op_code = Unmarshaller.unmarshal_op_code(buffer);
                    System.out.println("Received opcode: " + op_code);

                    // Means read response:
                    if (op_code == 10) {
                        System.out.println("Checking File Get Last Modified Time Response...");
                        // Unmarshal the response
                        FileClientGetAttrRespMessage response = Unmarshaller.unmarshallGetAttrFileResp(buffer);

                        // Print the received attribute and its value
                        System.out.println("Attribute:" + response.getFileAttribute() + " received for " +
                                response.getFilename());
                        System.out.println("Value: " + response.getFileAttributeValue());
                        System.out.println("\n");

                        if (response.getFileAttribute().equals("Last Modified Time")) {
                            System.out.println("Comparing File Last Modified Time with Server...");

                            // Format the last modified time received and compare if they are the same
                            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss yyyy");
                            LocalDateTime fileModifiedTime = LocalDateTime.from(myFormatObj.parse(response.getFileAttributeValue()));
                            System.out.println("Last Modified Time in cache:" + cacheFileItem.getLastModifiedTime());
                            System.out.println("Last Modified Time in server:" + fileModifiedTime);

                            // If same means file was not updated, we can continue using the cache content, and we update the sync time:
                            if (fileModifiedTime.isEqual(cacheFileItem.getLastModifiedTime())) {
                                System.out.println("Last Modified Time for file in client cache and server are the same!" + "\n" +
                                        "Cache content for file:" + filename + " is still valid, reading file contents from cache...");
                                System.out.println("File Contents: " + cacheFileItem.getContent() + "\n\n");

                                // Update Last sync time to current time:
                                cacheFileItem.setLastSyncTime(LocalDateTime.now());
                                return;
                            } else {
                                // Not the same means we need to send the request to the server:
                                System.out.println("Last Modified Time for file in client cache and server are NOT the same!" + "\n" +
                                        "Cache content for file:" + filename + " is no longer valid, sending request to " +
                                        "server to get updated file contents...\n");
                            }
                        }
                    }
                    else {
                        ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                        System.out.println("Error Code: " + response.getErrorCode());
                        System.out.println("Error Message: " + response.getErrMsg());
                        System.out.println("Cache Error, proceed as per normal to send File Read Request to Server...");
                        System.out.println("\n\n");
                    }

                }
            }
            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);

            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            // Send request packet to server through socket
            byte[] buffer =  sendRequestToServer(clientSocket, requestPacket);

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

                // Add the file to the cache:
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss yyyy");
                LocalDateTime fileModifiedTime = LocalDateTime.from(myFormatObj.parse(response.getModifiedTime()));
                CacheFileItem newCacheFileItem = new CacheFileItem(response.getFilename(), LocalDateTime.now(),
                        fileModifiedTime, response.getContent());

                // This check is to account for the case where the cache item has expired AND modified time is different
                if(cacheList.getCacheFileItem(msg) != null){
                    cacheList.setCacheFileItem(msg, newCacheFileItem);
                }
                else{
                    cacheList.addCacheFileItem(msg, newCacheFileItem);
                }

            }
            else {
                ErrorMessage response = Unmarshaller.unmarshallErrorResp(buffer);
                System.out.println("Error Code: " + response.getErrorCode());
                System.out.println("Error Message: " + response.getErrMsg());
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    // Send create directory request

    public void sendDirRequest(int opCode, int dirNameLen, String dirName) {
        // Create UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending create directory request...");
            if (dirNameLen != 0) {
                System.out.println("Directory Name: " + dirName + " ;Directory String Length: " + dirNameLen);
            }

            // Create the FileClientReadMessage object
            FileClientDirReqMessage msg = new FileClientDirReqMessage(opCode, dirNameLen, dirName);
            // Marshal the message
            byte[] marshalledMessage = Marshaller.marshal(msg);
            // Create a DatagramPacket with the marshalled message
            DatagramPacket requestPacket = new DatagramPacket(marshalledMessage, marshalledMessage.length,
                    InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

            byte[] buffer = new byte[1024];

            buffer = sendRequestToServer(clientSocket, requestPacket);
            // // Send the request to the server
            // clientSocket.send(requestPacket);

            // // Receive response from the server
            // byte[] buffer = new byte[1024];
            // DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            // clientSocket.receive(responsePacket);

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

    public void sendWriteRequest(int operationCode, int offsetBytes, String filename, String writeSequence) {
        // Create a UDP socket
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            System.out.println("Sending file write request for file name: " + filename
                    + " starting from offset bytes of " + offsetBytes + "...\n");

            // Create the FileClientWriteMessage object
            FileClientWriteReqMessage msg = new FileClientWriteReqMessage(operationCode, offsetBytes, filename,
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
            FileClientMonitorUpdatesReqMessage msg = new FileClientMonitorUpdatesReqMessage(operationCode, filename,
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
            FileClientDeleteReqMessage msg = new FileClientDeleteReqMessage(operationCode, offsetBytes, bytesToDelete,
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
            FileClientCreateFileReqMessage msg = new FileClientCreateFileReqMessage(operationCode, filename, content);

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
            FileClientDeleteFileReqMessage msg = new FileClientDeleteFileReqMessage(operationCode, filename);

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
}
