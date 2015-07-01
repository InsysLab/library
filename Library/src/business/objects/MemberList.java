package business.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MemberList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -617070314315355241L;
	private static MemberList instance = null;
	   protected MemberList() {
	      // Exists only to defeat instantiation.
	   }
	   public static MemberList getInstance() {
	      if(instance == null) {
	         instance = new MemberList();
	      }
	      return instance;
	   }
	   
	private List<LibraryMember> members = new ArrayList<>();

	public List<LibraryMember> getMembers() {
		return members;
	}
	
	public void addMember(LibraryMember member) {
		members.add(member);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return members.toString();
	}
}
