package main.java;

import main.java.filemgmtinterface.client.ClientUI;

public class Main {
    // This is the main file to run:
    public static void main(String[] args) {
        // Check if there are command line arguments
        int freshness = 30; // Set default value:
        if (args.length > 0) {
            try {
                // Parse the string argument into an integer
                freshness = Integer.parseInt(args[0]);
                // Print the parsed integer value
                System.out.println("Initalised with Freshness Value: " + freshness);
            } catch (NumberFormatException e) {
                // Handle the case where the argument is not a valid integer
                System.out.println("Invalid integer value: " + args[0]);
            }
            ClientUI.showUI(freshness);
        }
        else{
            System.out.println("No Cmd arguments given, initialised with Freshness Value: " + freshness);
            ClientUI.showUI(freshness);
        }
    }
}
