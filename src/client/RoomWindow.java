package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import common.User;

public class RoomWindow extends JFrame {
	private JPanel contentPane;
	private JLabel me, me2, opponent, opponent2;
	private JLabel[] opp = new JLabel[13];
	private JButton roll;
	private JButton[] buttons = new JButton[13];
	private int[] checked = new int[13];
	private int[] hold = new int[5];
	private JButton[] dices = new JButton[5];
	private ImageIcon[] diceImages = new ImageIcon[6];
	private ImageIcon[] holdeddice = new ImageIcon[6];
	private User user;
	private Socket socket;
	private int x = 87;
	private int y = 32;
	private int dicex = 20;
	private int dicey = 645;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int[][] myroll = new int[2][5];
	private int[] newroll = new int[5];
	private int rollcount = 0;
	private Font font = new Font("SansSerif",Font.BOLD,16);
	public RoomWindow(User user, Socket socket) {
		
		this.user = user;
		this.socket = socket;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		setTitle("Yahtzee");
		setBounds(0, 0, 455, 845);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setBackground(new Color(19, 194, 60));
		contentPane.setLayout(null);

		me = new JLabel(user.getNickname());
		opponent = new JLabel("waiting...");
		me2 = new JLabel(user.getNickname());
		opponent2 = new JLabel("waiting...");
		me.setBounds(85, 5, 50, 12);
		opponent.setBounds(155, 5, 50, 12);
		me2.setBounds(315, 5, 50, 12);
		opponent2.setBounds(385, 5, 50, 12);

		JLabel back = new JLabel(new ImageIcon("img/gameboard_1.png"));
		back.setBounds(0, 25, 455, 711);

		// 점수버튼 액션
		ActionListener b_action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < buttons.length; i++) {
					if (e.getSource() == buttons[i]) {
						buttons[i].setText(Integer.toString(calc(myroll,i)));
					}
				}
				try {
					oos.writeObject("turnEnd::");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};

