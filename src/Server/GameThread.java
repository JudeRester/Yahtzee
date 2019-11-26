package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

import common.GameRoom;
import common.User;

public class GameThread extends Thread {
	private Socket socket = null;
	private RoomDAO dao;
	private MemberDAO mDAO;
	private User user;
	private GameRoom gr;
	private int[] rolled = new int[5];
	private String[] users = new String[2];

	public GameThread(Socket socket, User user) {
		this.socket = socket;
		this.user = user;
		gr = user.getGr();
		dao = RoomDAO.getInstance();
		try {
			mDAO = new MemberDAO();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			for (GameRoom r : dao.getRoomlist()) {
				if (r.getSeq() == gr.getSeq()) {
					dao.putUser(r, oos);
				}
			}
			while (true) {
				String request = (String) ois.readObject();
				String[] tokens = request.split("::");
				if ("isStart".contentEquals(tokens[0])) {
					while (true) {
						if (isStart()) {
							for (int i = 0; i < 2; i++) {
								users[i] = gr.getUsers().get(i).getNickname();
							}
//							String user1 = gr.getUsers().get(0).getNickname();
//							String user2 = gr.getUsers().get(1).getNickname();
							dao.broadcast(gr, "start::" + users[0] + "::" + users[1]);
							getTurn(-1, -1);
							break;
						}
					}
				} else if ("isMyturn".contentEquals(tokens[0])) {
					while (gr.getTurn() == gr.getUsers().indexOf(user)) {

					}
				} else if ("roll".contentEquals(tokens[0])) {
					StringBuffer response = new StringBuffer("rolled");
					for (int i = 0; i < rolled.length; i++) {
						int a = new Random().nextInt(6);
						response.append("::" + a);
					}
					dao.broadcast(gr, response.toString());
				} else if ("turnEnd".contentEquals(tokens[0])) {
					gr.setTurn(gr.getTurn() + 1);
					int type = Integer.parseInt(tokens[1]);
					int score = Integer.parseInt(tokens[2]);
					setScore(type, score);
					getTurn(type, score);
				} else if ("endGame".contentEquals(tokens[0])) {
					dao.endGame(gr, user);
					user = mDAO.getUser(user.getId());
					oos.writeObject(user);
					break;
				}
			}
		} catch (SocketException e) {
			System.out.println("[GameThread]lost connection");
			dao.getRoomlist().get(gr.getSeq()).removeUser(user);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean isStart() {
		for (GameRoom r : dao.getRoomlist()) {
			if (r.getSeq() == gr.getSeq()) {
				gr = r;
			}
		}
		return gr.isStart();
	}

	public void getTurn(int type, int score) {
		int turn = gr.getTurn();
		int t_user = turn % 2;
		int totalscore;
		String response = "";
		totalscore = gr.getUserScore((t_user+1)%2);
		if (turn == 26) {
			response = "gameSet::" + users[t_user] + "::" + type + "::" + score + "::" + totalscore;
			dao.broadcast(gr, response);
		} else {
			response = "isYourTurn::" + users[t_user] + "::" + type + "::" + score + "::" + totalscore;
			dao.broadcast(gr, response);
		}
	}

	public void setScore(int type, int score) {
		Map<String, int[]> pre = gr.getScore();
		pre.get(user.getId())[type] = score;
		gr.setScore(pre);
	}
}
