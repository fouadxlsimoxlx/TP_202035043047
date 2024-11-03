package UDP;

public class Main {
    public static void main(String[] args) {
        // Create and start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.setVisible(true);  // Display the server GUI
            server.Receive(9797);  // Start the server to receive messages
        });

        // Create and start the client in a separate thread
        Thread clientThread = new Thread(() -> {
            Client client = new Client();
            client.setVisible(true);  // Display the client GUI
            client.Receive(9999);
        });

        // Start both threads
        serverThread.start();
        clientThread.start();
    }
}
