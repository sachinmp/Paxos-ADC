import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPConnection implements Connection{

	private String hostname;
	private int portNo;
	private Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	public TCPConnection(String hostname, int portNo) {
		// TODO Auto-generated constructor stub
		this.hostname = hostname;
		this.portNo = portNo;
	}
	@Override
	public boolean createConnection() {
		// TODO Auto-generated method stub
		
		try{
			System.out.println("Creating connection");
			socket = new Socket(hostname, portNo);
			System.out.println("Created socket");
			output = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Created output");
			input = new ObjectInputStream(socket.getInputStream());
			System.out.println("Creating input");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean send(Packet p) {
		// TODO Auto-generated method stub
		try{
			//System.out.println(new String(data));
			output.writeObject(p);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public Packet receive() {
		// TODO Auto-generated method stub
		try{
			Packet inputMessage = (Packet) input.readObject();
			//input.reset();
			return inputMessage;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

}
