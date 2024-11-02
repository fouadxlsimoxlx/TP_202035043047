package UDP;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Creating a thread for the Server
	    Thread serverThread = new Thread(() -> {
	        Server s = new Server();
	        System.out.println("server:");
	        s.start();
	    });

	    // Creating a thread for the Client
	    Thread clientThread = new Thread(() -> {
	        Client c = new Client();
	        System.out.println("client:");
	        c.start();
	    });

	    // Starting both threads
	    serverThread.start();
	    clientThread.start();
	}

}
