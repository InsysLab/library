package business.objects;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public abstract class Publication implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2747962880993378844L;
	
	private String title;
	private List<Copy> copyList;
	
	public Publication(String title)
	{
		this.title = title;
		this.copyList = new ArrayList<Copy>();
	}
		
	public abstract int getMaxCheckoutLength();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Copy> getCopyList() {
		return copyList;
	}

	public void setCopyList(List<Copy> copyList) {
		this.copyList = copyList;
	}
	
	public void addCopy(Copy copy) {
		this.copyList.add(copy);
	}

	public Copy getAvailableCopy(){
		List<Copy> copyList = getCopyList();
		
		for(Copy c : copyList){
			if( c.isAvailable() ){
				return c;
			}
		}
		
		return null;
	}
	
	public abstract String getNumber();
}
