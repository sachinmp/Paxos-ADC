import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientListener implements Runnable{

	private ServerSocket serverSocket;
	private Proposer proposer;
	
	public ClientListener(ServerSocket serverSocket,Proposer proposer) {
		// TODO Auto-generated constructor stub
		this.serverSocket = serverSocket;
		this.proposer = proposer;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(true){
				Socket clientSocket = serverSocket.accept();
				TCPSeverThread tcpServer = new TCPSeverThread(clientSocket, proposer);
				Thread thread = new Thread(tcpServer);
				thread.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

}