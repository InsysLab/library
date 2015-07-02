package business.dataaccess;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

import business.objects.Author;
import business.objects.AuthorList;
import business.objects.Book;
import business.objects.BookList;
import business.objects.LibraryMember;
import business.objects.MemberList;
import business.objects.Periodical;
import business.objects.PeriodicalList;
import business.objects.CheckoutRecord;
import business.objects.CheckoutRecordEntry;


public class DataAccessFacade implements DataAccess {
	enum StorageType {
		BookList, PeriodicalList, MemberList, CheckoutRecord, AuthorList;
	}
	
	public static final String OUTPUT_DIR = System.getProperty("user.dir") 
			+ "\\src\\business\\dataaccess\\storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	/*public void saveLibraryMember(String name, LibraryMember member) {
		ObjectOutputStream out = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, name);
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(member);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}
	}
	*/
	public LibraryMember readLibraryMember(String name) {
		ObjectInputStream in = null;
		LibraryMember member = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, name);
			in = new ObjectInputStream(Files.newInputStream(path));
			member = (LibraryMember)in.readObject();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}
		return member;
	}
	
	public void saveBook(Book book) {
		ObjectOutputStream out = null;
		BookList booklist = getBookList();
		try {
			//bookList = this.getBookList();
			if (booklist == null) {
				booklist = BookList.getInstance();
			}
			booklist.addBook(book);
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "BookList");
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(booklist);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}
	}
	
	public Book getBookByTitle(String title) {
		ObjectInputStream in = null;
		BookList bookList = null;
		Book bk = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "BookList");
			in = new ObjectInputStream(Files.newInputStream(path));
			bookList = (BookList)in.readObject();
			
			for (Book book: (ArrayList<Book>) bookList.getBooks()) {
				if (book.getTitle().equalsIgnoreCase(title)) {
					bk = book;					
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}

		return bk;
	}  
	
	public Book getBookByISBN(String isbn){
		ObjectInputStream in = null;
		BookList bookList = null;
		Book bk = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "BookList");
			in = new ObjectInputStream(Files.newInputStream(path));
			bookList = (BookList)in.readObject();
			
			for (Book book: (ArrayList<Book>) bookList.getBooks()) {
				if (book.getISBN().equals(isbn)) {
					bk = book;					
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}

		return bk;		
	}
	
	public BookList getBookList() {
		ObjectInputStream in = null;
		BookList bookList = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "BookList");
			in = new ObjectInputStream(Files.newInputStream(path));
			bookList = (BookList)in.readObject();
			
			//System.out.println(bookList.toString());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}

		return bookList;
	}
	
	public ArrayList<Book> wildSearchBookByTitle(String title) {
		BookList bookList =  getBookList();
		if (bookList != null && bookList.getBooks().size() > 0) {
			ArrayList<Book> list = new ArrayList<Book>();
			for (Book book: (ArrayList<Book>) bookList.getBooks()) {
				if (book.getTitle().toUpperCase().indexOf(title.toUpperCase())!=-1) {
					list.add(book);					
				}
			}
			return list;
		}
		return null;
	}
	
	public ArrayList<Book> wildSearchBookByISBN(String ISBN) {
		BookList bookList =  getBookList();
		if (bookList != null && bookList.getBooks().size() > 0) {
			ArrayList<Book> list = new ArrayList<Book>();
			for (Book book: (ArrayList<Book>) bookList.getBooks()) {
				if (book.getISBN().indexOf(ISBN.toUpperCase())!=-1) {
					list.add(book);					
				}
			}
			return list;
		}
		return null; //Comment
	}
	
	@Override
	public void saveAuthor(Author author) {
		// TODO Auto-generated method stub
		ObjectOutputStream out = null;
		AuthorList authorlist = AuthorList.getInstance();
		try {
			authorlist.addAuthor(author);
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "AuthorList");
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(authorlist);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}
	}
	@Override
	public AuthorList getAuthorList() {
		// TODO Auto-generated method stub
		ObjectInputStream in = null;
		AuthorList authorList = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "AuthorList");
			in = new ObjectInputStream(Files.newInputStream(path));
			authorList = (AuthorList)in.readObject();
			
			//System.out.println(bookList.toString());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}

		return authorList;
	}
	
	@Override
	public void saveMember(LibraryMember member) {
		MemberList memberlist = getMemberList();

		if (memberlist == null) {
			memberlist = MemberList.getInstance();
		}		
		
		memberlist.addMember(member);
		saveToStorage(StorageType.MemberList, memberlist);			
	}
	
	@Override
	public MemberList getMemberList() {
		MemberList memberList = (MemberList)readFromStorage(StorageType.MemberList);
		return memberList;
	}
	
	@Override
	public void savePeriodical (Periodical periodical) {
		PeriodicalList plist = getPeriodicalList();

		if (plist == null) {
			plist = PeriodicalList.getInstance();
		}		
		
		plist.addPeriodical(periodical);;
		saveToStorage(StorageType.PeriodicalList, plist);			
	}
	
	@Override
	public PeriodicalList getPeriodicalList() {
		PeriodicalList periodicalList = (PeriodicalList)readFromStorage(StorageType.PeriodicalList);
		return periodicalList;
	}
	
	public Periodical searchPeriodicalByIssueNo(String issueNo){
		PeriodicalList periodicalList =  getPeriodicalList();
		if (periodicalList != null && periodicalList.getPeriodicals().size() > 0) {
			Periodical periodical = null;
			for (Periodical p: (ArrayList<Periodical>) periodicalList.getPeriodicals()) {
				if (p.getIssueNo().equals(issueNo)) {
					periodical = p;
				}
			}
			return periodical;
		}
		return null;		
	}
	
	public ArrayList<Periodical> wildSearchPeriodicalByTitle(String title) {
		PeriodicalList periodicalList =  getPeriodicalList();
		if (periodicalList != null && periodicalList.getPeriodicals().size() > 0) {
			ArrayList<Periodical> list = new ArrayList<Periodical>();
			for (Periodical periodical: (ArrayList<Periodical>) periodicalList.getPeriodicals()) {
				if (periodical.getTitle().toUpperCase().indexOf(title.toUpperCase())!=-1) {
					list.add(periodical);					
				}
			}
			return list;
		}
		return null;
	}
	
	public ArrayList<Periodical> wildSearchPeriodicalByIssueNo(String issueNo) {
		PeriodicalList periodicalList =  getPeriodicalList();
		if (periodicalList != null && periodicalList.getPeriodicals().size() > 0) {
			ArrayList<Periodical> list = new ArrayList<Periodical>();
			for (Periodical periodical: (ArrayList<Periodical>) periodicalList.getPeriodicals()) {
				if (periodical.getIssueNo().toUpperCase().indexOf(issueNo.toUpperCase())!=-1) {
					list.add(periodical);					
				}
			}
			return list;
		}
		return null;
	}
	
	public LibraryMember searchLibraryMemberByID(int idNo) {
		MemberList list =  getMemberList();
		if (list != null && list.getMembers().size() > 0) {
			for (LibraryMember member: (ArrayList<LibraryMember>) list.getMembers()) {
				if (member.getMemberID() == idNo) {
					return member;					
				}
			}
		}
		return null;
	}
	
	@Override
	public void saveUpdateMember(LibraryMember member) {
		// TODO Auto-generated method stub
		ObjectOutputStream out = null;
		MemberList memberlist = getMemberList();
		
		try {
			if (memberlist == null) {
				memberlist = MemberList.getInstance();
			}	
			
			if (memberlist != null && memberlist.getMembers().size() > 0) {
				for (LibraryMember mem: (ArrayList<LibraryMember>) memberlist.getMembers()) {
					if (member.getMemberID() == mem.getMemberID()) {
						mem.setAddress(member.getAddress());
						mem.setFirstName(member.getFirstName());
						mem.setLastName(member.getLastName());
						mem.setPhone(member.getPhone());
						mem.setAddress(member.getAddress());
					}
				}
			}
			
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "MemberList");
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(memberlist);
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}
	}
	
	@Override
	public ArrayList<CheckoutRecordEntry> getCheckoutRecordEntryByMemberID(int idNo){
		List<CheckoutRecordEntry> entryList = getCheckoutRecord().getEntry();

		if (entryList.size() > 0) {
			ArrayList<CheckoutRecordEntry> list = new ArrayList<CheckoutRecordEntry>();
			for (CheckoutRecordEntry entry: entryList) {
				if (entry.getMember().getMemberID() == idNo) {
					list.add(entry);					
				}
			}
			
			return list;
		}
		
		return null;		
	}
	
	@Override
	public CheckoutRecord getCheckoutRecord(){
		// TODO Auto-generated method stub
		ObjectInputStream in = null;
		CheckoutRecord checkoutRecord = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "CheckoutRecord");
			in = new ObjectInputStream(Files.newInputStream(path));
			checkoutRecord = (CheckoutRecord)in.readObject();
			
			//System.out.println(checkoutRecord.toString());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}

		return checkoutRecord;		
	}
	
	@Override
	public void saveCheckoutRecordEntry(CheckoutRecordEntry entry){
		// TODO Auto-generated method stub
		ObjectOutputStream out = null;
		CheckoutRecord checkoutRecord = getCheckoutRecord();
		try {
			if(checkoutRecord == null){
				checkoutRecord = CheckoutRecord.getInstance();
			}
			
			checkoutRecord.addEntry(entry);
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "CheckoutRecord");
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(checkoutRecord);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}		
	}
	
	public static void saveToStorage(StorageType type, Object ob) {
		ObjectOutputStream out = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(ob);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}
	}
	
	public static Object readFromStorage(StorageType type) {
		ObjectInputStream in = null;
		Object retVal = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			in = new ObjectInputStream(Files.newInputStream(path));
			retVal = in.readObject();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}
		return retVal;
	}	
}
