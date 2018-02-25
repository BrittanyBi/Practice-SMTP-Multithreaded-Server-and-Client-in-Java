/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Weiying Zhu
 */ 

import java.net.*;
import java.io.*;

public class HW4MultiServerThread extends Thread {
    private Socket clientTCPSocket = null;

    public HW4MultiServerThread(Socket socket) {
		super("HW4MultiServerThread");
		clientTCPSocket = socket;
    }

    public void run() {

		try {
	 	   PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
	  		BufferedReader cSocketIn = new BufferedReader(
				    new InputStreamReader(
				    clientTCPSocket.getInputStream()));
                
         System.out.println("Thread started. Writing to client...");
         
         // Send 200 OK response & server's IP address / DNS name? for what?
         cSocketOut.println("200 OK");
         InetAddress address = clientTCPSocket.getInetAddress();
         cSocketOut.println(address.toString());
         
         System.out.println("Client contacted.");
         
	      String fromClient, toClient;
			  
	 	   while ((fromClient = cSocketIn.readLine()) != null) {
				
				toClient = fromClient.toUpperCase();
				cSocketOut.println(toClient);
				
				if (fromClient.equals("Bye"))
				    break;
	 	   }
			
		   cSocketOut.close();
		   cSocketIn.close();
		   clientTCPSocket.close();
         
         System.out.println("Client disconnected. Connection terminated.");

		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
}