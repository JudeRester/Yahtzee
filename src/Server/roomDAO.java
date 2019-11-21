package Server;

import java.util.ArrayList;

import common.GameRoom;
import common.User;

public class roomDAO {
	private ArrayList<GameRoom> roomList = new ArrayList<>();
	private int seq = 0;
	private roomDAO() {
	}

	private static class Room {
		public static final roomDAO instance = new roomDAO();
	}

	public static roomDAO getInstance() {
		return Room.instance;
	}

	public GameRoom createRoom(User user, String rName) {
		GameRoom gr = new GameRoom(user,seq++,rName);
		roomList.add(gr);
		return gr;
	}
	public ArrayList<GameRoom> getRoomlist() {
		return roomList;
	}
	public int enterRoom(User user, int seq) {
		for (GameRoom gr : roomList) {
			if (gr.getSeq() == seq) {
				if (gr.getUsers().size() > 1) { //방 꽉 참
					return 1;
				} else { // 입장 성공
					gr.addUser(user);
					return 0;
				}
			}
		}
		return 2; //방이 존재하지 않음
	}
	public void deleteRoom(int seq) {
		for(GameRoom gr : roomList) {
			if(gr.getSeq() == seq) {
				roomList.remove(gr);
			}
		}
	}
}
