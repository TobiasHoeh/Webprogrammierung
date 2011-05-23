import java.io.*;

import java.net.*;

public class HttpClientAufgabe21 {

	public static final int httpport = 80;

	public static final String pfad = "Q:/Hoehmann, Tobias/";

	public static void main(String[] args) {

		URL url = null;
		String adresse = "localhost";

		if (args.length > 0) {
			adresse = args[0];
		}
		PrintWriter networkOut = null;
		BufferedInputStream networkIn = null;
		Socket s;
		
		if(1 == 0) {
			
		}

		try {
			url = new URL(adresse);
			s = new Socket(url.getHost(), httpport);
			networkIn = new BufferedInputStream(s.getInputStream());
			networkOut = new PrintWriter(s.getOutputStream());

		} catch (UnknownHostException e) {
			System.err.println("Unable to resolve host");
		} catch (IOException e) {
			System.err.println("FEHLER");
			e.printStackTrace();
		}

		System.out.println("Alles ready");
		String anfrage = "GET " + url.getPath();
		networkOut.println(anfrage);
		networkOut.flush();

		try {
			int index = url.getPath().lastIndexOf("/");
			String dateiname = url.getPath().substring(index,
					url.getPath().length());
			System.out.println(dateiname);
			saveFile(networkIn, pfad + dateiname);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveFile(BufferedInputStream reader, String dateiname)
			throws Exception {

		// FileWriter datei = new FileWriter(pfad + "test" + dateityp);

		FileOutputStream datei = new FileOutputStream(dateiname);
		try {
			byte[] buffer = new byte[1024];
			// String antwortpart;
			int n;
			while ((n = reader.read(buffer)) != -1) {
				datei.write(buffer, 0, n);
			}

		} catch (Exception e) {
			System.err.println("FEHLER");
		} finally {
			datei.close();
		}
	}

}
