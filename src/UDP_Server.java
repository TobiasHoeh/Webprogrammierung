import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

public class UDP_Server {
	byte[] data = new byte[1024];
	DatagramSocket server;

	public static void main(String[] args) {
		UDP_Server server = new UDP_Server(5999);

	}

	public UDP_Server(int port) {

		try {
			server = new DatagramSocket(port);
			DatagramPacket empfang = new DatagramPacket(data, 1024);
			while (true) {
				try {

					server.receive(empfang);
					System.out.println(empfang.getAddress().toString());
					String nachricht = new String(empfang.getData(), 0,
							empfang.getLength());

					System.out.println(nachricht);

					// //////////////////////////////////////////////////////////////////////////
					// //////////////////////// VERARBEITUNG DES SERVERS

					if (nachricht.contains("READ")) {
						int beginn = nachricht.lastIndexOf("READ ");
						String filename = nachricht.substring(
								nachricht.indexOf("READ ") + 5,
								nachricht.indexOf(","));
						String str_line_no = nachricht.substring(nachricht
								.lastIndexOf(",") + 1);

						int line_no = Integer.parseInt(str_line_no);
						if (line_no < 1) {
							System.out.println("Die line_no ist kleiner als 1");
						} else {
							BufferedReader fileIn = new BufferedReader(
									new FileReader(filename));
							for (int i = 1; i < line_no; i++) {
								fileIn.readLine();
							}
							String zeile = fileIn.readLine();

							// //////////////////////////////////////////////////////////////////////////
							// //////////////////////// SENDEN des SERVERS

							byte[] sendung = zeile.getBytes();
							InetSocketAddress client = new InetSocketAddress(
									empfang.getAddress(), empfang.getPort());
							DatagramPacket sendePaket = new DatagramPacket(
									sendung, sendung.length, client);
							server.send(sendePaket);

						}
					}
					if (nachricht.contains("WRITE")) {

						nachricht = nachricht.substring(6);

						String[] split_nachricht = nachricht.split(",");

						String filename = split_nachricht[0];
						String str_line_no = split_nachricht[1];
						String data = split_nachricht[2];

						System.out.println(filename + " " + str_line_no + " "
								+ data);

						int line_no = Integer.parseInt(str_line_no);

						BufferedReader fileIn = new BufferedReader(
								new FileReader(filename));

						ArrayList<String> zeilen = new ArrayList<String>();

						String zeile = "";

						while ((zeile = fileIn.readLine()) != null) {
							zeilen.add(zeile);
						}
						fileIn.close();
						zeilen.set(line_no, data);
						String schreiben = "";
						PrintWriter fileOut = new PrintWriter(new FileWriter(
								filename));

						for (Iterator iterator = zeilen.iterator(); iterator
								.hasNext();) {
							String string = (String) iterator.next();
							fileOut.println(string);
						}

						// fileOut.write(data);
						fileOut.flush();
						fileOut.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public InetAddress getInetAdresse() {
		return server.getInetAddress();
	}

}
