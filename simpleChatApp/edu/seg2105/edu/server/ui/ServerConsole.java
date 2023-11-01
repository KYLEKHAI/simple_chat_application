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

    private void handleServerCommand(String command) {
        // 2c) implementation

    }

    // Implemented ChatIF method
    @Override
    public void display(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        // Initialize and set Echoserver instance with default port
        EchoServer echoServer = new EchoServer(EchoServer.DEFAULT_PORT);
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