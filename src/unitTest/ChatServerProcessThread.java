package unitTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread {
	private String nickname = null;
	private Socket socket = null;
	List<PrintWriter> list = null;

	public ChatServerProcessThread(Socket socket, List<PrintWriter> list) {
		this.socket = socket;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
			while (true) {
				String request = br.readLine();
				if (request == null) {
					System.out.println("lost connect");
					doQuit(pw);
					break;
				}
				System.out.println(request);
				String[] tokens = request.split(":");
				if ("join".contentEquals(tokens[0]))
					doJoin(tokens[1], pw);
				else if ("message".equals(tokens[0]))
					doMessage(tokens[1]);
				else if ("quit".equals(tokens[0]))
					doQuit(pw);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doMessage(String data) {
		broadcast(this.nickname + ":" + data);
	}

	private void removeWriter(PrintWriter pw) {
		synchronized (list) {
			list.remove(pw);
		}
	}

	private void broadcast(String data) {
		synchronized (list) {
			for (PrintWriter w : list) {
				w.println(data);
				w.flush();
			}
		}
	}

	private void addWriter(PrintWriter writer) {
		synchronized (list) {
			list.add(writer);
		}
	}

	private void doJoin(String nickname, PrintWriter writer) {
		this.nickname = nickname;

		String data = nickname + "님이 입장하였습니다.";
		broadcast(data);

		// writer pool에 저장
		addWriter(writer);
	}

	private void doQuit(PrintWriter pw) {
		removeWriter(pw);
		String data = this.nickname + " is gone";
		broadcast(data);
	}
}
