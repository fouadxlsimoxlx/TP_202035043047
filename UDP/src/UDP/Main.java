package UDP;

public class Main {
    public static void main(String[] args) {
        // Create and start the client in a separate thread
        Thread S1T = new Thread(() -> {
            Server S1 = new Server();
            S1.setTitle("S1");
            S1.setBounds(100, 100, 450, 300);
            S1.setVisible(true);  // Display the client GUI
        });
        Thread S2T = new Thread(() -> {
            Server S2 = new Server();
            S2.setTitle("S2");
            S2.setBounds(100, 400, 450, 300);
            S2.setVisible(true);  // Display the client GUI
        });
        Thread S3T = new Thread(() -> {
            Server S3 = new Server();
            S3.setTitle("S3");
            S3.setBounds(700, 400, 450, 300);
            S3.setVisible(true);  // Display the client GUI
        });

        S1T.start();       
        //S2T.start();
        //S3T.start();
    }
}
