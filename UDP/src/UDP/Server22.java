package UDP;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.sound.sampled.Port;
import javax.swing.DropMode;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server22 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Port_Field;
	private JTextField IP_recv_Field;
	private JTextField IP_send_Field;
	private JTextField Msg_sent_Field;
	private JTextField Msg_recv_Field;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
	}

	/**
	 * Create the frame.
	 */
	
	public void start() {
		System.out.println("server starting ..");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server22 frame = new Server22();
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
		Receive();
		
	}
	public Server22() {
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
				
				Msg_recv_Field.setText("ooooooooooooooooooo");
				
				/*
				////////////// first field
				int[] IP_dec_send = new int[4];
		        String[] parts = IP_send_Field.getText().split("\\.");    
		        for (int i=0;i<4;i++){
		        IP_dec_send[i]=Integer.parseInt(parts[i]);
		        }
		        //displaying ip
		        for (int i=0;i<4;i++){
		            System.out.print(IP_dec_send[i]+"  ");
		        }
		        
		        /////////////////second field
		        //port
		        int Port = Integer.parseInt(Port_Field.getText());
		        System.out.println("\nthis is the port "+Port);
		        
		        ///////////////third field
		        int[] IP_dec_recv = new int[4];
		        String[] parts2 = IP_recv_Field.getText().split("\\.");
		        for (int i=0;i<4;i++){
		        IP_dec_recv[i]=Integer.parseInt(parts2[i]);
		        }
		        
		        /////////////forth field
		        String message_sent = Msg_sent_Field.getText();
		        */
		        
		        
		        
			}
		});
		Send_Button.setBounds(103, 158, 89, 23);
		contentPane.add(Send_Button);
		
		Msg_recv_Field = new JTextField();
		Msg_recv_Field.setEditable(false);
		Msg_recv_Field.setColumns(10);
		Msg_recv_Field.setBounds(103, 192, 292, 58);
		contentPane.add(Msg_recv_Field);
		
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
	}
	
	public void Receive() {
		int port = 9888;  // Port on which the server listens
		//int port = Integer.parseInt(Port_Field.getText());
		
		 try (DatagramSocket socket = new DatagramSocket(port)) {
	            byte[] receiveBuffer = new byte[1024];
	            System.out.println("Server is listening on port "+port+"..");

	            while (true) {
	                // Receive message from client
	                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
	                socket.receive(receivePacket);
	                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
	                System.out.println("Received from client: " + clientMessage);
	                System.out.println(Msg_sent_Field.getText());
	                 
	                // Send response back to client
	                /*String response = "Message received: " + clientMessage;
	                byte[] sendBuffer = response.getBytes();
	                InetAddress clientAddress = receivePacket.getAddress();
	                int clientPort = receivePacket.getPort();
	                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
	                socket.send(sendPacket);*/
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}
	
	public int getport() {
		return Integer.parseInt(Port_Field.getText());
	}
	
}
