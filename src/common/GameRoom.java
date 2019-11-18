package common;

public class GameRoom {
	private int seq;
	private String rName;
	private int curr_user;
	private User user1, user2;
	
	
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
	public User getUser1() {
		return user1;
	}
	public void setUser1(User user1) {
		this.user1 = user1;
	}
	public User getUser2() {
		return user2;
	}
	public void setUser2(User user2) {
		this.user2 = user2;
	}
}
