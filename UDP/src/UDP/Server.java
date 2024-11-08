package UDP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.awt.Desktop;
import java.net.URI;
import java.net.UnknownHostException;

public class Server extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField Port_Field;
    private JTextField IP_recv_Field;
    private JTextField IP_send_Field;
    private JTextField Msg_sent_Field;
    private JTextArea Msg_recv_Field;  
    private JTextField Port_recv_field;

    public Server() {
        setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
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
        //Port_Field.setText("9999");

        Send_Button.setBounds(103, 158, 89, 23);
        contentPane.add(Send_Button);

        Msg_recv_Field = new JTextArea();
        Msg_recv_Field.setEditable(false);
        Msg_recv_Field.setLineWrap(true);
        Msg_recv_Field.setWrapStyleWord(true);

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

        
        JLabel lblPort_1 = new JLabel("Port recv");
        lblPort_1.setBounds(259, 99, 62, 14);
        contentPane.add(lblPort_1);
        
        Port_recv_field = new JTextField();
        Port_recv_field.setColumns(10);
        Port_recv_field.setBounds(331, 96, 64, 20);
        contentPane.add(Port_recv_field);
        
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

                // Check if the port is available
                try (DatagramSocket socket = new DatagramSocket(port)) {
                    // If the port is free, close the socket and proceed
                    socket.close();
                } catch (BindException e) {
                    JOptionPane.showMessageDialog(null, "Port " + port + " is already in use. Please choose another port.", "Port Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occurred while checking the port availability.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Port_Field.setEditable(false);
                Connect_btn.setEnabled(false);
                new Thread(() -> Receive(port)).start();     
        	}
        });
        Msg_sent_Field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    send();
                }
            }
        });
        
        Connect_btn.setBounds(306, 158, 89, 23);
        contentPane.add(Connect_btn);
    }
    

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
    	Port_Field.setText(Integer.toString(port));
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] receiveBuffer = new byte[1024];
            System.out.println("Server is listening on port " + port + "..");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client: " + clientMessage);
                
                
                if (clientMessage.equals("google") || clientMessage.equals("www.google.com")) {
                	URI website = new URI("www.google.com");
                	Desktop desktop = Desktop.getDesktop();
                    desktop.browse(website);
                } else if (clientMessage.equals("university") || clientMessage.equals("mohamed khider")){
                	URI website = new URI("https://univ-biskra.dz/index.php/ar/");
                	Desktop desktop = Desktop.getDesktop();
                    desktop.browse(website);
                }
                else if (clientMessage.equals("translator") || clientMessage.equals("google translator")){
                	URI website = new URI("https://translate.google.com/");
                	Desktop desktop = Desktop.getDesktop();
                    desktop.browse(website);
                }
                else {
                    SwingUtilities.invokeLater(() -> Msg_recv_Field.append(clientMessage + "\n"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
