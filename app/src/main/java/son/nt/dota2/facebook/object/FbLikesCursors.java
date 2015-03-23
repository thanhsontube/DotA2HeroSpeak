
package son.nt.dota2.facebook.object;


public class FbLikesCursors{
   	private String after;
   	private String before;

 	
	public FbLikesCursors(String after, String before) {
	    super();
	    this.after = after;
	    this.before = before;
    }
	public String getAfter(){
		return this.after;
	}
	public void setAfter(String after){
		this.after = after;
	}
 	public String getBefore(){
		return this.before;
	}
	public void setBefore(String before){
		this.before = before;
	}
}
