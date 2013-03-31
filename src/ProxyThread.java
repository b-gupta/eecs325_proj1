/*
 * Bharat Gupta
 * EECS 325
 * PROJ 1
 * 11/3/2012
 * Creates a thread for a given http request, and then
 * contacts the URL and forwards the information to the
 * requester.
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyThread extends Thread {

	Socket accepted;
	int host, port;
	String url;
	final int BUFFER = 2048;
	
	ProxyThread(Socket a) {
		
		System.out.println("Thread created. ");
		this.accepted = a;
	}
	
	public void run() {

        try {
        	
        	// These will be used to communicate with the browser.
        	BufferedReader in = new BufferedReader(new InputStreamReader(accepted.getInputStream()));
            DataOutputStream out = new DataOutputStream(accepted.getOutputStream());

            String input;
            int index = 0;
            String url = "";
            //begin get request from client
            ArrayList<String> httpreq = new ArrayList<String>();
            
            while ((input = in.readLine()) != null) {
                try {
                    StringTokenizer tok = new StringTokenizer(input);
                    tok.nextToken();
                } catch (Exception e) {
                    break;
                }
                //GET _ URL _ HTTP/1.1
                if (index == 0) {
                    String[] tokens = input.split(" ");
                    url = tokens[1];
                    //can redirect this to output log
                    System.out.println("Request for : " + url);
                }
                else
                	httpreq.add(input);
                System.out.println(input);

                index++;
            }
            
            // default
            String hostname = "www.google.com";
            String directory = "/";
            try {
            	// get rid of "http://"
            	String domain = url.substring(7, url.length());
            	index = domain.indexOf("/");
            	// (actual hostname) hostname = www.domain.com
            	hostname = domain.substring(0, index);
            	// (the file you are trying to request) /dir1/dir2/index.html
            	directory = domain.substring(index, domain.length());
            	System.out.println(hostname);
                System.out.println(directory);
            }
            catch (Exception e) {
            	
            	System.out.println("The URL " + url + " was not processable. The format is: http://www.domain.com/dir1/dir2");
            }
            
            System.out.println("Creating socket for "+ hostname);

            // start connection with the URL
            Socket socket = new Socket(hostname, 80);
            System.out.println("Socket created successfully.");

            // Send a request to the URL using PrintWriter
    		PrintWriter serv = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))); 
    		System.out.println("Sending get request to: " + url);
    		serv.println("GET " + directory + " HTTP/1.1");
    		for(int i = 0; i < httpreq.size(); i++) {
    			
    			serv.println(httpreq.get(i));
    		}
    		serv.println(); 
    		serv.flush(); 

    		// read from the URL into this stream
    		InputStream is = socket.getInputStream();
    		
    		byte [] data = new byte[BUFFER];
    		int check = is.read(data, 0, BUFFER);
    		while(check != -1) {
    			// write the data back to the browser
    			out.write(data, 0, check);
    			check = is.read(data, 0, BUFFER);
    		}

    		// close everything now that we are done.
    		socket.close();
    		in.close();
    		out.close();
    		is.close();
            
    		
        }
        catch (Exception e) {
        	
        	System.err.println("An error occured.\n" + e.getMessage());
        }
            
	}
}