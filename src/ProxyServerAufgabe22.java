import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.io.OutputStreamWriter;

import java.io.PrintWriter;

import java.net.*;

public class ProxyServerAufgabe22 {

	public final static int DEFAULT_SERVER_PORT = 8081;

	public static final int HTTP_PORT = 80;

	public static void main(String[] args) {

		int port = DEFAULT_SERVER_PORT;

		try {

			ServerSocket server = new ServerSocket(port);

			Socket browserConnection = null;

			BufferedOutputStream out = null;

			boolean http1 = false;

			while (true) {

				try {

					browserConnection = server.accept();

					BufferedReader in = new BufferedReader(
							new InputStreamReader(
									browserConnection.getInputStream()));

					System.out.println(in.toString());

					// ///////////////////////////////////////////////////////////////////////////////////

					// ///// CLIENT BEREICH

					// ///////////////////////////////////////////////////////////////////////////////////

					URL url = null;

					String adresse = in.readLine();

					adresse = adresse.substring(4, adresse.indexOf(" HTTP"));

					if (adresse.contains(" HTTP /1.1")) {

						http1 = true;

					}

					System.out.print(adresse);

					PrintWriter networkOut = null;

					BufferedInputStream networkIn = null;

					Socket s;

					if (adresse.length() == adresse.indexOf(".gif") + 4) {

						throw new Exception("Keine Gifs!!!");

					}

					try {

						url = new URL(adresse);

						s = new Socket(url.getHost(), HTTP_PORT);

						networkIn = new BufferedInputStream(s.getInputStream());

						networkOut = new PrintWriter(s.getOutputStream());

					} catch (UnknownHostException e) {

						System.err.println("Unable to resolve host");

					} catch (IOException e) {

						System.err.println("FEHLER");

						e.printStackTrace();

					}

					System.out.println("Alles ready");

					String anfrage = "oh maaan";

					if (http1 = true) {

						anfrage = "GET "
								+ url.getPath()
								+ " HTTP/1.1\nHost: "
								+ url.getHost()
								+ "\nUser-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; de-DE; rv:1.7.12) Gecko/20050919 Firefox/1.0.7\nConnection: close\n";

					} else {

						anfrage = "GET " + url;

					}

					System.out.println(anfrage);

					networkOut.println(anfrage);

					networkOut.flush();

					// ///////////////////////////////////////////////////////////////////////////////////

					// ///// CLIENT BEREICH

					// ///////////////////////////////////////////////////////////////////////////////////

					out = new BufferedOutputStream(
							browserConnection.getOutputStream());

					int n;

					while ((n = networkIn.read()) != -1) {

						out.write(n);

					}

					out.flush();

					out.close();

				} catch (Exception e) {

					System.out.println(e.getMessage());

					out = new BufferedOutputStream(
							browserConnection.getOutputStream());

					out.flush();

					out.close();

				} finally {

					// browserConnection.close();

				}

			}

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} finally {

		}

	}

}
