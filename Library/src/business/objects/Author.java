package business.objects;

import java.io.Serializable;

public class Author extends Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6561578507888988520L;
	
	private String credentials;
	
	public Author(String firstname, String lastname, String phone, String credential, Address addr)
	{
		super(firstname, lastname,phone, addr);
		this.credentials = credential;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

}
