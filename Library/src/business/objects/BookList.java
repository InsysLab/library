package business.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookList implements Serializable {
	private List books = new ArrayList<Book>();

	public List getBooks() {
		return books;
	}
	
	public void addBook(Book book) {
		books.add(book);
	}
}
