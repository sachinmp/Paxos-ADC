import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Messenger {
	private static final int PORT_OF_ACCEPTOR = 9000;
	public Map<String, ServerConnection> acceptorConnections = new HashMap<String, ServerConnection>();
	private String filePath;

	public Messenger(String filePath) {
		// TODO Auto-generated constructor stub
		this.filePath = filePath;
		/*try{
			File serverInfo = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(serverInfo));
			String line;
			while ((line = br.readLine()) != null) {
				Socket socket = new Socket(line.trim(), PORT_OF_ACCEPTOR);
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
				ServerConnection acceptorConnection = new ServerConnection(socket, input, output);
				acceptorConnections.put(line, acceptorConnection);
			}
			}catch(Exception e){
				e.printStackTrace();
			}*/
	}

	public void createConnections(){
		try{
			File serverInfo = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(serverInfo));
			String line;
			while ((line = br.readLine()) != null) {
				Socket socket = new Socket(line.trim(), PORT_OF_ACCEPTOR);
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
				ServerConnection acceptorConnection = new ServerConnection(socket, input, output);
				acceptorConnections.put(line, acceptorConnection);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param commitMessage
	 */
	public void sendMessage(String server,Packet message){
		//localhost, PORT_OF_LEARNER
		try{
			acceptorConnections.get(server).getOutput().writeObject(message);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This method is responsible for sending the accept message to the proposer.
	 * The message could be an accept or accept with a value or a reject.
	 * @param serverName
	 * @param packet
	 */
	public void sendAcceptMessage(String serverName, Packet packet) {

	}

	/**
	 * This function is responsible for sending the acknowledgement of a request to the proposer.
	 * @param serverName
	 */
	public void sendAcknowledgement(String serverName){

	}

	/**
	 * This method is responsible for sending the proposal to all the acceptors.	   
	 * @param proposerId
	 */
	public boolean sendProposal(int proposerId){
		return false;
	}

	public List<ServerConnection> getAcceptorConnections() {
		List<ServerConnection> listOfConnections = new ArrayList<ServerConnection>();
		for(String server : acceptorConnections.keySet()){
			listOfConnections.add(acceptorConnections.get(server));
		}
		return  listOfConnections;
	}
	public void setAcceptorConnections(Map<String, ServerConnection> acceptorConnections) {
		this.acceptorConnections = acceptorConnections;
	}
	/**
	 * This function is responsible for sending the promise message to the proposer.
	 * @param serverName
	 * @param p
	 */
	public void sendPromise(String serverName, Packet p){

	}
}
