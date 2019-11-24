package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import common.GameRoom;
import common.User;

public class GameThread extends Thread {
	private Socket socket = null;
	private RoomDAO dao;
	private User user;
	private GameRoom gr;
	public GameThread (Socket socket, User user){
		this.socket = socket;
		this.user = user;
		gr=user.getGr();
	}
	
	@Override
	public void run() {
		try {
			BufferedReader br;
			PrintWriter pw;
			while(true) {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),true);
				String request = br.readLine();
				String[] tokens = request.split("::");
				
			}
		}catch(SocketException e) {
			System.out.println("lost connection");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
