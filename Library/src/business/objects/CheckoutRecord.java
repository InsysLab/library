package business.objects;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class CheckoutRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3168413735714016878L;
	
	private List<CheckoutRecordEntry> entrylist= new ArrayList<>();
	private LibraryMember member;
		
	public LibraryMember getMember() {
		return member;
	}
	public void setMember(LibraryMember member) {
		this.member = member;
	}

	public List<CheckoutRecordEntry> getEntry() {
		return entrylist;
	}

	public void setEntry(List<CheckoutRecordEntry> entry) {
		this.entrylist = entry;
	}
	
	public void addEntry(CheckoutRecordEntry entry)
	{
		this.entrylist.add(entry);
	}
	
	public String toString() {
		return entrylist.toString();
	}

}
