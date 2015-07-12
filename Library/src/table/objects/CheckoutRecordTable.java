package table.objects;

public class CheckoutRecordTable {
	private String title;
	private String number;
	private String borrowedDate;
	private String dueDate;
	private String status;
	private String copyNum;

	public String getCopyNum() {
		return copyNum;
	}
	public void setCopyNum(String copyNum) {
		this.copyNum = copyNum;
	}
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
	public String getBorrowedDate() {
		return borrowedDate;
	}
	public void setBorrowedDate(String borrowedDate) {
		this.borrowedDate = borrowedDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
}
