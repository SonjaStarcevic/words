package ord;

import java.sql.SQLException;

public class Program {

	public static void main(String[] args) {
		/*
		 * C:\\Users\\win7\\Desktop\\SonjaS\\Dictionary.db
		 * C:\\Users\\win7\\Desktop\\SonjaS\\knjiga
		 */

		try {
			String url = "C:\\Users\\Sonja\\Desktop\\Dictionary.db";
			Dictionary d = new Dictionary(url);
			url = "C:\\Users\\Sonja\\Desktop\\knjiga";
			Book b = new Book(url, d);
			b.extract();
			b.record_additions();
			b.print_all_words();
			b.get_20_frequent();
			d.disconnectQuietly();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
