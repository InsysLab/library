package business.objects;

import java.io.Serializable;

public class Periodical extends Publication implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5023957423709300489L;

	public Periodical(String title, String issueNo, int maxcheck) {
		super(title);
		this.issueNo = issueNo;
		this.maxcheckoutlength = maxcheck;
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
	
	@Override
	public String toString() {
		return "Periodical> IssueNo: " + issueNo + ", maxcheckoutlength: "
				+ maxcheckoutlength + ", Title: " + getTitle();
	}

}
