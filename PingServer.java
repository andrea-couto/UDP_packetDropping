
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

class PingServer {
	public static void main(String args[]) throws Exception {
		@SuppressWarnings("resource")
		DatagramSocket serverSocket = new DatagramSocket(2014); 

		byte[] receiveData = new byte[1024];

		System.out.println("PingServer running...\n");

		while (true) {

			Random random = new Random();

			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			serverSocket.receive(receivePacket);

			String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
			InetAddress IPAddress = receivePacket.getAddress(); 
			int port = receivePacket.getPort(); 
			
			int clientPort = 2014;
			System.out.println("client's port # = " + clientPort);
			System.out.println("client's IP = " + IPAddress);
			System.out.println("client's message = " + sentence);

			String capitalizedSentence = sentence.toUpperCase();

			int rand = random.nextInt(10);

			if (rand < 4) {
				System.out.println("Reply not sent");
				continue;
			}

			byte[] sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			System.out.println("Reply to the client sent");
		}
	}
}
