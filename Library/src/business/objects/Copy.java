package business.objects;

public class Copy {
	private String copyNo;
	private Publication publication;
	
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
	

}
