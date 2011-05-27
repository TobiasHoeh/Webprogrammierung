import java.net.*;
import java.io.*;

public class ProxyServerAufgabe22Loesung {

	public static void getFile(String command[], Socket clientS) {
		// processing command
		int startURL = command[0].indexOf(' ') + 1;
		int endURL = command[0].indexOf(' ', startURL + 1);
		// DON'T get rid of 'HTTP/1.1' stuff
		// String trimmedCommand = command.substring(0, endURL);
		URL url = null;
		try {
			url = new URL(command[0].substring(startURL, endURL));
		}// try
		catch (MalformedURLException e) {
			System.err.println(e);
		}

		try {
			int c; // one character to read
			// get OutputStream for client to send back the file
			BufferedOutputStream originOut = new BufferedOutputStream(
					clientS.getOutputStream());

			Socket serverS = new Socket(url.getHost(), 80);
			// where to forward the command
			PrintWriter out = new PrintWriter(serverS.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(
					serverS.getInputStream());

			// send command and everything else
			for (int i = 0; !command[i].equals(""); i++) {
				out.println(command[i]);
				System.err.println("SENDING:" + command[i]);
			}
			out.println("\r\n"); // send TERMINATION-Code
			out.flush();

			// receive answer and forward to Client
			while ((c = in.read()) != -1) {
				originOut.write(c);
				// System.err.println("*** GOT BACK:" +c);
			}

			// finally close everything
			originOut.close();
			serverS.close();
			clientS.close();
		} // try
		catch (UnknownHostException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}// getFile

	public static void main(String args[]) {
		try {
			ServerSocket server = new ServerSocket(4001);
			System.err.println("** Proxy Server started and listening");
			while (true) {
				Socket clientS = server.accept(); // keep waiting for commands
				BufferedReader netIn = new BufferedReader(
						new InputStreamReader(clientS.getInputStream()));
				// read command and meta infos from client
				String command[] = new String[64];
				for (int i = 0; i < 64; i++) {
					command[i] = netIn.readLine();
					System.err.println("*** RECEIVED: [" + command[i] + "]");
					if (command[i].equals(""))
						break; // Look for TERMINATION-Code
				}

				getFile(command, clientS);
			} // while
		} // try
		catch (Exception e) {
			System.err.println(e);
		}
	} // main

} // class Proxy
