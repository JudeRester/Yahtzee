package Server;

import common.User;
import common.GameRoom;

public class roomDAO {
	/*
	 * Connection conn; PreparedStatement pstmt; ResultSet rs = null;
	 * 
	 * public roomDAO() throws SQLException{ conn = DataBaseUtil.getConnection(); }
	 */
	
	public GameRoom createRoom(User user1, int seq, String rName) {
		GameRoom gr = new GameRoom();
		
		gr.setUser1(user1);
		gr.setSeq(seq);
		gr.setrName(rName);
		return gr;
	}
}
