import java.io.ObjectOutputStream;

public class Acceptor {

	private Messenger messenger;
	private int currentProposalNumber = 0;
	private Integer acceptedId;
	private int acceptedValue;
	Packet commitInFlight = null;
	Learner learner = null;

	public Acceptor(Integer currentProposalNumber) {
		this.currentProposalNumber = currentProposalNumber;
	}

	public Acceptor(Messenger messenger){
		this.messenger = messenger;
		learner = new Learner();
	}

	//proposal same as prepare
	//proposal coming in as a a packet
	public synchronized void receiveProposal(Packet proposal, ObjectOutputStream output) {
		//proposal doesn't include get/put operaiton
		try{
			Integer proposalId = proposal.getSequenceNo();
			String serverName = proposal.getServerName();
			if(proposalId < currentProposalNumber) {
				//System.out.println("Proposal id is less than current proposal no");
				Packet response = new Packet();
				response.setOperation((short) 21);
				response.setSequenceNo(currentProposalNumber);
				//still send back acccept id and value
				//TODO - use packet instead. add data to packet for acceptedId and acceptedValue
				//messenger.sendMessage(serverName, response);
				output.writeUnshared(response);
				output.reset();
			} else {
				Packet pack;
				if(commitInFlight != null){
					//System.out.println("There is a commit in flight");
					pack = commitInFlight.clone();
					pack.setOperation((short)17);

				}else{
					//System.out.println("Promise to be granted setting operaton value to 16");
					pack = new Packet();
					pack.setOperation((short)16);
					currentProposalNumber = proposalId;
				}
				//messenger.sendMessage(serverName, pack);
				//System.out.println("Packet to be written has operation value "+pack.getOperation());
				output.writeUnshared(pack);
				output.reset();
			}
			//System.out.println("Promise message sent");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public synchronized void receiveAcceptRequest(Packet accept, ObjectOutputStream output) {
		try{
			int proposalId = accept.getSequenceNo();
			String serverName = accept.getServerName();
			//this to be operation later
			Packet response = new Packet();
			if(proposalId < currentProposalNumber){
				response.setOperation((short) 21);
				//messenger.sendMessage(serverName, response);
				output.writeObject(response);
				output.reset();
			}else if(proposalId == currentProposalNumber){
				//send acknowledgement
				commitInFlight = accept;
				response.setOperation((short) 19);
				//messenger.sendMessage(serverName, response);
				output.writeObject(response);
				output.reset();
			}else{
				System.out.println("Something went wrong somewhere");
			}
			//System.out.println("Accept message replied");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public synchronized void receiveCommitRequest(Packet commit, ObjectOutputStream output){
		try{
			int proposalId = commit.getSequenceNo();
			String serverName = commit.getServerName();
			Packet response = learner.writeToDataStore(commitInFlight);
			response.setSequenceNo(proposalId);
			commitInFlight = null;
			//messenger.sendMessage(serverName, response);
			output.writeObject(response);
			output.reset();
			//System.out.println("Commit message replied");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}