package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class memberDAO {
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs=null;
	
	public memberDAO() throws SQLException{
		conn=DataBaseUtil.getConnection();
	}
	//로그인
	public int Login(String id, String pass) {
		int cnt=0;
		StringBuffer sql = new StringBuffer();
		sql.append("select id, passwd from member where id = ?");
		sql.append("and passwd=?");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, pass);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				cnt++;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DataBaseUtil.close(conn,pstmt,rs);
		}
		return cnt;
	}
}
