import java.net.*;
import java.io.*;

public class HttpClientAufgabe21Loesung {
	public static void saveFile(URL url) {
		int b; // a single byte
		Socket s = null; // client socket to server
		PrintWriter out = null; // HTTP command
		BufferedInputStream in = null; // return data stream
		BufferedOutputStream f = null; // output file
		try {
			int port = -1;
			if ((port = url.getPort()) < 0)
				port = 80; // default port
			s = new Socket(url.getHost(), port); // connect to server

			out = new PrintWriter(s.getOutputStream());
			in = new BufferedInputStream(s.getInputStream());
			int pos = url.getFile().lastIndexOf('/'); // extract file name
			String filename = url.getFile().substring(pos + 1);
			f = new BufferedOutputStream(new FileOutputStream(filename));
			System.err.print("** Anfordern von <" + url + "> ...");
			out.println("GET " + url); // send command to server
			out.flush();
			while ((b = in.read()) != -1)
				f.write(b); // store data into local file
			System.err.println("... fertig");
		} // try
		catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				if (f != null)
					f.close(); // close file, flush buffer
				if (out != null)
					out.close();
				if (in != null)
					in.close();
				if (s != null)
					s.close();
			} catch (Exception e) {
			}
		}
	}// getFile

	public static void main(String args[]) {
		// 1. Kommandozeilenparameter = URL
		// z.B.: http://cruncher.dhbw-mannheim.de/index.php
		try {
			if (args.length < 1)
				System.out.println("*** Parameter-Fehler: Aufruf mittels "
						+ " 'java HttpGet url' ");
			else {
				URL myUrl = new URL(args[0]);
				saveFile(myUrl);
			}
		} catch (MalformedURLException e) {
			System.err.println(e);
		}
	} // main
} // class HttpGet
