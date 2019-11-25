package client;

import java.awt.Color;
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

import common.User;

public class RoomWindow extends JFrame {
	private JPanel contentPane;
	private JLabel me, me2, opponent, opponent2;
	private JLabel op_one, op_two, op_three, op_four, op_five, op_six, op_tok, op_fok, op_fullh, op_ss, op_ls,
			op_yahtzee, op_any;
	private JLabel[] opp = { op_one, op_two, op_three, op_four, op_five, op_six, op_tok, op_fok, op_fullh, op_ss, op_ls,
			op_yahtzee, op_any };
	private JButton one, two, three, four, five, six, tok, fok, fullh, ss, ls, yahtzee, any, roll;
	private JButton[] buttons = { one, two, three, four, five, six, tok, fok, fullh, ss, ls, yahtzee, any };
	private JButton dice_1, dice_2, dice_3, dice_4, dice_5;
	private JButton[] dices = { dice_1, dice_2, dice_3, dice_4, dice_5 };
	private ImageIcon dice1, dice2, dice3, dice4, dice5, dice6;
	private ImageIcon[] diceImages = { dice1, dice2, dice3, dice4, dice5, dice6 };
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
	private int rollcount=0;
	
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
		
		//점수버튼 액션
		ActionListener b_action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton clicked = (JButton)e.getSource();
				clicked.setText("0");
				clicked.setEnabled(false);
				try {
					oos.writeObject("turnEnd::");
					roll.setEnabled(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		// 내 점수 버튼 및 상대 점수 라벨
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
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
		for (int i = 0; i < dices.length; i++) {
			dices[i] = new JButton(diceImages[i]);
			dices[i].setBounds(dicex, dicey, 70, 70);
			dicex += 87;
			add(dices[i]);
		}

		roll = new JButton("주사위 굴리기");
		roll.setBounds(0, 735, 450, 80);
		roll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rollcount++;
				if(rollcount>=2) {
					roll.setEnabled(false);
				}
				try {
					oos.writeObject("roll::");
					newroll = (int[])ois.readObject();
					for(int i=0;i<newroll.length;i++) {
						if(myroll[1][i]==0) {
							myroll[0][i]=newroll[i];
							dices[i].setIcon(diceImages[myroll[0][i]]);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
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
		
//		new Thread() {
//			public void run() {
//				try {
//					String request = "";
//					String request = "isStart::";
//					oos.writeObject(request);
//					String oppo_nick = (String)ois.readObject();
//					opponent.setText(oppo_nick);
//					opponent2.setText(oppo_nick);
//					while(true) {
//						request = "isMyturn::";
//						oos.writeObject(request);
//						String response = (String) ois.readObject();
//						String[] tokens = request.split("::");
//						if("turn".contentEquals(tokens[0])) {
//							
//						}
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch(ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
	}

}
