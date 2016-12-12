import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Proposer{
	private final static int QUORUM_SIZE = 2;

	private Messenger messenger;
	private Integer currentProposerId = -1;
	//from all the different acceptors 
	private BlockingQueue<Packet> requestQueue = new LinkedBlockingQueue<Packet>();
	private Integer promisesReceived = 0;
	Packet onFlightCommit = null;
	Packet commitResult = null;

	public Proposer(Messenger messenger){
		this.messenger = messenger;
	}

	public synchronized boolean prepare() {
		//dont need any promises. starting fresh
		try{

			onFlightCommit = null;
			promisesReceived = 0;
			currentProposerId = currentProposerId + 1;
			Packet packet = new Packet();
			packet.setOperation((short)15);
			packet.setSequenceNo(currentProposerId);
			packet.setServerName(InetAddress.getLocalHost().getHostName());
			//System.out.println("ServerName:"+InetAddress.getLocalHost().getHostName());
			ExecutorService es = Executors.newCachedThreadPool();
			List<ServerConnection> acceptorSockets = messenger.getAcceptorConnections();
			for(ServerConnection acceptor : acceptorSockets){
				ProposerThread pt = new ProposerThread(acceptor, packet, this);
				es.execute(pt);
			}
			es.shutdown();
			boolean finshed = es.awaitTermination(3000, TimeUnit.SECONDS);
			//send to every acceptor
			if(promisesReceived < QUORUM_SIZE){
				System.out.println("Proposal failed");
				return false;
			}
			//System.out.println("Promise received");
			return true;

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public synchronized boolean accept(Packet packet){

		try{

			promisesReceived = 0;
			packet.setMainOp(packet.getOperation());
			packet.setOperation((short)18);
			packet.setSequenceNo(currentProposerId);
			packet.setServerName(InetAddress.getLocalHost().getHostName());
			ExecutorService es = Executors.newCachedThreadPool();
			List<ServerConnection> acceptorSockets = messenger.getAcceptorConnections();
			for(ServerConnection acceptor : acceptorSockets){
				ProposerThread pt = new ProposerThread(acceptor, packet, this);
				es.execute(pt);
			}
			es.shutdown();
			boolean finshed = es.awaitTermination(3000, TimeUnit.SECONDS);
			if(promisesReceived < QUORUM_SIZE){
				return false;
			}
			return true;

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public synchronized boolean commit(){
		try{
			commitResult = null;
			promisesReceived = 0;
			Packet packet = new Packet();
			packet.setOperation((short)20);
			packet.setSequenceNo(currentProposerId);
			packet.setServerName(InetAddress.getLocalHost().getHostName());
			ExecutorService es = Executors.newCachedThreadPool();
			List<ServerConnection> acceptorSockets = messenger.getAcceptorConnections();
			for(ServerConnection acceptor : acceptorSockets){
				ProposerThread pt = new ProposerThread(acceptor, packet, this);
				es.execute(pt);
			}
			es.shutdown();
			boolean finshed = es.awaitTermination(3000, TimeUnit.SECONDS);
			if(promisesReceived < QUORUM_SIZE){
				return false;
			}
			//System.out.println("Commit completed successfully");
			return true;

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public Packet getOnFlightCommit() {
		return onFlightCommit;
	}
	public void setOnFlightCommit(Packet onFlightCommit) {
		synchronized (this.onFlightCommit) {
			this.onFlightCommit = onFlightCommit;
		}
		
	}

	public void incrementPromises(){
		//System.out.println("Incrementing promise values");
		synchronized(promisesReceived){
			promisesReceived++;
		}
	}
	public int getCurrentProposerId() {
		return currentProposerId;
	}
	public void setCurrentProposerId(int currentProposerId) {
		synchronized (this.currentProposerId) {
			if(this.currentProposerId < currentProposerId){
				this.currentProposerId = currentProposerId;
			}
		}

	}

	public Packet getCommitResult() {
		return commitResult;
	}

	public void setCommitResult(Packet commitResult) {

		synchronized (commitResult) {
			if(this.commitResult == null){
				//System.out.println("Commit result written "+commitResult.getOperation());
				this.commitResult = commitResult;
			}
		}

	}

}