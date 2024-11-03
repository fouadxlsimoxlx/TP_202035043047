package UDP;

public class Main {
    public static void main(String[] args) {
        // Create and start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.setVisible(true);  // Display the server GUI
            server.Receive();  // Start the server to receive messages
        });

        // Create and start the client in a separate thread
        Thread clientThread = new Thread(() -> {
            Client client = new Client();
            client.setVisible(true);  // Display the client GUI
            client.Receive();
        });

        // Start both threads
        serverThread.start();
        clientThread.start();
    }
}
