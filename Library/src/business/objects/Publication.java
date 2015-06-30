package business.objects;

public abstract class Publication {
	private String title;
	
	public Publication(String title)
	{
		this.title = title;
	}
	
	abstract int getMaxCheckoutLength();

}
