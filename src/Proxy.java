/*
 * Bharat Gupta
 * EECS 325
 * PROJ 1
 * 11/3/2012
 * A simple proxy that handles basic HTTP requests.
 * Waits for a connection request, and then creates a 
 * thread for that request.
 */
import java.net.*;
import java.io.*;
public class Proxy {
	
	 public static void main(String[] argz) throws IOException {
	        
		 ServerSocket ss = null;
	        // specify the port number here.
	        int port = 5012;	//5000+12
	        
	        if(argz.length > 0) {
	        	
	        	port = Integer.parseInt(argz[0]);
	        }
	        try {
	            ss = new ServerSocket(port);
	            System.out.println("Waiting for a request at port " + port);
	        } catch (IOException e) {
	            System.err.println("Couldn't listen to port number " + port);
	            System.exit(-1);
	        }

	        // indefinitely wait for connection requests.
	        while (true) {

	        	new ProxyThread(ss.accept()).start();
	        }
	    }
	    
}
