package business.dataaccess;

import java.util.ArrayList;

import business.objects.Author;
import business.objects.AuthorList;
import business.objects.Book;
import business.objects.BookList;
import business.objects.LibraryMember;
import business.objects.MemberList;
import business.objects.Periodical;
import business.objects.PeriodicalList;

public interface DataAccess {
	public void saveLibraryMember(String name, LibraryMember member);
	public LibraryMember readLibraryMember(String name);
	public void saveBook(Book book);
	public Book getBookByTitle(String title);
	public BookList getBookList();
	public ArrayList<Book> wildSearchBookByTitle(String title);
	public void saveAuthor(Author author);
	public AuthorList getAuthorList();
	public void saveMember(LibraryMember member);
	public MemberList getMemberList();
	public void savePeriodical(Periodical periodical);
	public PeriodicalList getPeriodicalList();
	public ArrayList<Book> wildSearchBookByISBN(String ISBN);
	public ArrayList<Periodical> wildSearchPeriodicalByTitle(String title);
	public ArrayList<Periodical> wildSearchPeriodicalByIssueNo(String issueNo);
}
