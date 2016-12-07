package main;

public class Debug {
	private boolean debug;
	private String classname;
	
	public Debug(String _classname){
		this.debug = false;
		this.classname = _classname;
	}
	
	public boolean getDebug(){
		return this.debug;
	}
	
	public void DebugMessage(String s){
		if(debug){
			System.out.println(classname + ":		" + s);
		}
	}
}
