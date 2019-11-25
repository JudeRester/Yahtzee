package Server;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.User;

public class MemberDAO {
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs=null;
	
	public MemberDAO() throws SQLException{
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
		} /*
			 * finally { DataBaseUtil.close(conn,pstmt,rs); }
			 */
		return cnt;
	}
	
	//아이디 중복확인
	public int dupCheck(String id) {
		int cnt=0;
		StringBuffer sql = new StringBuffer();
		sql.append("select id from member where id =?");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
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
	
	//회원가입
	public int join(User user) {
		int cnt=0;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into member(id,passwd,name,nickname,email) values(?,?,?,?,?)");
		try {
			pstmt=conn.prepareStatement(sql.toString());
			pstmt.setString(1, user.getId());
			pstmt.setString(2, user.getPasswd());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getNickname());
			pstmt.setString(5, user.getEmail());
			
			cnt =pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DataBaseUtil.close(conn,pstmt);
		}
		return cnt;
	}
	
	//아이디 찾기
	public String find_id(String name, String email) {
		String id=null;
		StringBuffer sql = new StringBuffer();
		sql.append("select id from member where name=? and email=?");
		try {
			pstmt=conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				id=rs.getString("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseUtil.close(conn,pstmt,rs);
		}
		return id;
	}
	//비밀번호 찾기
	public int find_pass(String id, String email) {
		int cnt=0;
		StringBuffer sql = new StringBuffer();
		sql.append("select id from member where id=? and email=?");
		try {
			pstmt=conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, email);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				cnt++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return cnt;
	}
	
	//비밀번호 변경
	public int change_pass(String id, String pass) {
		int cnt=0;
		StringBuffer sql = new StringBuffer();
		sql.append("update member set passwd=? where id=?");
		try {
			pstmt=conn.prepareStatement(sql.toString());
			pstmt.setString(1, pass);
			pstmt.setString(2, id);
			cnt=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseUtil.close(conn,pstmt,rs);
		}
		return cnt;
	}
	
	//회원 정보 받아오기
	public User getUser(String id) {
		User user=null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from member where id=?");
		try {
			user = new User();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1,id);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				user.setId(rs.getString("id"));
				user.setNickname(rs.getString("nickname"));
				user.setWin(rs.getInt("win"));
				user.setLose(rs.getInt("lose"));
				user.setHigh(rs.getInt("high"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			DataBaseUtil.close(conn,pstmt,rs);
		}
		return user;
	}
}
