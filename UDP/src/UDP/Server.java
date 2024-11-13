package UDP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.awt.Desktop;
import java.net.URI;
import java.net.UnknownHostException;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        Port_Field.setText(Integer.toString(port));
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] receiveBuffer = new byte[65535];
            System.out.println("Server is listening on port " + port + "..");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Get the sender's IP and port
                String senderIP = receivePacket.getAddress().getHostAddress();
                int senderPort = receivePacket.getPort();

                if (clientMessage.equals("google") || clientMessage.equals("www.google.com")) {
                    Desktop.getDesktop().browse(new URI("https://www.google.com"));
                } else if (clientMessage.equals("university") || clientMessage.equals("mohamed khider")) {
                    Desktop.getDesktop().browse(new URI("https://univ-biskra.dz/index.php/ar/"));
                } else if (clientMessage.equals("translator") || clientMessage.equals("google translator")) {
                    Desktop.getDesktop().browse(new URI("https://translate.google.com/"));
                } else if (clientMessage.equals("screenshoot") || clientMessage.equals("sc")) {
                    // C2 receives screenshot request and sends screenshot back to C1
                    System.out.println("Screenshot request received from " + senderIP);
                    takeScreenshotAndSend(senderIP, Integer.parseInt(Port_recv_field.getText()));
                    //takeScreenshotAndSend(senderIP, senderPort);
                } else if (clientMessage.equals("IMG_START")) {
                    // C1 is receiving the image data
                    ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
                    System.out.println("Receiving image data...");

                    socket.setSoTimeout(3000); // Timeout after 3 seconds of inactivity
                    while (true) {
                        try {
                            socket.receive(receivePacket);
                            String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

                            // Check for end signal
                            if (receivedData.equals("IMG_END")) {
                                System.out.println("Image transfer complete.");
                                socket.setSoTimeout(0); 
                                break;
                            }

                            // Check for image data
                            if (receivedData.startsWith("IMG:")) {
                                imageStream.write(receivePacket.getData(), 4, receivePacket.getLength() - 4);
                            }
                        } catch (SocketTimeoutException e) {
                            System.out.println("Image reception timeout reached, but saving the received image.");
                            break;
                        }
                    }

                    byte[] imageData = imageStream.toByteArray();
                    saveReceivedImage(imageData,senderIP);
                } else {
                    System.out.println("Received from client: " + clientMessage);
                    SwingUtilities.invokeLater(() -> Msg_recv_Field.append(clientMessage + "\n"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static void saveReceivedImage(byte[] imageData, String senderIP) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            BufferedImage receivedImage = ImageIO.read(bais);

            if (receivedImage == null) {
                System.out.println("Failed to decode image. The received data may be corrupted.");
                return;
            }

            // Create the directory to save the image
            String directoryPath = "C:\\Users\\dmc\\Desktop\\udp_received_images";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate a filename with the sender IP and timestamp
            String sanitizedIP = senderIP.replace(".", "_"); // Replace dots with underscores for a valid filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = directoryPath + File.separator + "received_image_" + sanitizedIP + "_" + timestamp + ".png";
            File outputfile = new File(filename);

            // Save the image
            ImageIO.write(receivedImage, "png", outputfile);
            System.out.println("Image saved at: " + outputfile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
    public static void takeScreenshotAndSend(String ipAddress, int port) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenshot, "png", baos);
            byte[] imageData = baos.toByteArray();

            DatagramSocket socket = new DatagramSocket();
            int packetSize = 64000;
            byte[] header = "IMG:".getBytes();

            // Send start signal
            byte[] startSignal = "IMG_START".getBytes();
            DatagramPacket startPacket = new DatagramPacket(startSignal, startSignal.length, InetAddress.getByName(ipAddress), port);
            socket.send(startPacket);

            // Send image data in chunks
            for (int i = 0; i < imageData.length; i += packetSize) {
                int length = Math.min(packetSize, imageData.length - i);
                byte[] dataChunk = new byte[header.length + length];

                // Add header
                System.arraycopy(header, 0, dataChunk, 0, header.length);
                // Copy image data
                System.arraycopy(imageData, i, dataChunk, header.length, length);

                DatagramPacket packet = new DatagramPacket(dataChunk, dataChunk.length, InetAddress.getByName(ipAddress), port);
                socket.send(packet);
            }

            // Send end-of-image signal
            byte[] endSignal = "IMG_END".getBytes();
            DatagramPacket endPacket = new DatagramPacket(endSignal, endSignal.length, InetAddress.getByName(ipAddress), port);
            socket.send(endPacket);

            System.out.println("Screenshot sent successfully to " + ipAddress + ":" + port);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
