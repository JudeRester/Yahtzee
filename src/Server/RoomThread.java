package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import common.User;

public class RoomThread extends Thread{
	private Socket socket;
	private User user;
	public RoomThread(Socket socket, User user) {
		this.socket = socket;
		this.user = user;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader br;
			PrintWriter pw;
			while(true) {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),true);
				String request = br.readLine();
				if(request == null) {
					System.out.println("lost connect");
					break;
				}
				String[] tokens = request.split("::");
				
			}
		}catch(Exception e) {
			
		}
	}
}
