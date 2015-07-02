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
		
	private static CheckoutRecord instance = null;

    public static CheckoutRecord getInstance() {
        if(instance == null) {
           instance = new CheckoutRecord();
        }
        return instance;
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
