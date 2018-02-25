/*
 * Server App upon TCP
 * A thread is created for each connection request from a client
 * So it can handle Multiple Client Connections at the same time
 * Weiying Zhu
 */ 

import java.net.*;
import java.io.*;

public class HW4MultiServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverTCPSocket = null;
        boolean listening = true;

        try {
            serverTCPSocket = new ServerSocket(5050);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5050.");
            System.exit(-1);
        }

        while (listening){
	    		new HW4MultiServerThread(serverTCPSocket.accept()).start();
            System.out.println("Connected to client, starting thread...");
		  }
			
        serverTCPSocket.close();
    }
}