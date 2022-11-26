package chat;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class server extends JFrame {
	ServerSocket server;
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	//declare components
		private JLabel heading=new JLabel("server area");
		private JTextArea messageArea=new JTextArea();
		private JTextField messageInput=new JTextField();
		private Font font=new Font("Roboto",Font.PLAIN,20);
	
		
		
	//constructor
	public server() {
		
		
		try {
			server=new ServerSocket(7777);
			System.out.println("server is ready to accept connection");
			System.out.println("waiting...");
			socket=server.accept();
			
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out =new PrintWriter(socket.getOutputStream());
			
			createGUI();
			handleEvents();
			startReading();
			startWriting();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
				
	}
	
	

	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("key released"+e.getKeyCode());
				if(e.getKeyCode()==10) {
					//System.out.println("you have pressed enter button");
					String contentToSend=messageInput.getText();
					messageArea.append("Me :"+contentToSend+"\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
			
		});
	}
	
	public void createGUI() {
		
		this.setTitle("server Messager[END]");
		this.setSize(600,700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// coding for component
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		//heading.setIcon(new ImageIcon("clogo.png"));
		//heading.setHorizontalAlignment(SwingConstants.CENTER);
		//heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		//heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.setLayout(new BorderLayout());	
		
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane=new JScrollPane(messageArea);
		this.add(messageArea,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.setVisible(true);
	}
	

	// thread - it will read the data
	public void startReading() {
		
		Runnable r1=()->{
			
			System.out.println("reader started..");
			try {
			while(true) 
			{
			
				String msg=br.readLine();
				   if(msg.equals("exit"))
				   {
					  System.out.println("client terminated the chat");
					  JOptionPane.showMessageDialog(this,"client Terminate the chat" );
					  messageInput.setEnabled(false);
					  socket.close();
					  break;
				   } 
				
				    System.out.println("client: "+msg);
				    messageArea.append("client: "+msg+"\n");
				
			}
			}
			catch(Exception e)
			{
				//e.printStackTrace();
				System.out.println("connection closed");
			}
			};
			
			new Thread(r1).start();
		};
		
	
	// thread - will take data from user and will send it to the client
	public void startWriting() {
		
         Runnable r2=()->{
        	 System.out.println("writer started..");
			try 
			{
        	 while(!socket.isClosed())
			 {
				
					BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
					String content=br1.readLine();
					
					out.println(content);
					out.flush();
					
					if(content.equals("exit")) {
						socket.close();
						break;
					}
			 }	
				
        	 System.out.println("connection is closed");	
			}
			catch(Exception e){
				//e.printStackTrace();
				System.out.println("connection closed");
			}
		};
		
		new Thread(r2).start();
		
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("this is server... going to start server ");
		
		new server();
	
	}

}

