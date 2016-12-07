package main;

public class Debug {
	private boolean debug;
	private String classname;
	private String os;
	
	public Debug(String _classname){
		this.debug = true;
		this.classname = _classname;
		this.os = System.getProperty("os.name").toLowerCase();
	}
	
	public boolean getDebug(){
		return this.debug;
	}
	
	public String getOS(){
		return this.os;
	}
	
	public void DebugMessage(String s){
		if(debug){
			System.out.println(classname + ":		" + s);
		}
	}
}
