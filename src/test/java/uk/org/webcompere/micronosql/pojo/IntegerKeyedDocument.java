package uk.org.webcompere.micronosql.pojo;

import uk.org.webcompere.micronosql.annotation.Key;

public class IntegerKeyedDocument {
	@Key
	private int id;
	
	private String name;

	public IntegerKeyedDocument() {
		
	}
	
	public IntegerKeyedDocument(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
