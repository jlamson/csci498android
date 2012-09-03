package csci498.jlamson.lunchlist;

public class Restaurant {
	private String name = "";
	private String address = "";
	private String type = "";
	
	public String getName() 	{ return name; }
	public String getAddress()	{ return address; }
	public String getType()		{ return type; }
	
	public void setName(String name)
		{ this.name = name; }
	public void setAddress(String address)
		{ this.address = address; }
	public void setType(String type)
		{ this.type = type; }

	@Override
	public String toString() {
		return getName();
	}
}
