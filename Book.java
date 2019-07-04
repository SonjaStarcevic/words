package ord;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Book {
	private final String url;
	private Dictionary lex;
	private HashMap<String, Integer> ex_lex = new HashMap<String, Integer>();
	private TreeSet<String> new_words = new TreeSet<String>();
	private TreeSet<String> all_book_words = new TreeSet<String>();

	public Book(String url, Dictionary lex) {
		this.url = url;
		this.lex = lex;
	}

	public void extract() {
		// extracts words from the file and counts their appearances
		// divides them into two separate hashmaps according to presence in the dictionary
		// records all words in a separate hashmap also
		try (BufferedReader br = new BufferedReader(new FileReader(url))) {
			String row;
			while ((row = br.readLine()) != null) {
				row = convert(row.toLowerCase());
				String[] str = row.split(" ");
				for (String s : str) {
					if (!(s.equals(" ") || s.equals(""))) {
						if (!lex.lexis.containsKey(s)) {
							new_words.add(s);
						} else {
							if (!ex_lex.containsKey(s)) ex_lex.put(s, 1);
							else {
								int count = ex_lex.get(s);
								ex_lex.put(s, ++count);
							}
						}
						all_book_words.add(s);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String convert(String s) {
		// converts all non-letter characters to blank spaces
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c < 'a' || c > 'z') {
				if (c == '-' && s.charAt(i - 1) != ' ' && s.charAt(i + 1) != ' ') continue;
				if (c == '\'') continue;
				else s = s.replace(c, ' '); //TODO dodati uslove za crticu
			}
		}
		return s;
	}

	public void record_additions() throws SQLException {
		// records the words that are not in the dictionary into the database by creating a new table
		Statement stm = lex.con.createStatement();
		StringBuilder new_word = new StringBuilder();
		for (String s : new_words) new_word.append("(\"").append(s).append("\"),");
		new_word.deleteCharAt(new_word.length() - 1);
		stm.executeUpdate("DROP TABLE IF EXISTS additional;"
				+ "CREATE TABLE additional (new_words VARCHAR(30));");
		stm.executeUpdate("INSERT INTO additional VALUES " + new_word.toString());
		stm.close();
	}
	
	public void print_all_words() {
		// prints out all words in a separate file sorted asc
		try (FileWriter fw = new FileWriter("all_words.txt")){
			for (String s : all_book_words) fw.write(s + "\n");
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void get_20_frequent() {
		// sorts all words found in the dictionary by count desc and prints it on the console
		Map<String, Integer> sorted = ex_lex.entrySet().stream() 
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		int full = 0;
		for (String s : sorted.keySet()) {
			if (full == 20) break;
			System.out.println((++full + ". " +s + ": " + ex_lex.get(s)));
		}
	}
}
