package business.objects;

import java.util.ArrayList;
import java.util.List;

public class CheckoutRecord {
	
	public CheckoutRecord()
	{
		this.entrylist = new ArrayList<CheckoutRecordEntry>();
	}
	

	private List<CheckoutRecordEntry> entrylist;

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
	

}
