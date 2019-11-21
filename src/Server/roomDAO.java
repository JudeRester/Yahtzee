package Server;

import java.util.ArrayList;

import common.GameRoom;
import common.User;

public class roomDAO {
	private ArrayList<GameRoom> roomList = new ArrayList<>();

	private roomDAO() {
	}

	private static class Room {
		public static final roomDAO instance = new roomDAO();
	}

	public static roomDAO getInstance() {
		return Room.instance;
	}

	public void createRoom(User user, int seq, String rName) {
		GameRoom gr = new GameRoom(user,seq,rName);
		roomList.add(gr);
	}
	public ArrayList<GameRoom> getRoomlist() {
		return roomList;
	}
	public int enterRoom(User user, int seq) {
		for (GameRoom gr : roomList) {
			if (gr.getSeq() == seq) {
				if (gr.getUsers().size() > 1) {
					return 1;
				} else {
					gr.addUser(user);
					return 0;
				}
			}
		}
		return 2;
	}
}
