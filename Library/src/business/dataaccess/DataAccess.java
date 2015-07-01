package business.dataaccess;

import java.util.ArrayList;

import business.objects.Book;
import business.objects.BookList;
import business.objects.LibraryMember;

public interface DataAccess {
	public void saveLibraryMember(String name, LibraryMember member);
	public LibraryMember readLibraryMember(String name);
	public void saveBook(Book book);
	public Book getBookByTitle(String title);
	public BookList getBookList();
	public ArrayList<Book> wildSearchBookByTitle(String title);
}
