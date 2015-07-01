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
	
	public void saveMember(LibraryMember member);
	public MemberList getMemberList();
	public LibraryMember readLibraryMember(String name);
	public void saveLibraryMember(String name, LibraryMember member);
	
	public void saveBook(Book book);
	public Book getBookByTitle(String title);
	public BookList getBookList();
	public ArrayList<Book> wildSearchBookByTitle(String title);
	public ArrayList<Book> wildSearchBookByISBN(String ISBN);
	
	public void saveAuthor(Author author);
	public AuthorList getAuthorList();
	
	public PeriodicalList getPeriodicalList();
	public void savePeriodical(Periodical periodical);
	public ArrayList<Periodical> wildSearchPeriodicalByTitle(String title);
	public ArrayList<Periodical> wildSearchPeriodicalByIssueNo(String issueNo);
	
	public LibraryMember searchLibraryMemberByID(int idNo);
	public void saveUpdateMember(LibraryMember member);
	
	
}
