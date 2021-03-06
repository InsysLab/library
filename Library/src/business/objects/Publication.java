package business.objects;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public abstract class Publication implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2747962880993378844L;
	
	private int id;
	private String title;
	private List<Copy> copyList;
	
	public Publication(String title)
	{
		this.title = title;
		this.copyList = new ArrayList<Copy>();
	}
		
	public abstract int getMaxCheckoutLength();
	public abstract String getNumber();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
	
	public void addCopy() {
		int num = copyList.size() + 1;
		Copy c = new Copy(Integer.toString(num), this);
		c.setAvailable(true);
		this.copyList.add(c);
	}

	public Copy getAvailableCopy(){
		if( copyList != null ){
			for(Copy c : copyList){
				if( c.isAvailable() ){
					return c;
				}
			}
		}
		
		return null;
	}
	
	public void checkoutACopy(int numCopies){
		int i = 0;
		for(Copy c : copyList){
			if(c.isAvailable()){
				c.setAvailable(false);
				i++;
			}
			
			if( i == numCopies ){
				break;
			}
		}		
	}
	
	public void returnACopy(){
		for(Copy c : copyList){
			if( ! c.isAvailable() ){
				c.setAvailable(true);
				break;
			}
		}		
	}
	
	
}
