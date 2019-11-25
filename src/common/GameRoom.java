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
	private Map<String, int[]> score = Collections.synchronizedMap(new HashMap<String, int[]>());
	private int[] roll = new int[5];
	private int turn;
	
	public GameRoom(User user, int seq, String rName) {
		users.add(user);
		this.seq=seq;
		this.rName=rName;
	}
	public void gameStart() {
		turn =0;
		score.put(users.get(0).getId(), new int[13]);
		score.put(users.get(1).getId(), new int[13]);
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
	public Map<String, int[]> getScore() {
		return score;
	}
	public void setScore(Map<String, int[]> score) {
		this.score = score;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int[] getRoll() {
		return roll;
	}
	public void setRoll(int[] roll) {
		this.roll = roll;
	}
}
