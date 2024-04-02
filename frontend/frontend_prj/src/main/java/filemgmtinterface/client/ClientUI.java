package main.java.filemgmtinterface.client;
import java.util.Scanner;

public class ClientUI {
    public static void showUI() {
        Client client = new Client("localhost", 5000);
        Scanner myObj = new Scanner(System.in);
        int choice = 0;
        do {
            String UIMessage = "Welcome to the Remote File Management system!\n"
                    + "What would you like to do? (Input your choice number):\n"
                    + "1. Read a file\n"
                    + "2. Write (insert) into a file\n"
                    + "3. Monitor updates to a file\n"
                    + "4. Delete contents from a file\n"
                    + "5. Create a new file\n"
                    + "6. Delete a file\n"
                    + "7. Create a new directory\n"
                    + "8. List directory contents\n"
                    + "0. Exit\n";
            System.out.println(UIMessage);

            try {
                choice = myObj.nextInt();
                myObj.nextLine();

                // Read operation:
                if (choice == 1) {
                    try {
                        System.out.println("Input the filename that you want to read:");
                        String filename = myObj.nextLine();
                        System.out.println("Input the offset bytes to start reading from:");
                        int offset_bytes = myObj.nextInt();
                        System.out.println("Input the number of bytes to read:");
                        int num_bytes = myObj.nextInt();
                        client.sendReadRequest(1, offset_bytes, num_bytes, filename);
                    } catch (Exception e) {
                        System.out.println("Invalid values given, please try again");
                    }
                }

                // Write operation:
                else if (choice == 2) {
                    try {
                        System.out.println("Input the filename that you want to write to:");
                        String filename = myObj.nextLine();
                        System.out.println("Input the offset bytes to start writing from:");
                        int offset_bytes = myObj.nextInt();
                        myObj.nextLine();
                        System.out.println("Input the sequence to insert:");
                        String write_sequence = myObj.nextLine();
                        client.sendWriteRequest(2, offset_bytes, filename, write_sequence);
                    } catch (Exception e) {
                        System.out.println("Invalid values given, please try again");
                    }
                }

                // Monitor operation:
                else if (choice == 3) {
                    try {
                        System.out.println("Input the filename that you want to monitor:");
                        String filename = myObj.nextLine();
                        System.out.println("Input the monitor interval for the file (in seconds):");
                        int monitorInterval = myObj.nextInt();
                        client.sendMonitorRequest(3, filename, monitorInterval);
                    } catch (Exception e) {
                        System.out.println("Invalid values given, please try again");
                    }
                }

                // Delete operation:
                else if (choice == 4) {
                    try {
                        System.out.println("Input the filename that you want to delete content from:");
                        String filename = myObj.nextLine();
                        System.out.println("Input the offset bytes to start deleting from:");
                        int offset_bytes = myObj.nextInt();
                        System.out.println("Input the number of bytes to delete:");
                        int num_bytes = myObj.nextInt();
                        client.sendDeleteRequest(4, offset_bytes, num_bytes, filename);
                    } catch (Exception e) {
                        System.out.println("Invalid values given, please try again");
                    }
                }

                // Create file operation:
                else if (choice == 5) {
                    try {
                        System.out.println("Input the filename that you want to create:");
                        String filename = myObj.nextLine();
                        System.out.println("Input the sequence to write into the file:");
                        String write_sequence = myObj.nextLine();
                        client.sendCreateFileRequest(5, filename, write_sequence);
                    } catch (Exception e) {
                        System.out.println("Invalid values given, please try again");
                    }
                }

                // Delete file operation:
                else if (choice == 6) {
                    try {
                        System.out.println("Input the filename that you want to delete:");
                        String filename = myObj.nextLine();
                        client.sendDeleteFileRequest(6, filename);
                    } catch (Exception e) {
                        System.out.println("Invalid values given, please try again");
                    }
                }

                // Create directory operation:
                else if (choice == 7) {
                    try {
                        System.out.println("Input name of directory");
                        String dirName = myObj.nextLine();
                        int dirNameLen = dirName.length();
                        client.sendDirRequest(7, dirNameLen, dirName);
                    } catch (Exception e) {
                        System.out.println("Invalid values given please try again");
                    }
                }
                
                // List directory contents operation:
                else if (choice == 8) {
                    try {
                        System.out.println("Specify directory (Leave blank to list all)");
                        String dirName = myObj.nextLine();
                        int dirNameLen = dirName.length();
                        client.sendDirRequest(8, dirNameLen, dirName);
                    } catch (Exception e) {
                        System.out.println("Error encounted");
                    }
                }
                
                // Default
                else if (choice == 0) {
                    System.out.println("Thank you for using our file management system!");
                }
            } catch (Exception e) {
                // System.out.println(e);
                System.out.println("Please input a valid choice");
                choice = -1;
                myObj.nextLine();
            }
        } while (choice != 0);
    }
}
