package business.objects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;

import business.dataaccess.*;

public class CheckoutRecordEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7867212047222496038L;
	private static final String DATE_PATTERN = "MM/dd/yyyy";
	
	private LibraryMember member;
	private LocalDate checkoutDate;
	private LocalDate dueDate;	
	private Copy copy;
	
	public CheckoutRecordEntry(Copy copy, LocalDate checkoutDate, LocalDate dueDate){ 
		this.copy = copy;
		this.checkoutDate = checkoutDate;
		this.dueDate = dueDate;
	}
	
	public void setMember(LibraryMember member){
		this.member = member;
	}

	public LibraryMember getMember(){
		return this.member;
	}
	
	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}
	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}	
	
	public Copy getCopy() {
		return copy;
	}
	public void setCopy(Copy copy) {
		this.copy = copy;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		return "[" + "checkoutdate:" + 
	        checkoutDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN)) +
	        ", dueDate: " + dueDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN)) +
	        ", publication: " + copy + "]";
		
	}
}
