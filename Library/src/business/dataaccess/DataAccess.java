package business.dataaccess;

import business.objects.Book;
import business.objects.LibraryMember;

public interface DataAccess {
	public void saveLibraryMember(String name, LibraryMember member);
	public LibraryMember readLibraryMember(String name);
	public void saveBook(Book book);
	public Book getBookByTitle(String title);
}
