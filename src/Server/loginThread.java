package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import common.User;

public class loginThread extends Thread {
	private Socket socket=null;
	memberDAO dao;
	User user;
	public loginThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader br;
			PrintWriter pw;
			while (true) {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
				String request = br.readLine();
				if (request == null) {
					System.out.println("lost connect");
					break;
				}
				String[] tokens = request.split("::");
				if ("login".contentEquals(tokens[0])) {
					dao = new memberDAO();
					int result = dao.Login(tokens[1], tokens[2]);
					if (result == 1) {
						user = dao.getUser(tokens[1]);
						pw.println(result);
						System.out.println(result);
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(user);
						new RoomThread(socket, user).start();
						break;
					} else {
						pw.println(result);
					}

				} else if ("dupC".contentEquals(tokens[0])) {
					dao = new memberDAO();
					if (dao.dupCheck(tokens[1]) == 1) {
						pw.println(1);
					} else {
						pw.println(0);
					}
				} else if ("join".contentEquals(tokens[0])) {
					dao = new memberDAO();
					User user = new User(tokens[1],tokens[2],tokens[3],tokens[4],tokens[5]);
					pw.println(dao.join(user));
				} else if("f_id".contentEquals(tokens[0])) {
					dao = new memberDAO();
					pw.println(dao.find_id(tokens[1], tokens[2]));
				} else if("f_pass".contentEquals(tokens[0])) {
					dao = new memberDAO();
					pw.println(dao.find_pass(tokens[1], tokens[2]));
				} else if("c_pass".contentEquals(tokens[0])) {
					dao = new memberDAO();
					dao.change_pass(tokens[1], tokens[2]);
				}
			}
		} catch(SocketException e) {
			System.out.println("lost connection");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