		// 내 점수 버튼 및 상대 점수 라벨
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			buttons[i].setFont(font);
			buttons[i].setOpaque(false);
			opp[i] = new JLabel();
			buttons[i].setBounds(x, y, 50, 50);
			opp[i].setBounds(x + 70, y, 50, 50);
			if (i == 5) {
				y = 32;
				x = 315;
			} else {
				y += 81;
			}
			buttons[i].addActionListener(b_action);
			add(buttons[i]);
			add(opp[i]);
		}

		// 주사위
		for (int i = 0; i < diceImages.length; i++) {
			diceImages[i] = new ImageIcon("img/dice" + (i + 1) + "_1.png");
		}
		for (int i = 0; i < holdeddice.length; i++) {
			holdeddice[i] = new ImageIcon("img/dice" + (i + 1) + "_2.png");
		}
		ActionListener d_action= new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				for (int i = 0; i < dices.length; i++) {
					
					if (e.getSource() == dices[i]) {
						if(myroll[1][i]==0) {
							myroll[1][i]=1;
						}else {
							myroll[1][i]=0;
						}
						if(myroll[1][i]==0)
							dices[i].setIcon(diceImages[myroll[0][i]-1]);
						else
							dices[i].setIcon(holdeddice[myroll[0][i]-1]);
						
					}
				}
			}
		}; 
		for (int i = 0; i < dices.length; i++) {
			dices[i] = new JButton(diceImages[i]);
			dices[i].setBounds(dicex, dicey, 70, 70);
			dicex += 87;
			add(dices[i]);
			dices[i].addActionListener(d_action);
		}

		roll = new JButton("주사위 굴리기");
		roll.setBounds(0, 735, 450, 80);
		roll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button_enable();
				if (rollcount++ >= 2) {
					roll.setEnabled(false);
				}
				try {
					oos.writeObject("roll::");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		add(me);
		add(opponent);
		add(me2);
		add(opponent2);
		add(back);
		add(roll);
		setVisible(true);
		button_disable();
		// 스레드
		new Thread() {
			public void run() {
				try {
					String request = "isStart::";
					oos.writeObject(request);
					while (true) {
						String response = (String) ois.readObject();
						String[] tokens = response.split("::");
						if ("start".contentEquals(tokens[0])) {
							System.out.println("start!");
							
							if (user.getNickname().contentEquals(tokens[1])) {
								opponent.setText(tokens[2]);
								opponent2.setText(tokens[2]);
							} else {
								opponent.setText(tokens[1]);
								opponent2.setText(tokens[1]);
							}
						} else if ("rolled".contentEquals(tokens[0])) {
							for (int i = 0; i <myroll[1].length; i++) {
								if (myroll[1][i] == 0) {
									myroll[0][i] = Integer.parseInt(tokens[i+1])+1;
									dices[i].setIcon(diceImages[myroll[0][i]-1]);
								}
							}
						} else if("isYourTurn".contentEquals(tokens[0])) {
							if(user.getNickname().contentEquals(tokens[1])) {
								roll.setEnabled(true);
								for(int i=0;i<myroll[1].length;i++) {
									myroll[1][i]=0;
								}
								rollcount=0;
								
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public int calc(int[][] roll, int type) {
		int x = 0;
		int[] temp = new int[7];
		int sum = 0;
		switch (type) {
		case 0:
			for (int r : roll[0]) {
				if (r == 1) {
					x += 1;
				}
			}
			break;
		case 1:
			for (int r : roll[0]) {
				if (r == 2) {
					x += 2;
				}
			}
			break;
		case 2:
			for (int r : roll[0]) {
				if (r == 3) {
					x += 3;
				}
			}
			break;
		case 3:
			for (int r : roll[0]) {
				if (r == 4) {
					x += 4;
				}
			}
			break;
		case 4:
			for (int r : roll[0]) {
				if (r == 5) {
					x += 5;
				}
			}
			break;
		case 5:
			for (int r : roll[0]) {
				if (r == 6) {
					x += 6;
				}
			}
			break;
		case 6:
			for (int i=0;i<roll[0].length;i++) {
				temp[roll[0][i]]++;
				sum += roll[0][i];
			}
			for (int i=1;i<temp.length;i++) {
				if (temp[i] > 2) {
					x = sum;
				}
			}
			break;
		case 7:
			for (int i=0;i<roll[0].length;i++) {
				temp[roll[0][i]]++;
				sum += roll[0][i];
			}
			for (int i=1;i<temp.length;i++) {
				if (temp[i] > 3) {
					x = sum;
				}
			}
			break;
		case 8:
			boolean three = false;
			boolean two = false;
			for (int i=0;i<roll[0].length;i++) {
				temp[roll[0][i]]++;
			}
			for (int t : temp) {
				if (t == 3)
					three = true;
				if (t == 2)
					two = true;
			}
			if (three && two)
				x = 25;
			break;
		case 9:
			for (int i=0;i< roll[0].length;i++) {
				temp[roll[0][i]]++;
			}
			for (int i=0;i<temp.length;i++) {
				if (temp[i] > 0) {
					sum++;
					if (sum > 3) {
						x = 30;
					}
				} else {
					sum = 0;
				}
			}
			break;
		case 10:
			for (int i=0;i< roll[0].length;i++) {
				temp[roll[0][i]]++;
			}
			for (int i=0;i<temp.length;i++) {
				if (temp[i] > 0) {
					sum++;
					if (sum > 4) {
						x = 40;
					}
				} else {
					sum = 0;
				}
			}
			break;
		case 11:
			for (int i=0;i< roll[0].length;i++) {
				temp[roll[0][i]]++;
			}
			for (int t : temp) {
				if (t > 4) {
					x = 50;
				}
			}
			break;
		case 12:
			for (int r : roll[0]) {
				x += r;
			}
			break;
		}
		checked[type]++;
		button_disable();
		return x;
	}
	public void button_enable() {
		for(int i=0;i<buttons.length;i++) {
			if(checked[i]==0) {
				buttons[i].setEnabled(true);
			}
		}
		for(int i=0;i<dices.length;i++) {
			dices[i].setEnabled(true);
		}
		roll.setEnabled(true);
	}
	public void button_disable() {
		for(int i=0;i<buttons.length;i++) {
			buttons[i].setEnabled(false);
		}
		for(int i=0;i<dices.length;i++) {
			dices[i].setEnabled(false);
		}
		roll.setEnabled(false);
	}
}
