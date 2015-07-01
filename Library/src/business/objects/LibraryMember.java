package business.objects;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

public class LibraryMember extends Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7641841468211440963L;
	
	private int memberID;
	private CheckoutRecord record = new CheckoutRecord();
	
	public LibraryMember(int id, String firstname, String lastname, String phone, Address addr)
	{
		super(firstname, lastname, phone, addr);
		this.memberID = id;
	}
	
	public void checkout(Copy copy, LocalDate checkoutDate, LocalDate dueDate) {
		CheckoutRecordEntry entry = new CheckoutRecordEntry(copy, checkoutDate, dueDate);
		record.addEntry(entry);
	}
	
	public String toString() {
		return "Checkout record for library member " + memberID + ": " + super.toString() + ", "+ record;
	}
	
	public int getMemberID() {
		return memberID;
	}
	
	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}
	
}
