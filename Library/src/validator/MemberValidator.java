package validator;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class MemberValidator {
	public static String validateMember(String id, String fName, String lname, String phone) {
		return id(id) + firstName(fName) + lastName(lname) + phone(phone);		
	}
	
	public static String id(String id) {
		String errorMessage = "";
		try {
			int memberID = Integer.parseInt(id);
		} catch (NumberFormatException nfe) {
			errorMessage +="Member ID must be a number!\n";
		}
        if (id.length() > 10) {
        	errorMessage +="Member ID must not be greater than 10 characters\n";  
		}
        return errorMessage;
	}
	
	public static String firstName(String fName) {
		String errorMessage = "";
        if (!fName.matches("[a-zA-Z]*")) {
        	errorMessage +="First name field may not contain spaces or characters other than a-z, A-Z\n";  
		}
        if (fName.length() > 30) {
        	errorMessage +="First name must not be greater than 30 characters\n";  
		}
        return errorMessage;
	}
	
	public static String lastName(String lName) {
		String errorMessage = "";
        if (!lName.matches("[a-zA-Z]*")) {
        	errorMessage +="First name field may not contain spaces or characters other than a-z, A-Z\n";  
		}
        if (lName.length() > 30) {
        	errorMessage +="First name must not be greater than 30 characters\n";  
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
