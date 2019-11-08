package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataBaseUtil {
	private static BasicDataSource dataSource = new BasicDataSource();
	private static DataBaseUtil instance = new DataBaseUtil();
	
	private static final String driverName = "oracle.jdbc.driver.OracleDriver";
	private static final String serverIP="127.0.0.1";
	private static final String userName = "yahtzee";
	private static final String userPass = "yahtzee";
	
	private DataBaseUtil() {
		//jdbc 접속 설정
		dataSource.setDriverClassName(driverName);
		System.out.println("Driver Loaded");
		
		//db연결
		dataSource.setUrl("jdbc:oracle:thin:@"+serverIP+":1522:xe");
		dataSource.setUsername(userName);
		dataSource.setPassword(userPass);
		System.out.println("DB connected");
		
		//Connection Pool 설정
		dataSource.setInitialSize(6);
		dataSource.setMaxTotal(-1);
		
		dataSource.setMaxWaitMillis(1000);
		dataSource.setMinIdle(5);
		dataSource.setMaxIdle(-1);
	}
	public static DataBaseUtil getInstance() {
		return instance;
	}
	public static Connection getConnection() throws SQLException{
		Connection con = dataSource.getConnection();
		return con;
	}
	public static void close(PreparedStatement pstmt, ResultSet rs) {
		try {
			if(rs!=null)rs.close();
			if(pstmt!=null)pstmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		rs=null;
		pstmt=null;
	}
	public static void close(Connection conn,PreparedStatement pstmt, ResultSet rs) {
		try {
			if(rs!=null)rs.close();
			if(pstmt!=null)pstmt.close();
			if(conn!=null)conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		rs=null;
		pstmt=null;
		conn=null;
	}
	public static void close(Connection conn,PreparedStatement pstmt) {
		try {
			if(pstmt!=null)pstmt.close();
			if(conn!=null)conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		pstmt=null;
		conn=null;
	}
	public static void close(Connection conn) {
		try {
			if(conn!=null)conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		conn=null;
	}
	public static void close(PreparedStatement pstmt) {
		try {
			if(pstmt!=null)pstmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		pstmt=null;
	}
	
}
