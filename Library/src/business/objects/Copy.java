package business.objects;

import java.io.Serializable;

public class Copy implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -954451322069238619L;
	
	private String copyNo;
	private Publication publication;
	private boolean isAvailable;
	
	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Copy(String copyno, Publication pub)
	{
		this.copyNo = copyno;
		this.publication = pub;
	}	

	public String getCopyNo() {
		return copyNo;
	}

	public void setCopyNo(String copyNo) {
		this.copyNo = copyNo;
	}

	public Publication getPublication() {
		return publication;
	}

	public void setPublication(Publication publication) {
		this.publication = publication;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return publication.toString();
	}
}
