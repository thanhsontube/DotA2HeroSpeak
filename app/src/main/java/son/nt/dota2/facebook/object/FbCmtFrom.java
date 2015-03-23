
package son.nt.dota2.facebook.object;


public class FbCmtFrom{
   	private String id;
   	private String name;
   	private String source;
   	
   	
 	public FbCmtFrom(String source) {
	    super();
	    this.source = source;
    }
	public FbCmtFrom(String id, String name) {
	    super();
	    this.id = id;
	    this.name = name;
    }
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	
}
