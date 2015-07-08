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
	
	public void addCopy() {
		int num = copyList.size() + 1;
		Copy c = new Copy(Integer.toString(num), this);
		c.setAvailable(true);
		this.copyList.add(c);
	}

	public Copy getAvailableCopy(){
		for(Copy c : copyList){
			if( c.isAvailable() ){
				return c;
			}
		}
		
		return null;
	}
	
	public void checkoutACopy(int numCopies){
		int i = 1;
		for(Copy c : copyList){
			if(c.isAvailable()){
				c.setAvailable(false);
			}
			
			if( i == numCopies ){
				break;
			}
			
			i++;
		}		
	}
	
	public abstract String getNumber();
}
