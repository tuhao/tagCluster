package org.conan.mymahout.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class OJDBC {
	
	private Connection conn = null;
	
	private PreparedStatement pstm = null;
	
	public OJDBC(){
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("oracle.properties");
			properties.load(fis);
			String host = (String)properties.get("host");
			String port = (String)properties.get("port");
			String service = (String)properties.get("service");
			String user = (String)properties.get("user");
			String passwd = (String)properties.get("passwd");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = String.format("jdbc:oracle:thin:@//%s:%s/%s",host,port,service);
			conn = DriverManager.getConnection(url,user,passwd);
			conn.setAutoCommit(false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class TagScore{
		String weibo_id;
		String tag;
		String score;
		public TagScore(String weibo_id,String tag,String score){
			this.weibo_id = weibo_id;
			this.tag = tag;
			this.score = score;
		}
	}
	
	public void insert(TagScore tagScore){
		String sql = String.format("insert into LIHANG.USER_TAG_SCORE (WEIBO_ID,TAG,SCORE) VALUES ('%s','%s','%s')",tagScore.weibo_id,tagScore.tag,tagScore.score);
		try {
			pstm = conn.prepareStatement(sql);
			pstm.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(pstm != null){
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void delete(){
		
	}
	
	public void commit(){
		if (conn != null){
			try {
				conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void close(){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void test() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String url = "jdbc:oracle:thin:@//10.65.162.40:1521/orcl";
		
		Connection conn = DriverManager.getConnection(url,"system","oracle");
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("select BANNER from SYS.V_$VERSION");
		while(rset.next()){
			System.out.println(rset.getString(1));
		}
		stmt.close();
		conn.close();
		System.out.println("Done.");
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		test();
//		OJDBC factory = new OJDBC();
//		TagScore tagScore = new TagScore("1","game","1.0");
//		factory.insert(tagScore);
//		factory.close();
	}
	

}
