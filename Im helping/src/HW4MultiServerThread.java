
/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Weiying Zhu
 */

import java.net.*;
import java.io.*;

public class HW4MultiServerThread extends Thread {
	private Socket clientTCPSocket = null;
	PrintWriter cSocketOut = null;
	BufferedReader cSocketIn = null;

	public HW4MultiServerThread(Socket socket) {
		super("HW4MultiServerThread");
		clientTCPSocket = socket;
	}

	public void run() {

		try {
			cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
			cSocketIn = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));

			System.out.println("Thread started. Writing to client...");

			// Send 200 OK response & server's IP address / DNS name? for what?
			cSocketOut.println("200 OK");
			InetAddress address = clientTCPSocket.getInetAddress();
			cSocketOut.println(address.toString());

			System.out.println("Client contacted.");

			System.out.println("Starting state machine...");
			stateMachine(1);

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

	public void stateMachine(int state) throws IOException {
		String fromClient;

		// wait for, read, & display each:
		switch (state) {
		case 0: // received QUIT

			cSocketOut.println("221 <server�s ip> closing connection");
			break;

		case 1: // waiting for HELO
			while (!(fromClient = cSocketIn.readLine()).equals("HELO")
					|| !(fromClient = cSocketIn.readLine()).equals("QUIT")) {
				cSocketOut.println("503 5.5.2 Send hello first");
			}

			// state transitions
			if (fromClient.equals("HELO")) {
				state = 2;
				cSocketOut.println("250 <server�s ip> Hello <client�s ip>");

			} else if (fromClient.equals("QUIT")) {
				state = 0;
			}

			break;

		case 2: // waiting for MAIL FROM
			while (!(fromClient = cSocketIn.readLine()).equals("MAIL FROM")
					|| !(fromClient = cSocketIn.readLine()).equals("QUIT")) {
				cSocketOut.println("503 5.5.2 Need mail command");
			}

			// state transitions
			if (fromClient.equals("MAIL FROM")) {
				state = 3;
				cSocketOut.println("250 2.1.0 Sender OK");

			} else if (fromClient.equals("QUIT")) {
				state = 0;
			}

			break;

		case 3: // waiting for RCPT TO
			while (!(fromClient = cSocketIn.readLine()).equals("RCPT TO")
					|| !(fromClient = cSocketIn.readLine()).equals("QUIT")) {
				cSocketOut.println("503 5.5.2 Need rcpt command");
			}

			// state transitions
			if (fromClient.equals("RCPT TO")) {
				state = 4;
				cSocketOut.println("250 2.1.5 Recipient OK");

			} else if (fromClient.equals("QUIT")) {
				state = 0;
			}

			break;

		case 4: // waiting for DATA
			while (!(fromClient = cSocketIn.readLine()).equals("DATA")
					|| !(fromClient = cSocketIn.readLine()).equals("QUIT")) {
				cSocketOut.println("503 5.5.2 Need data command");
			}

			// state transitions
			if (fromClient.equals("DATA")) {
				state = 5;
				cSocketOut.println("354 Start mail input; end with <CRLF>.<CRLF>");

			} else if (fromClient.equals("QUIT")) {
				state = 0;
			}

			break;

		case 5: // waiting for full email message
			cSocketOut.println("250 Message received and to be delivered");
			break;

		default: // error encountered.
			return;
		}
	}
}