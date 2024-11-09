package start.web;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Storage {

	static String source = "jdbc:mysql://127.0.0.1/sample" +
					"?user=me&password=password";
	
	public static User 
	getUserByEmail(String email) {
		var sql = "select * from users where email = ?";
		User u = null;
		try {
			var cn = DriverManager.getConnection(source);
			var ps = cn.prepareStatement(sql);
			ps.setString(1, email);
			var rs = ps.executeQuery();
			if (rs.next()) {
				u = new User();
				u.email = rs.getString("email");
			}
			rs.close(); ps.close(); cn.close();
		} catch (Exception e) { }
		return u;
	}
	
	public static User
	checkPassword(String email, String password) {
		var sql =   " select * from users where email = ? " +
					" and password = sha2(?, 512)         ";
		User u = null;
		try {
			var cn = DriverManager.getConnection(source);
			var ps = cn.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, password);
			var rs = ps.executeQuery();
			if (rs.next()) {
				u = new User();
				u.email = rs.getString("email");
			}
			rs.close(); ps.close(); cn.close();
		} catch (Exception e) { }
		return u;
	}
	
	public static boolean
	createAccount(String email, String password, 
			String firstName, String lastName) {
		boolean result = false;
		var sql =   " insert into users                      " +
					" (email,password,first_name,last_name)  " +
					" values(?, sha2(?, 512), ?, ?)          ";
		try {
			var cn = DriverManager.getConnection(source);
			var ps = cn.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, password);
			ps.setString(3, firstName);
			ps.setString(4, lastName);
			result = ps.execute();
			ps.close(); cn.close();
		} catch (Exception e) { }
		return result;
	}
	
	public static boolean
	resetPassword(String email, String password) {
		boolean result = false;
		var sql =   " update users set password = sha2(?, 512)  " +
					" where email = ?                           ";
		try {
			var cn = DriverManager.getConnection(source);
			var ps = cn.prepareStatement(sql);
			ps.setString(1, password);
			ps.setString(2, email);
			result = ps.execute();    // or ps.executeUpdate()
			ps.close(); cn.close();
		} catch (Exception e) { }
		return result;
	}
	
	public static int
	saveContactMessage(String topic, String detail, String email) {
		var sql   = " insert into messages(topic,detail,email,time) " +
					" values(?, ?, ?, utc_timestamp())              ";
		ArrayList<Integer> list = new ArrayList<>();
		
		try {
			var cn = DriverManager.getConnection(source);
			var ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, topic);
			ps.setString(2, detail);
			ps.setString(3, email);
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			while (rs.next()) {
				list.add( (int)rs.getLong(1) );
			}
			rs.close(); ps.close(); cn.close();
		} catch (Exception e) { }
		
		if (list.size() == 0) return 0;
		Integer first = list.get(0);
		if (first == null) return 0;
		return first;
	}
}
