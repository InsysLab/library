package table.objects;

public class SearchPublicationTable {
	private String title;
	private String number;
	private String maxDays;
	private String intCopies;
	private String status;
	private String availableCopies;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMaxDays() {
		return maxDays;
	}
	public void setMaxDays(String maxDays) {
		this.maxDays = maxDays;
	}
	public String getIntCopies() {
		return intCopies;
	}
	public void setIntCopies(String intCopies) {
		this.intCopies = intCopies;
	}
	public String getAvailableCopies() {
		return availableCopies;
	}
	public void setAvailableCopies(String availableCopies) {
		this.availableCopies = availableCopies;
	}

}
