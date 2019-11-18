package common;

import java.util.ArrayList;

public class GameRoom {
	private int seq;
	private String rName;
	private int curr_user;
	private ArrayList<User> users= new ArrayList<>();;
	
	
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
	public int getCurr_user() {
		return curr_user;
	}
	public void setCurr_user(int curr_user) {
		this.curr_user = curr_user;
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
}
