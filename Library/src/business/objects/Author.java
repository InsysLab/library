package business.objects;

import java.io.Serializable;

public class Author extends Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6561578507888988520L;
	
	private int AuthorID;
	private String bio;
	
	public Author(String firstname, String lastname, String phone, String bio, Address addr)
	{
		super(firstname, lastname,phone, addr);
		this.bio = bio;
	}

	public String getBio() {
		return bio;
	}
	
	public int getAuthorID() {
		return AuthorID;
	}

	public void setAuthorID(int authorID) {
		AuthorID = authorID;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	

}
