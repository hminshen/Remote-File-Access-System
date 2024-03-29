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
                    + "2. Write to a file\n"
                    + "3. Append to a file\n"
                    + "4. Delete a file\n" // Add Create directory
                    + "5. Create Directory\n"
                    + "0. Exit\n";
            System.out.println(UIMessage);
            try {
                choice = myObj.nextInt();
                // Read operation:
                if (choice == 1) {
                    try {
                        System.out.println("Input the filename that you want to read:");
                        myObj.nextLine();
                        String filename = myObj.nextLine();
                        System.out.println("Input the offset bytes to start reading from:");
                        int offset_bytes = myObj.nextInt();
                        System.out.println("Input the number of bytes to read:");
                        int num_bytes = myObj.nextInt();
                        client.sendReadRequest(1, offset_bytes, num_bytes, filename);
                    } catch (Exception e) {
                        System.out.println("Invalid values given, please try again");
                    }

                } else if (choice == 0) {
                    System.out.println("Thank you for using our file management system!");
                } else if (choice == 5) {
                    try {
                        System.out.println("Input name of directory");
                        myObj.nextLine();
                        String dirName = myObj.nextLine();
                        int dirNameLen = dirName.length();
                        client.sendCreateDirRequest(3, dirNameLen, dirName);

                    } catch (Exception e) {
                        System.out.println("Invalid values given please try again");
                    }
                }

                else {
                    System.out.println("Please choose a valid number");
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
