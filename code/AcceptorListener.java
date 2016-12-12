import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AcceptorListener implements Runnable{

	private ServerSocket serverSocket;
	private Acceptor acceptor;
	
	public AcceptorListener(ServerSocket serverSocket, Acceptor acceptor) {
		// TODO Auto-generated constructor stub
		this.serverSocket = serverSocket;
		this.acceptor = acceptor;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(true){
				Socket clientSocket = serverSocket.accept();
				AcceptorClient coordClient = new AcceptorClient(clientSocket,acceptor);
				Thread thread = new Thread(coordClient);
				thread.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
