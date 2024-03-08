package main.java;
import main.java.filemgmtinterface.client.Client;
public class Main {
    // This is the main file to run:
    public static void main(String[] args) {
        Client.showUI();
        Client.sendRequest(1);
    }
}