package unitTest;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatWindow {
	private String name;
	private Frame frame;
	private Panel panel;
	private Button buttonSend;
	private TextField tf;
	private TextArea ta;

	private Socket socket;

	public ChatWindow(String name, Socket socket) {
		this.name = name;
		frame = new Frame(name);
		panel = new Panel();
		buttonSend = new Button("send");
		tf = new TextField();
		ta = new TextArea(30, 80);
		this.socket = socket;

		new ChatServerReceiveThread(socket).start();

		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				sendMessage();
			}
		});

		// Textfield
		tf.setColumns(80);
		tf.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

		// panel
		panel.setBackground(Color.LIGHT_GRAY);
		panel.add(tf);
		panel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, panel);

		// TextArea
		ta.setEditable(false);
		frame.add(BorderLayout.CENTER, ta);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				PrintWriter pw;
				try {
					pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
							true);
					String request = "quit:\r\n";
					pw.println(request);
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.setVisible(true);
		frame.pack();
	}

	private void sendMessage() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
			String message = tf.getText();
			String request = "message:" + message + "\r\n";
			pw.println(request);

			tf.setText("");
			tf.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ChatServerReceiveThread extends Thread {
		Socket socket = null;

		ChatServerReceiveThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				while (true) {
					String msg = br.readLine();
					ta.append(msg);
					ta.append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
