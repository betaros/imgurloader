package main;

public class Profile {
	String id;
	String sectet;
	
	public Profile(String _id, String _secret){
		this.id = _id;
		this.sectet = _secret;
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getSecret(){
		return this.sectet;
	}
}
