package unitTest;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class client {
	private static final String SERVER_IP = "192.168.0.8";
	private static final int SERVER_PORT = 8888;

	public static void main(String[] args) {
		String name = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("대화명 입력 :");
		name = sc.nextLine();
		
		sc.close();
		
		Socket socket = null;
		
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			System.out.println("connected successfully");
			
			new ChatWindow(name, socket);
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),true);
			String request = "join:" + name + "\r\n";
			pw.println(request);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
