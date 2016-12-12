import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPSeverThread implements Runnable{

	private Socket socket = null;
	private Proposer proposer;
	int check = 0;

	public TCPSeverThread(Socket socket, Proposer proposer) {
		this.socket =  socket;
		this.proposer = proposer;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			System.out.println("Starting tcp server thread");
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			//String inputLine;
			Packet packet;
			while((packet = (Packet) input.readObject()) != null){
				//Packet pac = encodeco.decodeData(inputLine.getBytes());
				Packet response = new Packet();
				if(!doPrepareStage()){
					//send an error message back
					response.setOperation((short) -1);
					output.writeObject(response);
					continue;
				}
				while(!proposer.accept(packet)){
					doPrepareStage();
				}
				if(!proposer.commit()){
					response.setOperation((short) -1);
					output.writeObject(response);
					continue;
				}
				//System.out.println("Sending the result of commit"+proposer.getCommitResult().getOperation());
				output.writeObject(proposer.getCommitResult());
				output.reset();

				/*if(packet.getOperation() == 1){
					String key = packet.getKey();
					synchronized(Server.keyValueMap){
						if(Server.keyValueMap.get(key) != null){
							response.setOperation((short) 5);
							response.setValue(Server.keyValueMap.get(key));
						}
						else{
							response.setOperation((short) 6);
						}
					}
				}else if(packet.getOperation() == 2){
					//pac.setOperation((short)10);
					if(coord.doTwoPhaseCommit(packet)){

						synchronized(Server.keyValueMap){
							Server.keyValueMap.put(packet.getKey(), packet.getvalue());
						}
					}

					response.setOperation((short) 4);
				}else if(packet.getOperation() == 3){
					if(coord.doTwoPhaseCommit(packet)){
						synchronized (Server.keyValueMap) {
							Server.keyValueMap.remove(packet.getKey());
						}
						response.setOperation((short) 7);
					}
				}else{
					System.out.println("Unknown operation requested");
					response.setOperation((short) -1);
				}

				//byte[] responseByte = encodeco.encodeData(response);
				output.writeObject(response);*/
				//input.reset();
			}
		}catch(Exception e){

		}


	}


	public boolean doPrepareStage(){
		try{
			boolean status = proposer.prepare();
			if(!status){
				check++;
				//try again
				if(proposer.getOnFlightCommit() != null){
					if(check ==2){
						proposer.commit();
						check = 0;
					}else{
						Thread.sleep(10);
					}
				}
				status = proposer.prepare();
			}else{
				check = 0;
				return status;
			}
			if(!status){
				proposer.setCurrentProposerId(proposer.getCurrentProposerId()+10);
				 status = doPrepareStage();
			}
			return status;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
