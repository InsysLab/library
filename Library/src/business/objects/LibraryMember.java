package business.objects;

import java.util.ArrayList;
import java.util.List;

public class LibraryMember extends Person {
	
	public LibraryMember()
	{
		this.copyList = new ArrayList<Copy>();
	}
	
	private String memberID;
	public String getMemberID() {
		return memberID;
	}
	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}
	public List<Copy> getCopyList() {
		return copyList;
	}
	public void setCopyList(List<Copy> copyList) {
		this.copyList = copyList;
	}
	
	public void addCopy(Copy copy)
	{
		this.copyList.add(copy);
	}
	private List<Copy> copyList;
}
