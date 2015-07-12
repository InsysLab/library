package business.dataaccess;

import java.util.ArrayList;

import business.objects.Author;
import business.objects.AuthorList;
import business.objects.Publication;
import business.objects.Book;
import business.objects.BookList;
import business.objects.Copy;
import business.objects.LibraryMember;
import business.objects.MemberList;
import business.objects.Periodical;
import business.objects.PeriodicalList;
import business.objects.CheckoutRecord;
import business.objects.CheckoutRecordEntry;

public interface DataAccess {
	
	public void saveMember(LibraryMember member);
	public LibraryMember searchLibraryMemberByID(int idNo);
	public void saveUpdateMember(LibraryMember member);
	
	public void saveBook(Book book);
	public void saveUpdateBook(Book book);
	//public void updateBook(Book book);
	public Book getBookByTitle(String title);
	public Book getBookByISBN(String isbn);
	public BookList getBookList();
	public ArrayList<Book> wildSearchBookByTitle(String title);
	public ArrayList<Book> wildSearchBookByISBN(String ISBN);
	
	public void saveAuthor(Author author);
	public ArrayList<Author> getAuthorList();
	
	public PeriodicalList getPeriodicalList();
	public void savePeriodical(Periodical periodical);
	public void saveUpdatePeriodical(Periodical periodical);
	//public void updatePeriodical(Periodical periodical);
	public Periodical searchPeriodicalByIssueNo(String issueNo);
	public ArrayList<Periodical> wildSearchPeriodicalByTitle(String title);
	public ArrayList<Periodical> wildSearchPeriodicalByIssueNo(String issueNo);
	
	public CheckoutRecord getCheckoutRecord();
	public void saveCheckoutRecordEntry(CheckoutRecordEntry entry);
	public ArrayList<CheckoutRecordEntry> getCheckoutRecordEntryByMemberID(int idNo);
	public void removeCheckoutRecordEntry(int memberId, Publication pub);
	public CheckoutRecordEntry getCheckoutRecordEntryById(int memberId, String type, String pubNum, String copyNum);
	public CheckoutRecordEntry getCheckoutRecordEntry(Copy copy);
}
