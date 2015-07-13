package validator;

public final class AuthorValidator {
	public static String validateAuthor(String bio, String fName, String lname, String phone) {
		return  bio(bio) + firstName(fName) + lastName(lname) + phone(phone);		
	}
	
//	public static String id(String id) {
//		String errorMessage = "";
//		try {
//			int memberID = Integer.parseInt(id);
//		} catch (NumberFormatException nfe) {
//			errorMessage +="Author ID must be a number!\n";
//		}
//        if (id.length() > 10) {
//        	errorMessage +="Author ID must not be greater than 10 characters\n";  
//		}
//        return errorMessage;
//	}
	
	public static String bio(String bio) {
		String errorMessage = "";
//        if (!bio.matches("[0-9a-zA-Z]*")) {
//        	errorMessage +="Bio field may not contain spaces or characters other than a-z, A-Z\n";  
//		}
        if (bio.length() > 254) {
        	errorMessage +="Bio field must not be greater than 254 characters\n";  
		}
        if (bio.length() < 10) {
        	errorMessage +="Bio field must be greater than 10 characters\n";  
		}
        return errorMessage;
	}
	
	public static String firstName(String fName) {
		String errorMessage = "";
        if (!fName.matches("[a-zA-Z]*")) {
        	errorMessage +="First name field may not contain spaces or characters other than a-z, A-Z\n";  
		}
        if (fName.length() > 45) {
        	errorMessage +="First name must not be greater than 45 characters\n";  
		}
        return errorMessage;
	}
	
	public static String lastName(String lName) {
		String errorMessage = "";
        if (!lName.matches("[a-zA-Z]*")) {
        	errorMessage +="Last name field may not contain spaces or characters other than a-z, A-Z\n";  
		}
        if (lName.length() > 45) {
        	errorMessage +="Last name must not be greater than 45 characters\n";  
		}
        return errorMessage;
	}
	
	public static String phone(String phone) {
		String errorMessage = "";
		if (!phone.matches("^[0-9-]*")) {
            errorMessage += "Not a valid phone number!\n"; 
        }
        if (phone.length() > 12) {
        	errorMessage +="Phone number must not be greater than 12 characters\n";  
		}
        
        return errorMessage;
	}
}
