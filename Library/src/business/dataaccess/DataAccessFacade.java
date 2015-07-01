package business.dataaccess;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import business.objects.Author;
import business.objects.AuthorList;
import business.objects.Book;
import business.objects.BookList;
import business.objects.LibraryMember;
import business.objects.MemberList;
import business.objects.Periodical;
import business.objects.PeriodicalList;



public class DataAccessFacade implements DataAccess {
	public static final String OUTPUT_DIR = System.getProperty("user.dir") 
			+ "\\src\\business\\dataaccess\\storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	public void saveLibraryMember(String name, LibraryMember member) {
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
		// TODO Auto-generated method stub
		ObjectOutputStream out = null;
		MemberList memberlist = MemberList.getInstance();
		try {
			memberlist.addMember(member);
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
	public MemberList getMemberList() {
		// TODO Auto-generated method stub
		ObjectInputStream in = null;
		MemberList memberList = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "MemberList");
			in = new ObjectInputStream(Files.newInputStream(path));
			memberList = (MemberList)in.readObject();
			
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

		return memberList;
	}
	@Override
	public void savePeriodical (Periodical periodical) {
		// TODO Auto-generated method stub
		ObjectOutputStream out = null;
		PeriodicalList plist = getPeriodicalList();
		try {
			if (plist == null) {
				plist = PeriodicalList.getInstance();
			}
			plist.addPeriodical(periodical);
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "PeriodicalList");
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(plist);
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
	public PeriodicalList getPeriodicalList() {
		// TODO Auto-generated method stub
		ObjectInputStream in = null;
		PeriodicalList periodicalList = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "PeriodicalList");
			in = new ObjectInputStream(Files.newInputStream(path));
			periodicalList = (PeriodicalList)in.readObject();
			
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

		return periodicalList;
	}
}
