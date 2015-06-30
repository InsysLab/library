package business.objects;

import java.io.Serializable;

public class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3460297249840624268L;
	
	private String firstName;
	private String lastName;
	private String phone;
	private Address address;
	
	public Person(String firstname, String lastname, String phonenum, Address addr)
	{
		this.firstName = firstname;
		this.lastName = lastname;
		this.phone = phonenum;
		this.address = addr;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Firstname: " + firstName + ", Lastname: " + lastName + ", Phone: " + phone + ", Address: " + address;
	}

}
