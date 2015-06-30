package business.objects;

import java.util.ArrayList;
import java.util.List;

public class Book extends Publication {
	private String ISBN;
	private int maxcheckoutlength;
	private List<Author> authorlist;
	
	public Book(String title)
	{
		super(title);
		this.authorlist = new ArrayList<Author>();		
	}
	
	public Book(String isbn, int maxcheck, String title)
	{
		this(title);
		this.ISBN = isbn;
		this.maxcheckoutlength = maxcheck;
	}
	
	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public int getMaxcheckoutlength() {
		return maxcheckoutlength;
	}

	public void setMaxcheckoutlength(int maxcheckoutlength) {
		this.maxcheckoutlength = maxcheckoutlength;
	}

	public List<Author> getAuthorlist() {
		return authorlist;
	}

	public void setAuthorlist(List<Author> authorlist) {
		this.authorlist = authorlist;
	}

	public void addAuthor(Author author) {
		this.authorlist.add(author);
	}

	@Override
	int getMaxCheckoutLength() {
		// TODO Auto-generated method stub
		return maxcheckoutlength;
	}
	

}
