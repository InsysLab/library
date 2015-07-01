package business.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AuthorList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3995126215557618686L;
	private static AuthorList instance = null;
	   protected AuthorList() {
	      // Exists only to defeat instantiation.
	   }
	   public static AuthorList getInstance() {
	      if(instance == null) {
	         instance = new AuthorList();
	      }
	      return instance;
	   }
	   
	private List<Author> authors = new ArrayList<>();

	public List<Author> getBooks() {
		return authors;
	}
	
	public void addAuthor(Author author) {
		authors.add(author);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return authors.toString();
	}
}
