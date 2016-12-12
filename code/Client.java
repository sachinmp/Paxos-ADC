import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class Client {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection connection = null;
		int sequenceNo = 0;
		if(args.length != 3){
			System.out.println("Incorrect no of arguments provided. Client <TCP/UDP/RMI> <hostname> <portNo>");
			System.exit(1);
		}
		
		try{
		String mode = args[0];
		String hostName = args[1];
		int portNo = Integer.parseInt(args[2]);
		
		if(mode.equalsIgnoreCase("TCP")){
			System.out.println("Creating TCP connection");
			connection = new TCPConnection(hostName, portNo);
			boolean status = connection.createConnection();
			if(!status){
				System.out.println("Error while creating TCP connection");
				System.exit(1);
			}
			
		}else if(mode.equalsIgnoreCase("UDP")){
			/*connection = new UDPConnection(InetAddress.getByName(hostName), portNo);
			boolean status = connection.createConnection();
			if(!status){
				System.out.println("Error while creating UDP connection");
				System.exit(1);
			}*/
		} else if(mode.equalsIgnoreCase("RMI")){
			
		}else{
			System.out.println("Invalid mode :"+mode);
			System.exit(1);
		}
		System.out.println("Waiting for input");
		BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		System.out.println("Choose the input type: (File/Console)");
		String choice = consoleInput.readLine();
		if(choice.equalsIgnoreCase("File")){
			System.out.println("Enter the path of the file:");
			String filePath = consoleInput.readLine();
			File commandFile = new File(filePath);
			if(commandFile.exists()){
				BufferedReader br = new BufferedReader(new FileReader(commandFile));
				String line;
			    while ((line = br.readLine()) != null) {
			    	String [] commandParts = line.split(",");
					Packet input = new Packet(commandParts,sequenceNo++);
					//byte[] dataPacket = encodeco.encodeData(input);
					long startTime = System.nanoTime();
                    connection.send(input);
                    Packet response = connection.receive();
                    long endTime = System.nanoTime();
                    System.out.println("Operation "+input.getOperation()+": "+(endTime-startTime));
					//Packet p = encodeco.decodeData(response);
					if(response !=null){
						if(response.getOperation() == 4){
							System.out.println("Put operation completed successfully");
						}else if(response.getOperation() == 5){
							System.out.println("Value for key is : "+response.getvalue());
						}else if(response.getOperation() == 6){
							System.out.println("The key not present in the map");
						}else if(response.getOperation() == 7){
							System.out.println("Deletion operation done");
						}
					}else{
						System.out.println("The operation failed to complete");
					}
			    }
			}
		}else{
		while((userInput = consoleInput.readLine()) != null){
		
		String [] commandParts = userInput.split(",");
		Packet input = new Packet(commandParts,sequenceNo++);
		//byte[] dataPacket = encodeco.encodeData(input);
		connection.send(input);
		Packet response = connection.receive();
		//Packet p = encodeco.decodeData(response);
		if(response !=null){
			if(response.getOperation() == 4){
				System.out.println("Put operation completed successfully");
			}else if(response.getOperation() == 5){
				System.out.println("Value for key is : "+response.getvalue());
			}else if(response.getOperation() == 6){
				System.out.println("The key not present in the map");
			}else if(response.getOperation() == 7){
				System.out.println("Deletion operation done");
			}
		}else{
			System.out.println("The operation failed to complete");
		}
		}
		}
		}catch(Exception e){
			System.exit(1);
		}
	}

}
