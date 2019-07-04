package ord;

import java.sql.SQLException;

public class Program {

	public static void main(String[] args) {
		try {
			//tests the methods created
			String url = ""; // enter db filepath here
			Dictionary d = new Dictionary(url);
			url = ""; // enter book filepath here
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
