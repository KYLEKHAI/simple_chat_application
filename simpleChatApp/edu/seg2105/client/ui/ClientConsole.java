package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client. It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF {
  // Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  // Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  /**
   * Scanner to read from the console
   */
  Scanner fromConsole;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) {
    try {
      client = new ChatClient(host, port, this);

    } catch (IOException exception) {
      System.out.println("Error: Can't setup connection!"
          + " Terminating client.");
      System.exit(1);
    }

    // Create scanner object to read from console
    fromConsole = new Scanner(System.in);
  }

  // Instance methods ************************************************

  /**
   * This method waits for input from the console. Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept() {
    try {

      String message;

      // Handle # commands and regular messages from client

      while (true) {
        message = fromConsole.nextLine();

        if (message.startsWith("#")) {
          handleCommand(message);

        } else {
          client.handleMessageFromClientUI(message);
        }
      }

    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  // This method handles commands from the client with msg starting with "#"
  private void handleCommand(String command) {

    if (command.startsWith("#quit")) {
      // Handle quit command
      System.out.println(client + " requested to quit");

      try {
        client.closeConnection();
        System.exit(0);
        System.out.println("Exit the system");
      } catch (IOException e) {
        e.printStackTrace();

      }

    } else if (command.startsWith("#logoff")) {

      // Handle logoff command
      System.out.println(client + " requested to log off");

      try {
        client.closeConnection();
        System.out.println(client + " is now logged off");
      } catch (IOException e) {
        e.printStackTrace();

      }

    } else if (command.startsWith("#sethost")) {

      // Handle sethost command
      System.out.println(client + " requested to set host");

      if (!client.isConnected()) {

        // Take client message to set the host in console
        String[] cLine = command.split(" ");
        if (cLine.length >= 2) {
          String newHost = cLine[1];
          client.setHost(newHost);

          System.out.println("Setting new host to: " + newHost);
        }

        else if (cLine.length < 2) {
          System.out.println("Invalid syntax. Must input #sethost <host>");
        }

      } else {
        System.out.println("Error. Please log off before setting a new host");
      }

    } else if (command.startsWith("#setport")) {

      // Handle setport command
      System.out.println(client + " requested to set host");

      if (!client.isConnected()) {

        // Take client message to set the host in console
        String[] cLine = command.split(" ");
        if (cLine.length >= 2) {
          int newPort = Integer.parseInt(cLine[1]);
          client.setPort(newPort);

          System.out.println("Setting new port to: " + newPort);
        }

        else if (cLine.length < 2) {
          System.out.println("Invalid syntax. Must input #setport <port>");
        }

      } else {
        System.out.println("Error. Please log off before setting a new port");
      }

      // ERROR TO BE FIXED: LOGIN DOESNT ALLOW FOR RE-LOGGIN IN WITH NEW HOST/PORT
      // (also use getInet() to validate)
    } else if (command.startsWith("#login")) {
      // Handle login command
      System.out.println(client + " requested to log in");

      if (!client.isConnected()) {

        try {
          client.openConnection();
          System.out.println(client + " is now logged in");
        } catch (

        IOException e) {
          e.printStackTrace();
        }

      } else {
        System.out.println(client + " is already logged in");
      }

    } else if (command.startsWith("#gethost")) {

      // Handle gethost command
      System.out.println(client + " requested to get host");
      System.out.println("Current host: " + client.getHost());

    } else if (command.startsWith("#getport")) {

      // Handle getport command
      System.out.println(client + " requested to get port");
      System.out.println("Current port: " + client.getPort());
    }

    else {
      System.out.println("Invalid command: " + command);
    }
  }

  /**
   * This method overrides the method in the ChatIF interface. It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
    System.out.println("> " + message);
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) {
    String host = "";
    int port = 0;

    try {
      host = args[0];

    } catch (ArrayIndexOutOfBoundsException e) {
      host = "localhost";
      port = DEFAULT_PORT;
    }

    ClientConsole chat = new ClientConsole(host, DEFAULT_PORT);
    chat.accept(); // Wait for console data
  }
}
// End of ConsoleChat class