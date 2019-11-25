package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import common.User;

public class LoginThread extends Thread {
	private Socket socket=null;
	MemberDAO dao;
	User user;
	public LoginThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//			BufferedReader br;
//			PrintWriter pw;
			while (true) {
				
//				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
//				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
				String request = (String)ois.readObject();
				if (request == null) {
					System.out.println("lost connect");
					break;
				}
				String[] tokens = request.split("::");
				if ("login".contentEquals(tokens[0])) {
					dao = new MemberDAO();
					int result = dao.Login(tokens[1], tokens[2]);
					if (result == 1) {
						user = dao.getUser(tokens[1]);
						oos.writeObject(result);
						oos.writeObject(user);
						new RoomThread(user,socket).start();
						break;
					} else {
						oos.writeObject(result);
					}

				} else if ("dupC".contentEquals(tokens[0])) {
					dao = new MemberDAO();
					if (dao.dupCheck(tokens[1]) == 1) {
						oos.writeObject(1);
					} else {
						oos.writeObject(0);
					}
				} else if ("join".contentEquals(tokens[0])) {
					dao = new MemberDAO();
					User user = new User(tokens[1],tokens[2],tokens[3],tokens[4],tokens[5]);
					oos.writeObject(dao.join(user));
				} else if("f_id".contentEquals(tokens[0])) {
					dao = new MemberDAO();
					oos.writeObject(dao.find_id(tokens[1], tokens[2]));
				} else if("f_pass".contentEquals(tokens[0])) {
					dao = new MemberDAO();
					oos.writeObject(dao.find_pass(tokens[1], tokens[2]));
				} else if("c_pass".contentEquals(tokens[0])) {
					dao = new MemberDAO();
					dao.change_pass(tokens[1], tokens[2]);
				}
			}
		} catch(SocketException e) {
			System.out.println("[LoginThread]lost connection");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
