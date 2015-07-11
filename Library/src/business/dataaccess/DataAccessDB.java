package business.dataaccess;
import static java.util.stream.Collectors.toList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


public class DataAccessDB implements DataAccess {
	private static Connection conn = null;
	
	public DataAccessDB (String DB_URL, String USERNAME, String PASSWORD) {
		try {
		if(conn == null) {	
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("Got connection...");
			conn.setAutoCommit(true);
		}
		} catch (SQLException sqe) {
			System.out.println("Cannot get connection...");
		}
	}
	
	public void closeConnection(Connection con)  throws SQLException {
		if(con != null && !con.isClosed()) {
			con.close();
		}
	}
	
	enum StorageType {
		BookList, PeriodicalList, MemberList, CheckoutRecord, AuthorList;
	}
	
	public static final String OUTPUT_DIR = System.getProperty("user.dir") 
			+ "\\src\\business\\dataaccess\\storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	@Override
	public void saveBook(Book book) {
		int pubid = saveAddPublication(book);
		List<Author> authors = book.getAuthorlist();
		if (authors != null) {
			for (Author auth: authors) {
				//saveAddpublicationAuthor(pubid, auth.getAuthorID());
			}
		}
	}
	
	private void saveAddpublicationAuthor(int pub, int author) {
		try {
			String updateSQL = "INSERT INTO APP.PUBLICATIONAUTHOR (AUTHORID, PUBID) VALUES (?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
			preparedStatement.setInt(1, pub);
			preparedStatement.setInt(2, author);
			int rs = preparedStatement.executeUpdate();
			conn.commit();
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}	
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
	public ArrayList<Book> wildSearchBookByTitle(String tit) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		try {
			String selectSQL = "SELECT TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH FROM APP.PUBLICATION WHERE PUBTYPE = ? AND UPPER(TITLE) like ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setString(1, "book");
			preparedStatement.setString(2, "%" + tit.toUpperCase() + "%" );
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			boolean found = false;
			while (rs.next()) {
				found = true;
				String title = rs.getString("TITLE");
				String num = rs.getString("ISBN_ISSUENUM");	
				int max = rs.getInt("MAXCHECKOUTLENGTH");
				Book book = new Book(num.trim(), max, title.trim());
				//Add retrieve of Copies
				bookList.add(book);
			}
			if (found) {
				return bookList;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return null;
	}
	
	private ArrayList getCopyList(int pubID) {
		
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
		try {
			int address = saveAddress(author.getAddress());
			String updateSQL = "INSERT INTO APP.AUTHOR (FIRSTNAME, LASTNAME, TELEPHONE, ADDRESSID) VALUES (?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(updateSQL, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, author.getFirstName());
			preparedStatement.setString(2, author.getLastName());
			preparedStatement.setString(3, author.getPhone());
			preparedStatement.setInt(4, address);
			//System.out.println(preparedStatement.);
			int row = preparedStatement.executeUpdate();
			conn.commit();
			if (row == 0) {
				throw new SQLException("Failed creating author record!");
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int authID = generatedKeys.getInt(1);
					author.setAuthorID(authID);
				}
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}	
	}
	
	@Override
	public ArrayList<Author> getAuthorList() {
		ArrayList<Author> list = new ArrayList<Author>();
		try {
			String selectSQL = "SELECT FIRSTNAME, LASTNAME, TELEPHONE, ADDRESSID, BIO FROM APP.AUTHOR";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			boolean found = false;
			while (rs.next()) {
				String fName = rs.getString("FIRSTNAME");
				String lName = rs.getString("LASTNAME");
				String tel = rs.getString("TELEPHONE");	
				int addID = rs.getInt("ADDRESSID");	
				String bio = rs.getString("BIO");
				Address address = this.getAddress(addID+"");
				Author author = new Author(fName.trim(), lName.trim(), tel.trim(), bio.trim(), address);
				list.add(author);
				found = true;
			}
			if (found) { 
				return list;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void saveMember(LibraryMember member) {
		try {
			int address = saveAddress(member.getAddress());
			String updateSQL = "INSERT INTO APP.LIBRARYMEMBER (FIRSTNAME, LASTNAME, TELEPHONE, ADDRESSID, MEMBERID) VALUES (?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
			preparedStatement.setString(1, member.getFirstName());
			preparedStatement.setString(2, member.getLastName());
			preparedStatement.setString(3, member.getPhone());
			preparedStatement.setInt(4, address);
			preparedStatement.setInt(5, member.getMemberID());
			//System.out.println(preparedStatement.);
			int rs = preparedStatement.executeUpdate();
			//updateAddress(member.getAddress(), member.getMemberID()+"");
			conn.commit();
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}		
	}
	
	private int saveAddress(Address address) {
		int addressID = 0;
		try {
			String insertSQL = "INSERT INTO APP.ADDRESS (STREET, CITY, STATE, ZIP) VALUES(?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, address.getStreet());
			preparedStatement.setString(2, address.getCity());
			preparedStatement.setString(3, address.getState());
			preparedStatement.setString(4, address.getZip());
			//System.out.println(preparedStatement.);
			int row = preparedStatement.executeUpdate();
			conn.commit();
			if (row == 0) {
				throw new SQLException("Failed creating address record!");
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					addressID = generatedKeys.getInt(1);
				}
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return addressID;
	}
	
	//@Override
	public MemberList getMemberList() {
		MemberList memberList = (MemberList)readFromStorage(StorageType.MemberList);
		return memberList;
	}
	
	private int saveAddPublication(Publication pub) {
		int pubID = 0;
		try {
			String insertSQL = "INSERT INTO APP.PUBLICATION (PUBTYPE, TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH) VALUES(?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);

			if (pub instanceof Book) {
				//Book bk = (Book) pub;
				preparedStatement.setString(1, "book");
			} else {
				preparedStatement.setString(1, "periodical");
			}

			preparedStatement.setString(2, pub.getTitle());
			preparedStatement.setString(3, pub.getNumber());
			preparedStatement.setInt(4, pub.getMaxCheckoutLength());
			//System.out.println(preparedStatement.);
			int row = preparedStatement.executeUpdate();
			conn.commit();
			if (row == 0) {
				throw new SQLException("Failed creating publication record!");
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					pubID = generatedKeys.getInt(1);
				}
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return pubID;
	}
	
	@Override
	public void savePeriodical (Periodical periodical) {
		saveAddPublication(periodical);
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
	
	@Override
	public PeriodicalList getPeriodicalList() {
		PeriodicalList periodicalList = (PeriodicalList)readFromStorage(StorageType.PeriodicalList);
		return periodicalList;
	}
	
	public Periodical searchPeriodicalByIssueNo(String issueNo){
		try {
			String selectSQL = "SELECT TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH FROM APP.PUBLICATION WHERE ISBN_ISSUENUM = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setString(1, issueNo);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				String title = rs.getString("TITLE");
				String num = rs.getString("ISBN_ISSUENUM");	
				int max = rs.getInt("MAXCHECKOUTLENGTH");
				Periodical periodical = new Periodical(title.trim(), num.trim(), max);
				return periodical;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ArrayList<Periodical> wildSearchPeriodicalByTitle(String tit) {
		ArrayList<Periodical> periodicalList = new ArrayList<Periodical>();
		try {
			String selectSQL = "SELECT TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH FROM APP.PUBLICATION WHERE PUBTYPE = ? AND UPPER(TITLE) like ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setString(1, "periodical");
			preparedStatement.setString(2, "%" + tit.toUpperCase() + "%" );
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			boolean found = false;
			while (rs.next()) {
				found = true;
				String title = rs.getString("TITLE");
				String num = rs.getString("ISBN_ISSUENUM");	
				int max = rs.getInt("MAXCHECKOUTLENGTH");
				Periodical periodical = new Periodical(title.trim(), num.trim(), max);
				periodicalList.add(periodical);
			}
			if (found) {
				return periodicalList;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
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

		try {
			String selectSQL = "SELECT MEMBERID, ADDRESSID, FIRSTNAME, LASTNAME, TELEPHONE FROM APP.LIBRARYMEMBER WHERE MEMBERID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setString(1, idNo+"");
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				String memberid = rs.getString("MEMBERID");
				String addressid = rs.getString("ADDRESSID");	
				String firstname = rs.getString("FIRSTNAME");
				String lastname = rs.getString("LASTNAME");
				String tel = rs.getString("TELEPHONE");
				LibraryMember member = new LibraryMember(Integer.parseInt(memberid.trim()), 
						firstname.trim(), lastname.trim(), tel.trim(), getAddress(addressid));
				return member;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		
		return null;
	}
	
	private Address getAddress(String ID) {
		try {
			String selectSQL = "SELECT ID, STREET, CITY, STATE, ZIP FROM APP.ADDRESS WHERE ID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setString(1, ID);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				String street = rs.getString("STREET");
				String city = rs.getString("CITY");	
				String state = rs.getString("STATE");
				String zip = rs.getString("ZIP");
				Address a = new Address(street.trim(),city.trim(),state.trim(),zip.trim() );
				a.setId(Integer.parseInt(ID));
				return a;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return null;
	}
	

	@Override
	public void saveUpdateMember(LibraryMember member) {
		try {
			String updateSQL = "UPDATE APP.LIBRARYMEMBER SET FIRSTNAME=?, LASTNAME=?, TELEPHONE=? WHERE MEMBERID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
			preparedStatement.setString(1, member.getFirstName());
			preparedStatement.setString(2, member.getLastName());
			preparedStatement.setString(3, member.getPhone());
			preparedStatement.setString(4, member.getMemberID() + "");
			//System.out.println(preparedStatement.);
			int rs = preparedStatement.executeUpdate();
			updateAddress(member.getAddress());
			conn.commit();
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
	}
	
	private void updateAddress(Address addr) {
		try {
			String updateSQL = "UPDATE APP.ADDRESS SET STREET=?, CITY=?, STATE=?, ZIP=? WHERE ID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
			preparedStatement.setString(1, addr.getCity());
			preparedStatement.setString(2, addr.getStreet());
			preparedStatement.setString(3, addr.getState());
			preparedStatement.setString(4, addr.getZip());
			preparedStatement.setInt(5, addr.getId());
			//System.out.println(preparedStatement.);
			preparedStatement.executeUpdate();
			
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
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
	
	@Override
	public CheckoutRecordEntry getCheckoutRecordEntryById(int memberId, String idNum){
		List<CheckoutRecordEntry> checkoutRecord = getCheckoutRecordEntryByMemberID(memberId);
		
		for(CheckoutRecordEntry recEntry : checkoutRecord){
			if( recEntry.getCopy().getPublication().getNumber().equals(idNum) ){
				return recEntry;
			}
		}
		
		return null;
	}
	
	@Override
	public void removeCheckoutRecordEntry(int memberId, Publication pub){
		CheckoutRecord checkoutRecord = getCheckoutRecord();
		List<CheckoutRecordEntry> entries = new ArrayList<CheckoutRecordEntry>();
		
		for(CheckoutRecordEntry recEntry : checkoutRecord.getEntry()){
			if( recEntry.getMember().getMemberID() == memberId ){
				Publication p = recEntry.getCopy().getPublication();
			
				if(pub.getClass().getName().contains("Book")){
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
		
	}
	
	public static Object readFromStorage(StorageType type) {
		return null;
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

}
