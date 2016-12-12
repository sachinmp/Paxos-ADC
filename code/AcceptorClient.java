import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class AcceptorClient implements Runnable{

	private Socket socket = null;
	private Acceptor acceptor;
	
	public AcceptorClient(Socket socket, Acceptor acceptor) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.acceptor = acceptor;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
	        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
	        Packet packet; 
	        while((packet = (Packet) input.readObject()) != null){
	        	
	        	if(packet.getOperation() == 15){
	        		//System.out.println("15 - Prepare Message received");
	        		acceptor.receiveProposal(packet,output);
	        	}else if(packet.getOperation() == 18){
	        		//System.out.println("18 - Accept Message received");
	        		acceptor.receiveAcceptRequest(packet, output);
	        	}else if(packet.getOperation() == 20){
	        		//System.out.println("20 - Commit Message received");
	        		acceptor.receiveCommitRequest(packet, output);
	        	}else{
	        		
	        		System.out.println("Something went wrong somewhere. Printing from Acceptor client"+ packet.getOperation());
	        	}
	        	//input.reset();
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
