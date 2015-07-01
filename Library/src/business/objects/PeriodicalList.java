package business.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeriodicalList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3120005427545719438L;
	private static PeriodicalList instance = null;
	   protected PeriodicalList() {
	      // Exists only to defeat instantiation.
	   }
	   public static PeriodicalList getInstance() {
	      if(instance == null) {
	         instance = new PeriodicalList();
	      }
	      return instance;
	   }
	   
	private List<Periodical> plist = new ArrayList<>();

	public List<Periodical> getPeriodicals() {
		return plist;
	}
	
	public void addPeriodical(Periodical p) {
		plist.add(p);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return plist.toString();
	}
}
