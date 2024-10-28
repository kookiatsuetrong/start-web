import java.sql.DriverManager;

class Storage {

	static String source = "jdbc:mysql://127.0.0.1/sample" +
					"?user=me&password=password";
	
	static User getUserByEmail(String email) {
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
	
	static User checkPassword(String email, String password) {
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
}
