import java.io.Serializable;

public class Packet implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2611106465984206402L;
	private int sequenceNo = -1;
	private short operation = -1;
	private short mainOp = -1;
	private String key;
	private String value;
	private String serverName;
	
	public Packet(){
		
	}
	/**
	 * Operation value and what it means
	 * --------------------------------
	 * 
	 * 1 - GET
	 * 2 - PUT
	 * 3 - DELETE
	 * 4 - Successful completed PUT operation
	 * 5 - Contains value of a key
	 * 6 - Value requested for a key not in the map
	 * 7 - Deletion operation completed successfully
	 * 15 - Prepare Message
	 * 16 - Promise Message
	 * 17 - Promise with value
	 * 18 - Accept Message
	 * 19 - Acknowledge
	 * 20 - Commit Message
	 * 21 - Reject Message
	 * @param commandParts
	 * @param sequenceNo
	 */
	public Packet(String[] commandParts, int sequenceNo){
		this.sequenceNo = sequenceNo;
		if("GET".equalsIgnoreCase(commandParts[0])){
			operation = 1;
			key =  commandParts[1];
		}else if("PUT".equalsIgnoreCase(commandParts[0])){
			operation = 2;
			key = commandParts[1];
			value = commandParts[2];
		}else if("DELETE".equalsIgnoreCase(commandParts[0])){
			operation = 3;
			key = commandParts[1];
		}
	}
	public Packet(int sequenceNo, short operation, String key, String value){
		this.sequenceNo = sequenceNo;
		this.operation = operation;
		this.key = key;
		this.value = value;
				
	}
	
	public void setOperation(short operation){
		this.operation = operation;
	}
	
	public short getOperation(){
		return operation;
	}
	
	public int getSequenceNo(){
		return sequenceNo;
	}
	
	public void setSequenceNo(int sequenceNo){
		this.sequenceNo = sequenceNo;
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getvalue(){
		return value;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	@Override
	protected Packet clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Packet) super.clone();
	}
	public short getMainOp() {
		return mainOp;
	}
	public void setMainOp(short mainOp) {
		this.mainOp = mainOp;
	}
}
