/*
 * Client App upon TCP
 *
 * Weiying Zhu
 *
 */ 

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class HW4Client {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        
        // Ask user for host name or IP address:
        System.out.println("Please input the Host Name (or ip-address) of your SMTP server:");
        String hostName = sysIn.readLine();
        
        try {
            tcpSocket = new Socket(hostName, 5050);
            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);                    // client
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));  // server
            
            System.out.println("Connection successful! Waiting on first contact...");
            
            // Wait for 200 OK response from server:
            System.out.println(socketIn.readLine());
            // Wait for address from server:
            System.out.println(socketIn.readLine());
            
            System.out.println("First contact made!");
            
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "  + hostName);
            System.exit(1);
        }
        
        // Prompt user for sender's email address, recipient's email address, subject, and email contents:
        System.out.println("Please input the sender's email address:");
        String sender = sysIn.readLine();
        
        System.out.println("Please input the recipient's email address:");
        String recipient = sysIn.readLine();
        
        System.out.println("Please input the email subject:");
        String subject = sysIn.readLine();
        
        System.out.println("Please input the email message. "
                         + "\nEnd input with a single period alone on a line (\".\"):");
        
        String message = "",
               body = "";
        while (!message.equals(".")) {
           message = sysIn.readLine();
           body += message;  // double-check whether .nextLine() strips off newlines
        }
        
        System.out.println("All input accepted.");
        
        // implement steps 4 & 5.
        socketOut.println("HELO bb.com");
        System.out.println(socketIn.readLine());
        
        socketOut.println("MAIL FROM: " + sender);
        System.out.println(socketIn.readLine());
        
        socketOut.println("RCPT TO: " + recipient);
        System.out.println(socketIn.readLine());
        
        socketOut.println("Subject line: " + subject);
        System.out.println(socketIn.readLine());
        
        socketOut.println("<Mail message>\n.\n");
        System.out.println(socketIn.readLine());
        

        socketOut.close();
        socketIn.close();
        sysIn.close();
        tcpSocket.close();
    }
}