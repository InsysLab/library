package business.objects;

public class Periodical extends Publication{
	
	public Periodical(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	private String issueNo;
	private int maxcheckoutlength;
	
	public String getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	public int getMaxcheckoutlength() {
		return maxcheckoutlength;
	}
	public void setMaxcheckoutlength(int maxcheckoutlength) {
		this.maxcheckoutlength = maxcheckoutlength;
	}
	
	@Override
	int getMaxCheckoutLength() {
		// TODO Auto-generated method stub
		return maxcheckoutlength;
	}

}
