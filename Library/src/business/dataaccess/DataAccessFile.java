package business.dataaccess;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import business.objects.Address;
import business.objects.Author;
import business.objects.AuthorList;
import business.objects.Book;
import business.objects.BookList;
import business.objects.CheckoutRecord;
import business.objects.CheckoutRecordEntry;
import business.objects.Copy;
import business.objects.LibraryMember;
import business.objects.MemberList;
import business.objects.Periodical;
import business.objects.PeriodicalList;
import business.objects.Publication;


public class DataAccessFile implements DataAccess {
	enum StorageType {
		BookList, PeriodicalList, MemberList, CheckoutRecord, AuthorList;
	}
	
	public static final String OUTPUT_DIR = System.getProperty("user.dir") 
			+ "\\src\\business\\dataaccess\\storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	@Override
	public void closeConnection() {
	}
	
	@Override
	public void saveBook(Book book) {
		BookList bookList = getBookList();

		if (bookList == null) {
			bookList = BookList.getInstance();
		}		
		
		bookList.addBook(book);
		saveToStorage(StorageType.BookList, bookList);				
	}
	
//	@Override
//	public void updateBook(Book book){
//		BookList bookList = getBookList();
//
//		if (bookList == null) {
//			bookList = BookList.getInstance();
//		}	
//		
//		if (bookList != null && bookList.getBooks().size() > 0) {
//			for (Book bk: (ArrayList<Book>) bookList.getBooks()) {
//				if (book.getISBN() == bk.getISBN()) {
//					bk.setAuthorlist(book.getAuthorlist());
//					bk.setCopyList(book.getCopyList());
//					bk.setMaxcheckoutlength(book.getMaxcheckoutlength());
//					bk.setTitle(book.getTitle());
//				}
//			}
//		}
//		
//		saveToStorage(StorageType.BookList, bookList);			
//	}
	
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
	
	private BookList getBookList() {
		BookList bookList = (BookList)readFromStorage(StorageType.BookList);
		
		if (bookList == null) {
			bookList = BookList.getInstance();
		}			
		
		return bookList;
	}
	
	@Override
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
	
	@Override
	public ArrayList<Book> wildSearchBookByTitle(String title) {
		List<Book> bookList = getBookList().getBooks();
		if (bookList != null && bookList.size() > 0) {
			List<Book> list = bookList.stream()
									  .filter( b -> b.getTitle().toUpperCase().indexOf(title.toUpperCase())!=-1 )
									  .collect(toList());
			
			return new ArrayList<Book>(list);
		}
		
		return null;
	}
	
	@Override
	public ArrayList<Book> wildSearchBookByISBN(String ISBN) {
		List<Book> bookList = getBookList().getBooks();
		if (bookList != null && bookList.size() > 0) {
			List<Book> list = bookList.stream()
									  .filter( b -> b.getISBN().indexOf(ISBN.toUpperCase())!=-1 )
									  .collect(toList());
			
			return new ArrayList<Book>(list);
		}
		
		return null;		
	}
	
	@Override
	public void saveAuthor(Author author) {
		AuthorList authorlist = (AuthorList)readFromStorage(StorageType.AuthorList);

		if (authorlist == null) {
			authorlist = AuthorList.getInstance();
		}		
		
		authorlist.addAuthor(author);
		saveToStorage(StorageType.AuthorList, authorlist);		
	}
	
