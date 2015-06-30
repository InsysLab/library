package business.objects;

public class Author extends Person {
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
