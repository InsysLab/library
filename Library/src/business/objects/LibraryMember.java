package business.objects;

import java.util.ArrayList;
import java.util.List;

public class LibraryMember extends Person {
	private String memberID;
	private List<Copy> copyList;
	
	public LibraryMember(String memberid, String firstname, String lastname, String phone, Address addr)
	{
		super(firstname, lastname, phone, addr);
		this.memberID = memberid;
		this.copyList = new ArrayList<Copy>();
	}
		
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
	
}
