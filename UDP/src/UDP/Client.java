package UDP;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Port_Field;
	private JTextField IP_recv_Field;
	private JTextField IP_send_Field;
	private JTextField Msg_sent_Field;
	private JTextField Msg_recv_Field;
	
	Server s = new Server();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
	}
	
	public void start() {
			
			System.out.println("client starting ..");
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Client frame = new Client();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});	
		}

	/**
	 * Create the frame.
	 */
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
				try (DatagramSocket socket = new DatagramSocket()) {
		            String message = Msg_sent_Field.getText();
		            byte[] sendBuffer = message.getBytes();
		            InetAddress serverAddress = InetAddress.getByName(IP_recv_Field.getText());
		            int serverPort = s.getport();

		            // Send message to server
		            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
		            socket.send(sendPacket);
		            System.out.println("Message sent to server: " + message);

		            // Receive response from server
		            /*byte[] receiveBuffer = new byte[1024];
		            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		            socket.receive(receivePacket);
		            String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
		            System.out.println("Response from server: " + serverResponse);
		            */
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
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
	
	public int getport() {
		return Integer.parseInt(Port_Field.getText());
	}
	
	public void work() {
		
	}

}
