import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {

	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	
	public ServerConnection(Socket socket, ObjectInputStream reader, ObjectOutputStream writer) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.input = reader;
		this.output = writer;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public void setInput(ObjectInputStream input) {
		this.input = input;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}
}
