package UDP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class Client extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField Port_Field;
    private JTextField IP_recv_Field;
    private JTextField IP_send_Field;
    private JTextField Msg_sent_Field;
    private JTextArea Msg_recv_Field; 
    public Server s = new Server();
    private JTextField Port_recv_field;

    public Client() {
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(800, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        IP_recv_Field = new JTextField();
        IP_recv_Field.setBounds(103, 96, 146, 20);
        contentPane.add(IP_recv_Field);
        IP_recv_Field.setColumns(10);

        IP_send_Field = new JTextField();
        IP_send_Field.setEditable(false);
        IP_send_Field.setBounds(103, 34, 146, 20);
        contentPane.add(IP_send_Field);
        IP_send_Field.setColumns(10);

        Port_Field = new JTextField();
        Port_Field.setBounds(103, 65, 86, 20);
        contentPane.add(Port_Field);
        Port_Field.setColumns(10);

        Msg_sent_Field = new JTextField();
        Msg_sent_Field.setColumns(10);
        Msg_sent_Field.setBounds(103, 127, 292, 20);
        contentPane.add(Msg_sent_Field);

        JButton Send_Button = new JButton("Send");
        Send_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                send();
            }
        });
        try {
            // Get the local IP address
            InetAddress localHost = InetAddress.getLocalHost();
            String localIp = localHost.getHostAddress();
            
            // Set the IP address in the text field
            IP_send_Field.setText(localIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            // Handle the error, e.g., set a default value or show a message
            IP_send_Field.setText("Unable to retrieve IP address");
        }
        //IP_send_Field.setText("127.0.0.1");
        //Port_Field.setText("8989");

        Send_Button.setBounds(103, 158, 89, 23);
        contentPane.add(Send_Button);

        // Initialize and configure JTextArea for Msg_recv_Field
        Msg_recv_Field = new JTextArea();
        Msg_recv_Field.setEditable(false);
        Msg_recv_Field.setLineWrap(true);
        Msg_recv_Field.setWrapStyleWord(true);

        // Use JScrollPane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(Msg_recv_Field);
        scrollPane.setBounds(103, 192, 292, 58);
        contentPane.add(scrollPane);

        JLabel lblNewLabel = new JLabel("IP");
        lblNewLabel.setBounds(10, 37, 46, 14);
        contentPane.add(lblNewLabel);

        JLabel lblPort = new JLabel("Port");
        lblPort.setBounds(10, 68, 46, 14);
        contentPane.add(lblPort);

        JLabel lblIpRecv = new JLabel("IP recv");
        lblIpRecv.setBounds(10, 99, 46, 14);
        contentPane.add(lblIpRecv);

        JLabel lblMsg = new JLabel("Message");
        lblMsg.setBounds(10, 130, 83, 14);
        contentPane.add(lblMsg);

        JLabel lblMessageRecv = new JLabel("Message recv");
        lblMessageRecv.setBounds(10, 214, 83, 14);
        contentPane.add(lblMessageRecv);
        
        Port_recv_field = new JTextField();
        Port_recv_field.setColumns(10);
        Port_recv_field.setBounds(328, 96, 67, 20);
        contentPane.add(Port_recv_field);
        
        JLabel lblPortRecv = new JLabel("Port recv");
        lblPortRecv.setBounds(256, 99, 62, 14);
        contentPane.add(lblPortRecv);
        
        JButton Connect_btn = new JButton("Connect");
        Connect_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if (Port_Field.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a port number to receive messages.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int port;
                try {
                    port = Integer.parseInt(Port_Field.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid port number. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Start the Receive method in a separate thread
                Port_Field.setEditable(false);
                Connect_btn.setEnabled(false);
                new Thread(() -> Receive(port)).start();            
        	}
        });
        Connect_btn.setBounds(306, 158, 89, 23);
        contentPane.add(Connect_btn);
    }

    // Method to send messages to the server
    public void send() {
    	if (Port_recv_field.getText().isEmpty() ) {
    		JOptionPane.showMessageDialog(null, "fill Port of the server", "Error", JOptionPane.ERROR_MESSAGE);
    	}
    	else if (Msg_sent_Field.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, "write a message", "Error", JOptionPane.ERROR_MESSAGE);
    	}else {
        try (DatagramSocket socket = new DatagramSocket()) {
            String message = Msg_sent_Field.getText();
            byte[] sendBuffer = message.getBytes();
            InetAddress serverAddress = InetAddress.getByName(IP_recv_Field.getText());
            int serverPort = Integer.parseInt(Port_recv_field.getText());

            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
            socket.send(sendPacket);
            System.out.println("Message sent to server: " + message);
            Msg_sent_Field.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }}
    }

    public void Receive(int port) {
        //int port = Integer.parseInt(Port_Field.getText());
        //Port_Field.setText(Integer.toString(port));
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] receiveBuffer = new byte[1024];
            System.out.println("Client is listening on port " + port + "..");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from server: " + clientMessage);
                String urlString = "https://www.google.com";

                if (clientMessage.equals(urlString)) {
                    try {
                        URI website = new URI("https://www.google.com");
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.browse(website);
                        } else {
                            System.out.println("Desktop is not supported on this platform.");
                        }
                    } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    SwingUtilities.invokeLater(() -> Msg_recv_Field.append(clientMessage + "\n"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int GetPort() {
        int port;
        port = Integer.parseInt(Port_Field.getText());
        return port;
    }
}
