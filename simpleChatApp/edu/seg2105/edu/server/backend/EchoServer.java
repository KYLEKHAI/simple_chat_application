package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import ocsf.server.*;
import java.io.IOException;

// Importing oscf classes
import ocsf.server.ConnectionToClient;
import ocsf.server.AbstractServer;

// Importing new ServerConsole class
import edu.seg2105.edu.server.ui.ServerConsole;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) {
    super(port);
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {

    this.sendToAllClients(msg);

    // Make message into string
    String message = msg.toString(); // Convert the message to a string

    // If message starts with #login then call the loginClientMessage method
    if (message.startsWith("#login")) {

      loginClientMessage(message, client);

    } else {
      // Other processing for regular messages
      System.out.println("Message received: " + message + " from " + client.getInfo("clientLoginId"));

      // Sending to all the clients on the same server
      this.sendToAllClients(client.getInfo("clientLoginId") + ": " + message);
    }
  }

  private void loginClientMessage(String command, ConnectionToClient client) {

    // Splits (#login and clientLoginId) to different strings to set client info
    String[] parts = command.split(" ");

    if (parts.length >= 2) {

      // Assigns 2nd split of the string to be the clientLoginId
      String clientLoginId = parts[1];

      // Set the client's login ID in their connection information
      client.setInfo("clientLoginId", clientLoginId);
      client.setInfo("clientLoginId", clientLoginId);
      System.out.println("Message received: " + command + " from null");
      System.out.println(clientLoginId + " has logged on.");

    }

  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
    System.out.println("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    System.out.println("Server has stopped listening for connections.");
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on. Defaults to 5555
   *                if no argument is entered.
   */
  public static void main(String[] args) {
    int port = 0; // Port to listen on

    try {
      if (args.length > 0) {
        port = Integer.parseInt(args[0]); // Get port from command line

      } else {
        port = DEFAULT_PORT; // Set port to 5555
      }

    } catch (Throwable t) {
      port = DEFAULT_PORT; // Set port to 5555
    }

    EchoServer sv = new EchoServer(port);

    try {
      sv.listen(); // Start listening for connections
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  /**
   * Implements hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * 
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client has connected to the server.");

  }

  /**
   * Implements hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(
      ConnectionToClient client) {

    System.out.println("Client " + client.getInfo("clientLoginId") + " is disconnected.");
  }

  public void setServerConsole(ServerConsole serverConsole) {
  }

  // Disconnects all existing clients
  public void disconnectAllClients() {

    // Iterate through all the clients connected in the thread list
    Thread[] clientThreadList = getClientConnections();

    // For each connected client, disconnect each of them
    for (Thread clientThread : clientThreadList) {
      if (clientThread instanceof ConnectionToClient) {

        ConnectionToClient client = (ConnectionToClient) clientThread;

        try {
          client.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    }
  }
}
// End of EchoServer class
