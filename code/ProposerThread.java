
public class ProposerThread implements Runnable{
	
	private ServerConnection serverConnection;
	private Packet packet = new Packet();
	Proposer proposer;
	
	public ProposerThread(ServerConnection serverConnection, Packet packet, Proposer proposer) {
		
		// TODO Auto-generated constructor stub
		this.serverConnection = serverConnection;
		this.packet = packet;
		this.proposer = proposer;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			//System.out.println("Sending proposal");
			serverConnection.getOutput().writeObject(packet);
			serverConnection.getOutput().reset();
			Packet response = (Packet) serverConnection.getInput().readObject();
			if(response.getOperation() == 16){
				//System.out.println("Promise for request obtained");
				// Acknowledgement received
				proposer.incrementPromises();
			}else if(response.getOperation() == 17){
				//Promimse with value.
				//System.out.println("Promise with value for request obtained");
				proposer.setOnFlightCommit(response);
			}else if(response.getOperation() == 21){
				//System.out.println("Reject for request obtained");
				proposer.setCurrentProposerId(response.getSequenceNo());
			}else if(response.getOperation() == 19){
				//System.out.println("Acknowledgement for request obtained");
				proposer.incrementPromises();
			}else{
				//System.out.println("commit for request obtained: "+response.getOperation());
				proposer.incrementPromises();
				proposer.setCommitResult(response);
				
			}
			//serverConnection.getInput().reset();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
