package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

import common.GameRoom;
import common.User;

public class GameThread extends Thread {
	private Socket socket = null;
	private RoomDAO dao;
	private User user;
	private GameRoom gr;
	private int[] rolled= new int[5];
	public GameThread(Socket socket, User user) {
		this.socket = socket;
		this.user = user;
		gr = user.getGr();
		dao = RoomDAO.getInstance();
	}

	@Override
	public void run() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			while (true) {
				String request = (String) ois.readObject();
				String[] tokens = request.split("::");
				System.out.println(request);
				if ("isStart".contentEquals(tokens[0])) {
					boolean a =true;
					while (a) {
						if (isStart()) {
							gr.gameStart();
							for (User u : gr.getUsers()) {
								if (u != user) {
									oos.writeObject(u.getNickname());
									a=false;
									break;
								}
							}
						}
					}
				}else if ("isMyturn".contentEquals(tokens[0])) {
					while(gr.getTurn()==gr.getUsers().indexOf(user)) {
						
					}
				}else if("roll".contentEquals(tokens[0])) {
					for(int i=0;i<rolled.length;i++) {
						rolled[i]= new Random().nextInt(6);
					}
					oos.writeObject(rolled);
				}else if("turnEnd".contentEquals(tokens[0])) {
					int turn = gr.getTurn();
					if(turn ==0) {
						gr.setTurn(1);
					}else {
						gr.setTurn(0);
					}
				}
				oos.reset();
			}
		} catch (SocketException e) {
			System.out.println("[GameThread]lost connection");
			dao.getRoomlist().get(gr.getSeq()).removeUser(user);
			gr = dao.getRoomlist().get(gr.getSeq());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean isStart() {
		for(GameRoom r:dao.getRoomlist()) {
			if(r.getSeq() == gr.getSeq()) {
				gr=r;
			}
		}
		return gr.isStart();
	}
}
