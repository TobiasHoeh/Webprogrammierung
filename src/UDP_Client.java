import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDP_Client {
	public static void main(String[] args) {
		UDP_Client server = new UDP_Client();
	}

	public UDP_Client() {
		try {
			DatagramSocket client = new DatagramSocket();
			BufferedReader userIn = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {
				String message = "";
				try {
					// ///////////////////////////////////////////////////////////////////////////////////
					// /////////// SENDE Teil des Clients
					while (true) {
						String theLine;
						theLine = userIn.readLine();
						if (theLine.equals("."))
							break;
						message = message + theLine;
					}
					byte[] sendung = message.getBytes();
					InetSocketAddress server = new InetSocketAddress(
							"127.0.0.1", 5999);
					DatagramPacket sendePaket = new DatagramPacket(sendung,
							sendung.length, server);
					client.send(sendePaket);
					// ///////////////////////////////////////////////////////////////////////////////////
					// /////////// EMPFANG Teil des Clients
					if (message.contains("READ")) {
						byte[] data = new byte[1024];
						DatagramPacket empfang = new DatagramPacket(data, 1024);
						while (true) {
							try {
								client.receive(empfang);
								System.out.println(empfang.getAddress()
										.toString());
								String nachricht = new String(
										empfang.getData(), 0,
										empfang.getLength());
								System.out.println(nachricht);
								break;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
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
}
