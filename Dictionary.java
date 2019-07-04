package ord;

import java.sql.*;
import java.util.*;

public class Dictionary {
	protected Connection con;
	protected HashMap<String, String> lexis = new HashMap<String, String>();
	private final String dburl;

	public Dictionary(String url) throws SQLException {
		dburl = "jdbc:sqlite:" + url;
		con = DriverManager.getConnection(dburl);
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery("SELECT word, wordtype FROM entries GROUP BY word");
		while(rs.next()) {
			lexis.put(rs.getString("word").toLowerCase(), rs.getString("wordtype"));
		}
		stm.close();
	}

	public void disconnectQuietly() {
		try {
			if(con!=null && !con.isClosed()) con.close();
		} catch (SQLException e) {
			System.err.println("Nemoguce zatvoriti vezu!");
			e.printStackTrace();
		}
	}
}
