package business.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookList implements Serializable {
	
	private static BookList instance = null;
	   protected BookList() {
	      // Exists only to defeat instantiation.
	   }
	   public static BookList getInstance() {
	      if(instance == null) {
	         instance = new BookList();
	      }
	      return instance;
	   }
	   
	private List<Book> books = new ArrayList<>();

	public List getBooks() {
		return books;
	}
	
	public void addBook(Book book) {
		books.add(book);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return books.toString();
	}
}
