package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import common.GameRoom;
import common.User;

public class RoomThread extends Thread {
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
			roomDAO dao = roomDAO.getInstance();
			while (true) {
				System.out.println("listen...");
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
				String request = br.readLine();

				String[] tokens = request.split("::");
				if ("getlist".contentEquals(tokens[0])) {
					ArrayList<GameRoom> rl = dao.getRoomlist();
					System.out.println(request);
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(rl);
					System.out.println("loading room list");
				} else if ("create".contentEquals(tokens[0])) {
					user.setGr(dao.createRoom(user, tokens[1])); 
					System.out.println("room has been created");
					System.out.println("number of rooms : " + dao.getRoomlist().size());
					break;
				} else if ("enter".contentEquals(tokens[0])) {
					int result = dao.enterRoom(user, Integer.parseInt(tokens[1]));
					if (result == 0) {
						for (GameRoom gr : dao.getRoomlist()) {
							if (gr.getSeq() == Integer.parseInt(tokens[1])) {
								user.setGr(gr);
							}
							pw.println(result);
							break;
						}
					}
					pw.println(result);

				}
			}
		} catch (SocketException e) {
			System.out.println("lost connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
