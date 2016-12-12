
public interface Connection {

	
	boolean createConnection();
	
	boolean send(Packet p);
	
	Packet receive();
}
