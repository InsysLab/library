package business.dataaccess;
import static java.util.stream.Collectors.toList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		book.setId(pubid);
		saveUpdateCopies(book);
		List<Author> authors = book.getAuthorlist();
		if (authors != null) {
			for (Author auth: authors) {
				saveAddpublicationAuthor(pubid, auth.getAuthorID());
			}
		}
	}
	
	private void saveAddpublicationAuthor(int pub, int author) {
		try {
			String updateSQL = "INSERT INTO APP.PUBLICATIONAUTHOR (AUTHORID, PUBID) VALUES (?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
			preparedStatement.setInt(1, author);
			preparedStatement.setInt(2, pub);
			int rs = preparedStatement.executeUpdate();
			if (rs == 0) {
				System.out.println("Cannot add Author record!");
			}
			conn.commit();
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}	
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
	
	private Publication getPublicationByDBID(int id) {
		Publication pub = null;
		try {
			String selectSQL = "SELECT TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH, PUBTYPE FROM APP.PUBLICATION WHERE ID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			boolean found = false;
			if (rs.next()) {
				found = true;
				String title = rs.getString("TITLE").trim();
				String num = rs.getString("ISBN_ISSUENUM").trim();	
				int max = rs.getInt("MAXCHECKOUTLENGTH");
				String type = rs.getString("PUBTYPE").trim();
				if (type.equals("book")) {
					pub = new Book(num.trim(), max, title.trim());
					pub.setId(id);
				} else {
					pub = new Periodical(title.trim(), num.trim(), max);
					pub.setId(id);
				}
			}
			if (found) {
				return pub;
			} else {
				System.out.println("Cannot find publication");
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return null;	
	}
	
	@Override
	public Book getBookByISBN(String isbn){
		Book book = null;
		try {
			String selectSQL = "SELECT TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH, ID FROM APP.PUBLICATION WHERE PUBTYPE = ? AND ISBN_ISSUENUM = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setString(1, "book");
			preparedStatement.setString(2, isbn);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			boolean found = false;
			while (rs.next()) {
				found = true;
				String title = rs.getString("TITLE");
				String num = rs.getString("ISBN_ISSUENUM");	
				int max = rs.getInt("MAXCHECKOUTLENGTH");
				int id = rs.getInt("ID");
				book = new Book(num.trim(), max, title.trim());
				book.setId(id);
				//Add retrieve of Copies
				book.setCopyList(getCopyList(book));
			}
			if (found) {
				return book;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return null;	
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
		saveUpdateCopies(book);
	}
	
	@Override
	public ArrayList<Book> wildSearchBookByTitle(String tit) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		try {
			String selectSQL = "SELECT TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH, ID FROM APP.PUBLICATION WHERE PUBTYPE = ? AND UPPER(TITLE) like ?";
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
				int id = rs.getInt("ID");
				Book book = new Book(num.trim(), max, title.trim());
				book.setId(id);
				//Add retrieve of Copies
				book.setCopyList(getCopyList(book));
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
	
	private ArrayList<Copy> getCopyList(Publication pub) {
		ArrayList<Copy> copyList = new ArrayList<Copy>();
		try {
			String selectSQL = "SELECT COPYNUMBER, STATUS FROM APP.PUBCOPY WHERE PUBID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setInt(1, pub.getId());
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			boolean found = false;
			while (rs.next()) {
				found = true;
				String copyNumber = rs.getString("COPYNUMBER");
				boolean avl = rs.getBoolean("STATUS");	
				Copy copy = new Copy(copyNumber, pub);
				copy.setAvailable(avl);
				//Add retrieve of Copies
				copyList.add(copy);
			}
			if (found) {
				return copyList;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return copyList;
	}
	
	private void saveUpdateCopies(Publication pub) {
		List<Copy> listCopy = pub.getCopyList();
		for (Copy copy: listCopy) {
			Copy dbCopy = getCopy(pub, Integer.parseInt(copy.getCopyNo()));
			if (dbCopy != null) {
				 if (dbCopy.isAvailable() != copy.isAvailable()) {
					 updateCopy(copy);
				 }
			} else {
				addCopy(copy);
			}
		}
	}
	
	private Copy getCopy(Publication pub, int id) {
		Copy copy = null;
		try {
			String selectSQL = "SELECT COPYNUMBER, STATUS FROM APP.PUBCOPY WHERE PUBID = ? AND COPYNUMBER = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setInt(1, pub.getId());
			preparedStatement.setInt(2, id);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			//boolean found = false;
			if (rs.next()) {
				//found = true;
				String copyNumber = rs.getString("COPYNUMBER");
				boolean avl = rs.getBoolean("STATUS");	
				copy = new Copy(copyNumber.trim(), pub);
				copy.setAvailable(avl);
				return copy;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return copy;
	}
	
	private Copy getCopyByDBID(int id) {
		Copy copy = null;
		try {
			String selectSQL = "SELECT COPYNUMBER, STATUS, PUBID FROM APP.PUBCOPY WHERE ID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			//boolean found = false;
			if (rs.next()) {
				//found = true;
				String copyNumber = rs.getString("COPYNUMBER");
				boolean avl = rs.getBoolean("STATUS");
				int pubDBID = rs.getInt("PUBID");
				Publication pub = getPublicationByDBID(pubDBID);
				copy = new Copy(copyNumber.trim(), pub);
				copy.setAvailable(avl);
				return copy;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return copy;
	}
	
	private int getCopyDBID(Copy copy) {
		int copyID = 0;
		try {
			String selectSQL = "SELECT ID FROM APP.PUBCOPY WHERE PUBID = ? AND COPYNUMBER = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setInt(1, copy.getPublication().getId());
			preparedStatement.setInt(2, Integer.parseInt(copy.getCopyNo()));
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			//boolean found = false;
			if (rs.next()) {
				//found = true;
				return rs.getInt(1);
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		return copyID;
	}
	
	private void addCopy(Copy copy) {
		try {
			String insertSQL = "INSERT INTO APP.PUBCOPY (COPYNUMBER, STATUS, PUBID) VALUES (?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
			preparedStatement.setInt(1, Integer.parseInt(copy.getCopyNo()));
			preparedStatement.setBoolean(2, true);
			preparedStatement.setInt(3,copy.getPublication().getId());
			//System.out.println(preparedStatement.);
			int rs = preparedStatement.executeUpdate();
			//boolean found = false;
			if (rs == 0) {
				System.out.println("Cannot add copy record!");
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
	}
	
	private void updateCopy(Copy copy) {
		try {
			String insertSQL = "UPDATE APP.PUBCOPY SET STATUS = ? where COPYNUMBER = ? AND PUBID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
			preparedStatement.setBoolean(1, copy.isAvailable());
			preparedStatement.setInt(2, Integer.parseInt(copy.getCopyNo()));
			preparedStatement.setInt(3, copy.getPublication().getId());
			//System.out.println(preparedStatement.);
			int rs = preparedStatement.executeUpdate();
			//boolean found = false;
			if (rs == 0) {
				System.out.println("Cannot update copy record!");
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
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
			String selectSQL = "SELECT FIRSTNAME, LASTNAME, TELEPHONE, ADDRESSID, BIO, ID FROM APP.AUTHOR";
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
				int id = rs.getInt("ID");
				Address address = this.getAddress(addID+"");
				Author author = new Author(fName.trim(), lName.trim(), tel.trim(), bio.trim(), address);
				author.setAuthorID(id);
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
		int id = saveAddPublication(periodical);
		periodical.setId(id);
		saveUpdateCopies(periodical);
	}
	
	@Override
	public void saveUpdatePeriodical(Periodical periodical) {
		saveUpdateCopies(periodical);
	}	
	
	@Override
	public PeriodicalList getPeriodicalList() {
		PeriodicalList periodicalList = (PeriodicalList)readFromStorage(StorageType.PeriodicalList);
		return periodicalList;
	}
	
	public Periodical searchPeriodicalByIssueNo(String issueNo){
		try {
			String selectSQL = "SELECT TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH, ID FROM APP.PUBLICATION WHERE ISBN_ISSUENUM = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setString(1, issueNo);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				String title = rs.getString("TITLE");
				String num = rs.getString("ISBN_ISSUENUM");	
				int max = rs.getInt("MAXCHECKOUTLENGTH");
				int id = rs.getInt("ID");
				Periodical periodical = new Periodical(title.trim(), num.trim(), max);
				periodical.setId(id);
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
			String selectSQL = "SELECT TITLE, ISBN_ISSUENUM, MAXCHECKOUTLENGTH, ID FROM APP.PUBLICATION WHERE PUBTYPE = ? AND UPPER(TITLE) like ?";
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
				int id = rs.getInt("ID");
				Periodical periodical = new Periodical(title.trim(), num.trim(), max);
				periodical.setId(id);
				//Add retrieve of Copies
				periodical.setCopyList(getCopyList(periodical));
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
	
	private LibraryMember getLibraryMemberByDBID(int idNo) {

		try {
			String selectSQL = "SELECT MEMBERID, ADDRESSID, FIRSTNAME, LASTNAME, TELEPHONE FROM APP.LIBRARYMEMBER WHERE ID = ?";
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
	
	private int getLibraryMemberDBID(int idNo) {
		int dbID = 0;
		try {
			String selectSQL = "SELECT ID FROM APP.LIBRARYMEMBER WHERE MEMBERID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setString(1, idNo+"");
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				dbID = rs.getInt("ID");
				return dbID;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
		
		return dbID;
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
		ArrayList<CheckoutRecordEntry> list = new ArrayList<CheckoutRecordEntry>();
		int memDBID = getLibraryMemberDBID(idNo);
		try {
			String selectSQL = "SELECT IDMEM, COPYID, CHECKOUTDATE, DUEDATE FROM APP.CHECKOUTRECORD WHERE IDMEM = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setInt(1, memDBID);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			boolean found = false;
			while (rs.next()) {
				found = true;
				Date coDate = rs.getDate("CHECKOUTDATE");
				Date dueDate = rs.getDate("DUEDATE");
				int memID = rs.getInt("IDMEM");
				int copyID = rs.getInt("COPYID");
				Copy copy = getCopyByDBID(copyID);
				CheckoutRecordEntry entry = new CheckoutRecordEntry(copy, coDate.toLocalDate(), dueDate.toLocalDate());
				LibraryMember member = getLibraryMemberByDBID(memID);
				entry.setMember(member);
				list.add(entry);
			}
			if (found) {
				return list;
			} else {
				System.out.println("No checkout record entry found!");
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
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
		try {
			String insertSQL = "INSERT INTO APP.CHECKOUTRECORD (IDMEM, COPYID, CHECKOUTDATE, DUEDATE) VALUES(?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, getLibraryMemberDBID(entry.getMember().getMemberID()));
			preparedStatement.setInt(2, getCopyDBID(entry.getCopy()));
			preparedStatement.setDate(3, Date.valueOf(entry.getCheckoutDate()));
			preparedStatement.setDate(4, Date.valueOf(entry.getDueDate()));
			//System.out.println(preparedStatement.);
			int row = preparedStatement.executeUpdate();
			conn.commit();
			if (row == 0) {
				throw new SQLException("Failed creating CheckoutRecordEntry record!");
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int id = generatedKeys.getInt(1);
				}
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
	}
	
	@Override
	public CheckoutRecordEntry getCheckoutRecordEntryById(int memberId, String type, String pubNum, String copyNum){
		CheckoutRecordEntry checkoutRecord = null;
		Publication pub = null;
		if (type.equals("book")) {
			pub = getBookByISBN(pubNum);
		} else {
			pub = searchPeriodicalByIssueNo(pubNum);
		}
		
		if (pub == null) {
			return checkoutRecord;
		}
		
		Copy copy = getCopy(pub, Integer.parseInt(copyNum));
		
		if (copy == null) {
			return checkoutRecord;
		}
		checkoutRecord = getCheckoutRecordEntry(copy);		
		return checkoutRecord;
	}
	
	@Override
	public void removeCheckoutRecordEntry(int memberId, Publication pub, Copy copy){
		int memDBID = getLibraryMemberDBID(memberId);
		int copyDBID = getCopyDBID(copy);
		if (copyDBID == 0) {
			System.out.println("Cannot find copy " + copy.getCopyNo() + " of publication id +" + pub.getId());
			return;
		}
		try {
			String deleteSQL = "DELETE FROM APP.CHECKOUTRECORD WHERE COPYID = ? AND IDMEM=?";
			PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, copyDBID);
			preparedStatement.setInt(2, memDBID);
			//System.out.println(preparedStatement.);
			int rs = preparedStatement.executeUpdate();
			//boolean found = false;
			if (rs == 0) {
				System.out.println("Cannot find copy " + copy.getCopyNo() + " of publication id +" + pub.getId());
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
		}
	}		
	
	@Override
	public CheckoutRecordEntry getCheckoutRecordEntry(Copy copy) {
		CheckoutRecordEntry entry = null;
		int id = getCopyDBID(copy);
		if (id == 0) return null;
		try {
			String selectSQL = "SELECT IDMEM, COPYID, CHECKOUTDATE, DUEDATE FROM APP.CHECKOUTRECORD WHERE COPYID = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);
			//System.out.println(preparedStatement.);
			ResultSet rs = preparedStatement.executeQuery();
			//boolean found = false;
			if (rs.next()) {
				//found = true;
				Date coDate = rs.getDate("CHECKOUTDATE");
				Date dueDate = rs.getDate("DUEDATE");
				int memID = rs.getInt("IDMEM");
				entry = new CheckoutRecordEntry(copy, coDate.toLocalDate(), dueDate.toLocalDate());
				LibraryMember member = getLibraryMemberByDBID(memID);
				entry.setMember(member);
				return entry;
			}
		} catch (SQLException sqe) {
			//System.out.println();
			sqe.printStackTrace();
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
