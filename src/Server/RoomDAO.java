package Server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import common.GameRoom;
import common.User;

public class RoomDAO {
	private ArrayList<GameRoom> roomList = new ArrayList<>();
	private ArrayList<GameRoom> aRoomList = new ArrayList<>();

	public void setaRoomList(ArrayList<GameRoom> aRoomList) {
		this.aRoomList = aRoomList;
	}

	private HashMap<String, Socket> userList = new HashMap<>();
	private int seq = 0;

	private RoomDAO() {
	}

	private static class Room {
		public static final RoomDAO instance = new RoomDAO();
	}

	public static RoomDAO getInstance() {
		return Room.instance;
	}

	public GameRoom createRoom(User user, String rName, Socket socket) {
		GameRoom gr = new GameRoom(user, seq++, rName);
		roomList.add(gr);
		userList.put(user.getId(), socket);
		return gr;
	}

	public synchronized ArrayList<GameRoom> getRoomlist() {
		return roomList;
	}

	public int enterRoom(User user, int seq, Socket socket) {
		for (GameRoom gr : roomList) {
			if (gr.getSeq() == seq) {
				if (gr.getUsers().size() > 1) { // 방 꽉 참
					return 1;
				} else { // 입장 성공
					gr.addUser(user);
					userList.put(user.getId(), socket);

					return 0;
				}
			}
		}
		return 2; // 방이 존재하지 않음
	}

	public void deleteRoom(int seq) {
		for (GameRoom gr : roomList) {
			if (gr.getSeq() == seq) {
				roomList.remove(gr);
			}
		}
	}

	public synchronized ArrayList<GameRoom> getaRoomList() {
			aRoomList.clear();
			for (GameRoom r : roomList) {
				if (!r.isStart()) {
					aRoomList.add(r);
				}
			}
		return aRoomList;
	}
}
