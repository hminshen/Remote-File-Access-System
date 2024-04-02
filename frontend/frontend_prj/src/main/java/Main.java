package main.java;

import main.java.filemgmtinterface.client.ClientUI;

public class Main {
    // This is the main file to run:
    public static void main(String[] args) {
        // Check if there are command line arguments
        int freshness = 30; // Set default value:
        int maxretries = 3;
        int timeout = 1000;
        if (args.length > 0) {
            try {
                // Parse the string argument into an integer
                freshness = Integer.parseInt(args[0]);
                maxretries = Integer.parseInt(args[1]);
                timeout = Integer.parseInt(args[2]);
                // Print the parsed integer value
                System.out.println("Initalised with Freshness Value: " + freshness + ", Max Number of Retries: " + maxretries + ", " +
                        "Socket Request Timeout: " + timeout);
            } catch (NumberFormatException e) {
                // Handle the case where the argument is not a valid integer
                System.out.println("Invalid integer values given");
            }
            ClientUI.showUI(freshness, maxretries, timeout);
        }
        else{
            System.out.println("No Cmd arguments given, initialised with Freshness Value: " + freshness);
            ClientUI.showUI(freshness, maxretries, timeout);
        }
    }
}
