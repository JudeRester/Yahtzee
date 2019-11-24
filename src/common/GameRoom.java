package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameRoom implements Serializable{
	private int seq;
	private String rName;
	private ArrayList<User> users= new ArrayList<>();
	private boolean isStart = false;
	private Map<String, int[]> m = Collections.synchronizedMap(new HashMap<String, int[]>());


	
	public GameRoom(User user, int seq, String rName) {
		users.add(user);
		this.seq=seq;
		this.rName=rName;
	}
	public void gameStart() {
		m.put(users.get(1).getId(), new int[13]);
		m.put(users.get(2).getId(), new int[13]);
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getrName() {
		return rName;
	}
	public void setrName(String rName) {
		this.rName = rName;
	}
	public ArrayList<User> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	public void addUser(User user) {
		this.users.add(user);
	}
	public void removeUser(User user) {
		this.users.remove(user);
	}
	public boolean isStart() {
		return isStart;
	}
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
}
