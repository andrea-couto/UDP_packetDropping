
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.text.DateFormat;

public class PingClient implements Runnable {
	private static final int TIMEOUT = 1000;       //wait one second
	private static final int NUM_PACKETS = 10;
	private static final String HOST = "localhost";


	public static void main(String[] args) {
		int port = 2014;
		Runnable start = new PingClient(port);
		start.run();
	}

	private InetAddress Inethost;
	private int port;
	private DatagramSocket socket;
	private int num_packets;

	public PingClient(int port) {
		this.port = port;
		num_packets = NUM_PACKETS;
	}

	public void run() {
		try {
			Inethost = InetAddress.getByName(HOST);
		} catch (UnknownHostException e) {
			System.err.println("Error getting host InetAddress");
		}

		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(TIMEOUT);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}

		int pingNumber = 1;

		while (true) {
			String sendString = generateSendString(pingNumber);
			byte[] sendData = sendString.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Inethost, port);
			long sendTime = System.nanoTime();
			long rtt;
			try {
				socket.send(sendPacket);
			} catch (IOException e) {
				System.err.println("Error sending ping");
			}
			
			// structure to hold message from server
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			try {
				//recieving message from server
				socket.receive(receivePacket);

				//roundtrip delay (sent and recieved time)
				rtt = System.nanoTime() - sendTime;
				rtt = rtt / 1000;
				 
				String message = new String(receiveData);
				message = message.trim();
				
				//RTT printout
				System.out.println("Ping " + pingNumber + ": " + "   From Sever: " + message  + " " + "   RTT=" + rtt + " microseconds\n");  
				
			} catch (SocketTimeoutException e) {

				System.out.println("Ping " + pingNumber + ": Request timed out\n");
			} catch (IOException e) {
				System.err.println("Error recieving ping");
			}

			++pingNumber;
			--num_packets;

			if (num_packets == 0) break;
			try {
				Thread.sleep(TIMEOUT);  // wait 1 second
			} catch (InterruptedException e) {
				System.err.println("Error timing out");
			}
		}
	}

	private static String generateSendString(int pingNumber) {
		String str = "hello world \n";
		DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");  // elapsed time
	    Date dateobj = new Date();
	    System.out.println(df.format(dateobj));
		return str;
	}
}