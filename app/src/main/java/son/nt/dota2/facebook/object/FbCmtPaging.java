
package son.nt.dota2.facebook.object;


public class FbCmtPaging{
   	private FbCmtCursors cursors;
   	private String next;
   	
   	public FbCmtPaging() {
   		cursors = new FbCmtCursors(null,null);
   	}

 	public FbCmtCursors getCursors(){
		return this.cursors;
	}
	public void setCursors(FbCmtCursors cursors){
		this.cursors = cursors;
	}
 	public String getNext(){
		return this.next;
	}
	public void setNext(String next){
		this.next = next;
	}
}
