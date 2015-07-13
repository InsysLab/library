package validator;

public final class AddressValidator {
	public static String validateAddress(String street, String city, String state, String zip) {
		return street(street) + city(city) + state(state) + zip(zip);		
	}
	
	public static String street(String street) {
		String errorMessage = "";
		if (!street.matches("^[0-9a-zA-Z. ]+$")) {
            errorMessage += "Street field can only contain alphanumeric characters \n"; 
        }
        if (street.length() > 30) {
        	errorMessage +="Street must not be greater than 30 characters\n";  
		}
        return errorMessage;
	}
	
	public static String zip(String zip) {
		String errorMessage = "";
		if (!zip.matches("^[0-9]{5,5}")) {
        	errorMessage += "Zip must be numeric with exactly 5 digits\n";  
		} 
        return errorMessage;
	}
	
	public static String city(String city) {
		String errorMessage = "";
		if (!city.matches("[a-zA-Z ]*")) {
            errorMessage += "City must consist of Alphabet characters\n"; 
        }
        if (city.length() > 30) {
        	errorMessage +="City must not be greater than 30 characters\n";  
		}
        return errorMessage;
	}
	
	public static String state(String state) {
		String errorMessage = "";
		if (!state.matches("[a-zA-Z]*")) {
        	errorMessage += "State must consist of Alphabet characters\n";  
		}
        if (state.length() > 10) {
        	errorMessage +="City must not be greater than 10 characters";  
		}
        return errorMessage;
	}
}
