public class Learner {
	//flexible, handle int and string, other
	//GET 8
	//message - accept, result
	//make key value store available
	public Learner() {
	}
	
	public Packet writeToDataStore(Packet commitMessage) {
        Packet response = new Packet();
		//convert commit message to the operation
		 if(commitMessage.getMainOp() == 1){
         	String key = commitMessage.getKey();
         	if(Server.keyValueMap.get(key) != null){
         		response.setOperation((short) 5);
         		response.setValue(Server.keyValueMap.get(key));
         	}else{
         		response.setOperation((short) 6);
         	}
         }else if(commitMessage.getMainOp() == 2){
         	synchronized(Server.keyValueMap){
         		Server.keyValueMap.put(commitMessage.getKey(), commitMessage.getvalue());
         	}
         	response.setOperation((short) 4);
         }else if(commitMessage.getMainOp() == 3){
         	synchronized (Server.keyValueMap) {
         		Server.keyValueMap.remove(commitMessage.getKey());
				}
         	response.setOperation((short) 7);
         }else{
         	System.out.println("Unknown operation requested");
         	response.setOperation((short) -1);
         }
         return response;
		
	}
}
	
	
