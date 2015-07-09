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
import business.objects.Copy;
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
	
	@Override
	public void saveBook(Book book) {
		BookList bookList = getBookList();

		if (bookList == null) {
			bookList = BookList.getInstance();
		}		
		
		bookList.addBook(book);
		saveToStorage(StorageType.BookList, bookList);				
	}
	
	@Override
	public void updateBook(Book book){
		BookList bookList = getBookList();

		if (bookList == null) {
			bookList = BookList.getInstance();
		}	
		
		if (bookList != null && bookList.getBooks().size() > 0) {
			for (Book bk: (ArrayList<Book>) bookList.getBooks()) {
				if (book.getISBN() == bk.getISBN()) {
					bk.setAuthorlist(book.getAuthorlist());
					bk.setCopyList(book.getCopyList());
					bk.setMaxcheckoutlength(book.getMaxcheckoutlength());
					bk.setTitle(book.getTitle());
				}
			}
		}
		
		saveToStorage(StorageType.BookList, bookList);			
	}
	
	@Override
	public Book getBookByTitle(String title) {
		BookList bookList =  getBookList();
		Book bk = null;

		for (Book book: (ArrayList<Book>) bookList.getBooks()) {
			if (book.getTitle().equalsIgnoreCase(title)) {
				bk = book;					
			}
		}

		return bk;
	}  
	
	@Override
	public Book getBookByISBN(String isbn){
		BookList bookList =  getBookList();
		Book bk = null;
		
		for (Book book: (ArrayList<Book>) bookList.getBooks()) {
			if (book.getISBN().equals(isbn)) {
				bk = book;					
			}
		}

		return bk;		
	}
	
	@Override
	public BookList getBookList() {
		BookList bookList = (BookList)readFromStorage(StorageType.BookList);
		return bookList;
	}
	
	@Override
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
	
	@Override
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
		AuthorList authorlist = getAuthorList();

		if (authorlist == null) {
			authorlist = AuthorList.getInstance();
		}		
		
		authorlist.addAuthor(author);
		saveToStorage(StorageType.AuthorList, authorlist);		
	}
	
	@Override
	public AuthorList getAuthorList() {
		AuthorList authorList = (AuthorList)readFromStorage(StorageType.AuthorList);
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
		
		plist.addPeriodical(periodical);
		saveToStorage(StorageType.PeriodicalList, plist);			
	}
	
	@Override
	public void updatePeriodical(Periodical periodical){
		PeriodicalList plist = getPeriodicalList();

		if (plist == null) {
			plist = PeriodicalList.getInstance();
		}	
		
		if (plist != null && plist.getPeriodicals().size() > 0) {
			for (Periodical p: (ArrayList<Periodical>) plist.getPeriodicals()) {
				if (periodical.getIssueNo() == p.getIssueNo()) {
					p.setCopyList(periodical.getCopyList());
					p.setMaxcheckoutlength(periodical.getMaxcheckoutlength());
					p.setTitle(periodical.getTitle());
				}
			}
		}
		
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
		MemberList memberlist = getMemberList();

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

		saveToStorage(StorageType.MemberList, memberlist);			
	}
	
	@Override
	public ArrayList<CheckoutRecordEntry> getCheckoutRecordEntryByMemberID(int idNo){
		CheckoutRecord checkoutRecord = getCheckoutRecord();
		if(checkoutRecord == null) return null;
		List<CheckoutRecordEntry> entryList = checkoutRecord.getEntry();

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
		CheckoutRecord checkoutRecord = (CheckoutRecord)readFromStorage(StorageType.CheckoutRecord);
		return checkoutRecord;		
	}
	
	@Override
	public void saveCheckoutRecordEntry(CheckoutRecordEntry entry){
		CheckoutRecord checkoutRecord = getCheckoutRecord();

		if(checkoutRecord == null){
			checkoutRecord = CheckoutRecord.getInstance();
		}
		
		checkoutRecord.addEntry(entry);
		
		saveToStorage(StorageType.CheckoutRecord, checkoutRecord);	
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
	
	public void saveUpdateBook(Book book) {
		BookList booklist = getBookList();
		if (booklist == null) {
			booklist = BookList.getInstance();
		}
		List<Book> list = booklist.getBooks();
		for (int i=0; i<list.size(); i++) {
			Book b = list.get(i);
			if (b.getISBN().equals(book.getISBN())) {
				list.set(i, book);
				saveToStorage(StorageType.BookList, booklist);
				break;
			}
		}
	}
	
	public void saveUpdatePeriodical(Periodical periodical) {
		PeriodicalList periodicallist = getPeriodicalList();
		if (periodicallist == null) {
			periodicallist = PeriodicalList.getInstance();
		}
		List<Periodical> list = periodicallist.getPeriodicals();
		for (int i=0; i<list.size(); i++) {
			Periodical b = list.get(i);
			if (b.getIssueNo().equals(periodical.getIssueNo())) {
				list.set(i, periodical);
				saveToStorage(StorageType.PeriodicalList, periodicallist);
				break;
			}
		}
	}
	
	public CheckoutRecordEntry getCheckoutRecordEntry(Copy copy) {
		List<CheckoutRecordEntry> entryList = getCheckoutRecord().getEntry();

		if (entryList != null && entryList.size() > 0) {
			for (CheckoutRecordEntry entry: entryList) {
				if (entry.getCopy().equals(copy)) {
					return 	entry;				
				}
			}
		}
		
		return null;
	}
}
