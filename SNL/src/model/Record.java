package model;

public class Record {
	private String name;
	private String number;
	
	public Record(String name, String number) {
		super();
		this.name = name;
		this.number = number;
	}
	
	public Record(String number) {
		super();
		this.name = null;
		this.number = number;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Record [name=" + name + ", number=" + number + "]";
	}
	
}
