package business.dataaccess;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import business.objects.Book;
import business.objects.BookList;
import business.objects.LibraryMember;



public class DataAccessFacade implements DataAccess {
	public static final String OUTPUT_DIR = System.getProperty("user.dir") 
			+ "\\src\\business\\dataaccess\\storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	private BookList bookList = new BookList();
	
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
		try {
			bookList.addBook(book);
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "BookList");
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(bookList);
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
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, "BookList");
			in = new ObjectInputStream(Files.newInputStream(path));
			bookList = (BookList)in.readObject();
			
			for (Book book: (ArrayList<Book>) bookList.getBooks()) {
				if (book.getTitle().equalsIgnoreCase(title)) {
					return book;
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

		return null;
	}
	
}