	@Override
	public ArrayList<Author> getAuthorList() {
		AuthorList authorList = (AuthorList)readFromStorage(StorageType.AuthorList);
		return (ArrayList<Author>)authorList.getAuthors();
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
	
	//@Override
	private MemberList getMemberList() {
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
	
//	@Override
//	public void updatePeriodical(Periodical periodical){
//		PeriodicalList plist = getPeriodicalList();
//
//		if (plist == null) {
//			plist = PeriodicalList.getInstance();
//		}	
//		
//		if (plist != null && plist.getPeriodicals().size() > 0) {
//			for (Periodical p: (ArrayList<Periodical>) plist.getPeriodicals()) {
//				if (periodical.getIssueNo() == p.getIssueNo()) {
//					p.setCopyList(periodical.getCopyList());
//					p.setMaxcheckoutlength(periodical.getMaxcheckoutlength());
//					p.setTitle(periodical.getTitle());
//				}
//			}
//		}
//		
//		saveToStorage(StorageType.PeriodicalList, plist);				
//	}
	
	@Override
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
	
	private PeriodicalList getPeriodicalList() {
		PeriodicalList periodicalList = (PeriodicalList)readFromStorage(StorageType.PeriodicalList);
		return periodicalList;
	}
	
	public Periodical searchPeriodicalByIssueNo(String issueNo){
		List<Periodical> periodicalList = getPeriodicalList().getPeriodicals();
		if (periodicalList != null && periodicalList.size() > 0) {
			Optional<Periodical> periodical = periodicalList.stream()
												  .filter( p -> p.getIssueNo().equals(issueNo))
												  .findFirst();
			
			if(periodical.isPresent()){
				return periodical.get();
			}
		}
		
		return null;		
	}
	
	@Override
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
	
	@Override
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
	
	@Override
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
	
	private CheckoutRecord getCheckoutRecord(){
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
	
	@Override
	public CheckoutRecordEntry getCheckoutRecordEntryById(int memberId, String type, String pubNum, String copyNum){
		List<CheckoutRecordEntry> checkoutRecord = getCheckoutRecordEntryByMemberID(memberId);
		
		for(CheckoutRecordEntry recEntry : checkoutRecord){
			if( recEntry.getCopy().getPublication().getNumber().equals(pubNum) && recEntry.getCopy().getCopyNo().equals(copyNum)){
				return recEntry;
			}
		}
		
		return null;
	}
	
	@Override
	public void removeCheckoutRecordEntry(int memberId, Publication pub, Copy copy){
		CheckoutRecord checkoutRecord = getCheckoutRecord();
		List<CheckoutRecordEntry> entries = new ArrayList<CheckoutRecordEntry>();
		
		for(CheckoutRecordEntry recEntry : checkoutRecord.getEntry()){
			if( recEntry.getMember().getMemberID() == memberId ){
				Publication p = recEntry.getCopy().getPublication();
			
				if(pub instanceof Book){
					Book b1 = (Book)pub;
					Book b2 = (Book)p;
					if( ! b1.getISBN().equals(b2.getISBN()) ){
						entries.add(recEntry);					}
				} else {
					Periodical pr1 = (Periodical)pub;
					Periodical pr2 = (Periodical)p;
					if( ! pr1.getIssueNo().equals(pr2.getIssueNo()) ){
						entries.add(recEntry);
					}			
				}				
			}
		}
		
		CheckoutRecord newList = CheckoutRecord.getInstance();
		newList.setEntry(entries);
		
		saveToStorage(StorageType.CheckoutRecord, newList);
	}		
	
	@Override
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
			return initializeStorage(type);
			//e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}
		return retVal;
	}
	
	public static Object initializeStorage(StorageType type) {
		Object obj = null;
		if (type.equals(StorageType.PeriodicalList)) {
			obj = PeriodicalList.getInstance();
		} else if (type.equals(StorageType.BookList)) {
			obj = BookList.getInstance();
		} else if (type.equals(StorageType.MemberList)) {
			obj = MemberList.getInstance();
		} else if (type.equals(StorageType.CheckoutRecord)) {
			obj = CheckoutRecord.getInstance();
		} else if (type.equals(StorageType.AuthorList)) {
			obj = AuthorList.getInstance();
		}

		return obj;
	}

	public static void main(String args[]) {
		Address add1 = new Address("4", "Iowa City", "Iowa", "52555");
		LibraryMember mem1 = new LibraryMember(1, "Jesus", "Sadang", "8808000", add1);
		Address add2 = new Address("5", "Fairfield City", "Iowa", "52557");
		LibraryMember mem2 = new LibraryMember(2, "Mark", "Pit", "7707000", add2);
		Address add3 = new Address("6", "San Francisco", "CA", "52887");
		LibraryMember mem3 = new LibraryMember(3, "Boldkhu", "Dar", "6606000", add3);
		Address add4 = new Address("7", "LA", "CA", "52899");
		LibraryMember mem4 = new LibraryMember(4, "Bill", "Gates", "5505000", add4);
		
		MemberList mL = MemberList.getInstance();
		mL.getMembers().clear();
		mL.addMember(mem1);mL.addMember(mem2);mL.addMember(mem3);mL.addMember(mem4);
		saveToStorage(StorageType.MemberList, mL);
		
		Book book1 = new Book("1111", 2, "Java Book");
		book1.addCopy(); book1.addCopy();
		Book book2 = new Book("2222", 3, "C++ Book");
		book2.addCopy(); book2.addCopy();
		Book book3 = new Book("3333", 4, "Perl Book");
		book3.addCopy();
		Book book4 = new Book("4444", 5, "Python Book");
		book4.addCopy();

		Author a1 = new Author("Mark", "Markus", "8808000", "The quick brown fox", add1);
		book1.addAuthor(a1);
		Author a2 = new Author("Luke", "Lukas", "7707000", "The quick brown fox", add2);
		book2.addAuthor(a2);
		Author a3 = new Author("John", "Johnnie", "6606000", "The quick brown fox", add3);
		book3.addAuthor(a3);
		Author a4 = new Author("Dan", "Dantes", "5505000", "The quick brown fox", add4);
		book4.addAuthor(a4);
		
		AuthorList aL = AuthorList.getInstance();
		aL.getAuthors().clear();
		aL.addAuthor(a1); aL.addAuthor(a2);aL.addAuthor(a3);aL.addAuthor(a4);
		saveToStorage(StorageType.AuthorList, aL);	
		
		Periodical p1 = new Periodical("Time", "1234", 2);
		p1.addCopy(); p1.addCopy();
		Periodical p2 = new Periodical("Newsweek", "5678", 3);
		p2.addCopy(); p2.addCopy();
		Periodical p3 = new Periodical("Nat Geo", "9101", 4);
		p3.addCopy(); p3.addCopy();
		
		LocalDate dueDate = LocalDate.of(2015, 5, 1).plusDays(book1.getMaxCheckoutLength());
		CheckoutRecordEntry entry1 = new CheckoutRecordEntry(book1.getAvailableCopy(), LocalDate.of(2015, 5, 1), dueDate);
		entry1.setMember(mem1);
		LocalDate dueDate2 = LocalDate.of(2015, 6, 1).plusDays(book2.getMaxCheckoutLength());
		CheckoutRecordEntry entry2 = new CheckoutRecordEntry(book2.getAvailableCopy(), LocalDate.of(2015, 6, 1), dueDate2);
		entry2.setMember(mem2);
		LocalDate dueDate3 = LocalDate.now().plusDays(book3.getMaxCheckoutLength());
		CheckoutRecordEntry entry3 = new CheckoutRecordEntry(book3.getAvailableCopy(), LocalDate.now(), dueDate3);
		entry3.setMember(mem3);
		LocalDate dueDate4 = LocalDate.now().plusDays(book4.getMaxCheckoutLength());
		CheckoutRecordEntry entry4 = new CheckoutRecordEntry(book4.getAvailableCopy(), LocalDate.now(), dueDate4);
		entry4.setMember(mem4);
		LocalDate dueDatea = LocalDate.now().plusDays(p1.getMaxCheckoutLength());
		CheckoutRecordEntry entrya = new CheckoutRecordEntry(p1.getAvailableCopy(), LocalDate.now(), dueDatea);
		entrya.setMember(mem1);
		LocalDate dueDateb = LocalDate.of(2015, 6, 1).plusDays(p2.getMaxCheckoutLength());
		CheckoutRecordEntry entryb = new CheckoutRecordEntry(p2.getAvailableCopy(), LocalDate.of(2015, 6, 1), dueDateb);
		entryb.setMember(mem2);
		LocalDate dueDatec = LocalDate.now().plusDays(p3.getMaxCheckoutLength());
		CheckoutRecordEntry entryc = new CheckoutRecordEntry(p3.getAvailableCopy(), LocalDate.now(), dueDatec);
		entryc.setMember(mem3);
		
		CheckoutRecord checkoutRecord = CheckoutRecord.getInstance();
		checkoutRecord.addEntry(entry1); checkoutRecord.addEntry(entry2);checkoutRecord.addEntry(entry3);checkoutRecord.addEntry(entry4);
		checkoutRecord.addEntry(entrya); checkoutRecord.addEntry(entryb);checkoutRecord.addEntry(entryc);
		saveToStorage(StorageType.CheckoutRecord, checkoutRecord);	
		
		BookList bL = BookList.getInstance();
		bL.addBook(book1);bL.addBook(book2);bL.addBook(book3);bL.addBook(book4);
		saveToStorage(StorageType.BookList, bL);
		
		PeriodicalList pL = PeriodicalList.getInstance();
		pL.addPeriodical(p1);pL.addPeriodical(p2);pL.addPeriodical(p3);
		saveToStorage(StorageType.PeriodicalList, pL);	
	}
}
