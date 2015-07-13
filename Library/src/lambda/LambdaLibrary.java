package lambda;


import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;

import business.objects.Book;
import business.objects.Periodical;

public class LambdaLibrary {

	public final static BiFunction<List<Book>, String, Optional<Book>> SearchBookByISBN	
		=  (list, isbn) -> list.stream()
		  .filter( b -> b.getISBN().equals(isbn) )
		  .findFirst();		
	
	public final static BiFunction<List<Book>, String, List<Book>> WildSearchBookByTitle
		=  (list, title) -> list.stream()
		  .filter( b -> b.getTitle().toUpperCase().indexOf(title.toUpperCase())!=-1 )
		  .collect(Collectors.toList());		
		
	public final static BiFunction<List<Book>, String, List<Book>> WildSearchBookByISBN
		=  (list, isbn) -> list.stream()
		  .filter( b -> b.getISBN().indexOf(isbn.toUpperCase())!=-1 )
		  .collect(Collectors.toList());			
	
	public final static BiFunction<List<Periodical>, String, List<Periodical>> WildSearchPeriodicalByTitle
		=  (list, title) -> list.stream()
		  .filter( b -> b.getTitle().toUpperCase().indexOf(title.toUpperCase())!=-1 )
		  .collect(Collectors.toList());		
		
	public final static BiFunction<List<Periodical>, String, Optional<Periodical>> SearchPeriodicalByIssueNo	
		=  (list, issuenum) -> list.stream()
		  .filter( b -> b.getIssueNo().equals(issuenum) )
		  .findFirst();	
		
}
