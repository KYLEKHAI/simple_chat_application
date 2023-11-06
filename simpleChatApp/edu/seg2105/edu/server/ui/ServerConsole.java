package edu.seg2105.edu.server.ui;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.ChatIF;

import java.io.IOException;
import java.util.Scanner;

public class ServerConsole implements ChatIF {

    // Declare class variables
    private EchoServer server;
    private Scanner scanner;

    // Constructor
    public ServerConsole(EchoServer server) {
        this.server = server;
        this.scanner = new Scanner(System.in);
    }

    public void accept() {

        String message;

        while (true) {
            message = scanner.nextLine();

            if (message != null && !message.isEmpty()) {

                // For server commands specified with "#"
                if (message.startsWith("#")) {
                    handleServerCommand(message);

                } else {
                    // Send to console for all clients
                    server.sendToAllClients("SERVER MSG> " + message);

                    // Send to server console
                    display("SERVER MSG> " + message);
                }
            }
        }
    }

    // Handle server commands
    private void handleServerCommand(String command) {

        // Handle quit command
        if (command.startsWith("#quit")) {
            display("Server will quit");
            server.sendToAllClients("SERVER MSG> Server is quitting");
            server.stopListening();
            System.exit(0);

            // Handle stop command
        } else if (command.startsWith("#stop")) {

            server.stopListening();

            // Handle close command
        } else if (command.startsWith("#close")) {
            server.stopListening();
            server.disconnectAllClients();

            // Handle setport command
        } else if (command.startsWith("#setport")) {

            if (!server.isListening()) {
                String[] cLine = command.split(" ");

                if (cLine.length == 2) {

                    int newPort = Integer.parseInt(cLine[1]);
                    server.setPort(newPort);
                    display("Server is setting new port to: " + newPort);

                } else {
                    display("Invalid syntax. Please use #setport <port>");
                }
            } else {
                display("Server is required to stop before setting new port");
            }

            // Handle start command
        } else if (command.startsWith("#start")) {

            if (!server.isListening()) {

                try {
                    server.listen();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                display("Server must be stopped before listening for new clients");
            }

            // Handle getport command
        } else if (command.startsWith("#getport")) {
            display("Current server port: " + server.getPort());

        } else {
            display("Invalid command: " + command);
        }

    }

    // Implemented ChatIF method
    @Override
    public void display(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        // Initialize and set Echoserver instance with default port
        int port = EchoServer.DEFAULT_PORT;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]); // Read port from command line argument
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number");
            }
        }
        EchoServer echoServer = new EchoServer(port);
        ServerConsole serverConsole = new ServerConsole(echoServer);
        echoServer.setServerConsole(serverConsole);

        try {
            // Wait for client connections
            echoServer.listen();

            // For server user input
            serverConsole.accept(); // Accept server-side user input

        } catch (IOException e) {
            System.err.println("ERROR - Could not listen for clients!");
        }
    }

}