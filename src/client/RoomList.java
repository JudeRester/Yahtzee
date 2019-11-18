package client;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import Server.memberDAO;

public class RoomList extends JFrame {
	private ArrayList<String> rooms;
	private JLabel lb_title;
	private JPanel contentPane;
	private GridBagConstraints gc;
	private GridBagLayout layout;
	private memberDAO mDAO;
	private JList<String> list;
	private DefaultListModel<String> rm;
	private JButton bt_create,bt_printHashMap;
	private int roomcount;
	
	public RoomList(String id) {
		setTitle("Yahtzee 대기실");
		setBounds(0, 0, 500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);

		layout = new GridBagLayout();
		gc = new GridBagConstraints();
		rooms = new ArrayList<String>();
		bt_create = new JButton("방만들기");

		rm = new DefaultListModel<>();
		roomcount=0;
		
		lb_title = new JLabel("대기실 목록");
		/*for (int i = 0; i < 10; i++) {
			roomcount++;
			rooms.put(i, "방제목" + i);
		}
		for (int i = 0; i < rooms.size(); i++) {
			rm.addElement(rooms.get(i));
		}*/
		

		list = new JList<String>(rm);
		ScrollPane sc = new ScrollPane();
		sc.add(list);

		bt_create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rooms.add("방제목"+roomcount++);
//				rm.clear();
				rm.addElement(rooms.get(rooms.size()-1));
//				for (int i = 0; i <= rooms.size(); i++) {
//					rm.addElement(rooms.get(i));
//				}
			}
		});

		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = list.getSelectedIndex();
					rooms.remove(index);
					rm.remove(index);
					
				}
			}
		});
		bt_printHashMap = new JButton("출력");
		bt_printHashMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(String key : rooms) {
					System.out.println(key);
				}
			}
		});
		addC(bt_printHashMap,0,3,1,1,0.1);
		addC(lb_title, 0, 0, 5, 1, 0.2);
		addC(sc, 0, 1, 5, 5, 0.2);
		addC(bt_create, 2, 0, 1, 1, 0.1);

		setVisible(true);

	}

	private void addC(Component c, int x, int y, int w, int h, double wx) {
		gc.weightx = wx;
		gc.gridx = x;
		gc.gridy = y;
		gc.gridwidth = w;
		gc.gridheight = h;
		layout.setConstraints(c, gc);
		add(c);
	}
}
