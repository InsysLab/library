package validator;

public final class PublicationValidator {
	public static String validateBook(String title, String isbn, int checkoutLength, int authors) {
		return title(title) + isbn(isbn) + checkoutLength(checkoutLength) + authors(authors);		
	}
	
	public static String validatePeriodical(String title, String issuenum, int checkoutLength) {
		return title(title) + issuenum(issuenum) + checkoutLength(checkoutLength);		
	}	
	
	public static String title(String title) {
		String errorMessage = "";
        if (title.length() > 255) {
        	errorMessage +="Title not be greater than 255 characters\n";  
		}
        return errorMessage;
	}
	
	public static String isbn(String isbn) {
		String errorMessage = "";
		if (!isbn.matches("[\\d-?]*")) {
        	errorMessage = "ISBN must be numeric and can be separated with dashes.\n";  
		} 
		if( isbn.length() > 13 ){
			errorMessage += "ISBN must not be more than 13 digits\n";  
		}
        return errorMessage;
	}
	
	public static String issuenum(String issuenum) {
		String errorMessage = "";
		if (!issuenum.matches("[\\d-]*")) {
        	errorMessage = "Issue Number must be numeric\n";  
		} 
		if( issuenum.length() > 45 ){
			errorMessage += "Issue Number must not be more than 45 characters\n";  
		}		
        return errorMessage;
	}	
	
	public static String checkoutLength(int days) {
		String errorMessage = "";
		if ( days < 2 ) {
            errorMessage = "Max Checkout Length must be at least 2 days\n"; 
        }
       
        return errorMessage;
	}
	
	public static String authors(int authors) {
		String errorMessage = "";
		if ( authors < 1 ) {
        	errorMessage += "Must have at least one author\n";  
		}
        
        return errorMessage;
	}
}
