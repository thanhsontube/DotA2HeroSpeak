
package son.nt.dota2.facebook.object;


public class FbLikesPaging{
   	private FbLikesCursors cursors;
   	private String next;
   	
   	public FbLikesPaging() {
   		cursors = new FbLikesCursors(null,null);
   	}

 	public FbLikesCursors getCursors(){
		return this.cursors;
	}
	public void setCursors(FbLikesCursors cursors){
		this.cursors = cursors;
	}
 	public String getNext(){
		return this.next;
	}
	public void setNext(String next){
		this.next = next;
	}
}
