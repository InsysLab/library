package business.objects;

import java.io.Serializable;
import java.time.LocalDate;

public class LibraryMember extends Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7641841468211440963L;
	
	private String memberID;
	private CheckoutRecord record = new CheckoutRecord();
	
	public LibraryMember(String memberid, String firstname, String lastname, String phone, Address addr)
	{
		super(firstname, lastname, phone, addr);
		this.memberID = memberid;
	}
	
	public void checkout(Copy copy, LocalDate checkoutDate, LocalDate dueDate) {
		CheckoutRecordEntry entry = new CheckoutRecordEntry(copy, checkoutDate, dueDate);
		record.addEntry(entry);
	}
	
	public String toString() {
		return "Checkout record for library member " + memberID + ": " + super.toString() + ", "+ record;
	}
	
		
	public String getMemberID() {
		return memberID;
	}
	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}
	
}
