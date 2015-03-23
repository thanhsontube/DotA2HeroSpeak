
package son.nt.dota2.facebook.object;


public class FbCmtData{
	private String id;
	private String created_time;
	private String message;
	private int like_count;
	private FbCmtFrom from;
   	private boolean can_remove;
   	private boolean user_likes;
   	
   	

 	public FbCmtData(String id, String created_time, String message, int like_count, FbCmtFrom from) {
	    super();
	    this.id = id;
	    this.created_time = created_time;
	    this.message = message;
	    this.like_count = like_count;
	    this.from = from;
    }
	public boolean getCan_remove(){
		return this.can_remove;
	}
	public void setCan_remove(boolean can_remove){
		this.can_remove = can_remove;
	}
 	public String getCreated_time(){
		return this.created_time;
	}
	public void setCreated_time(String created_time){
		this.created_time = created_time;
	}
 	public FbCmtFrom getFrom(){
		return this.from;
	}
	public void setFrom(FbCmtFrom from){
		this.from = from;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public Number getLike_count(){
		return this.like_count;
	}
	public void setLike_count(int like_count){
		this.like_count = like_count;
	}
 	public String getMessage(){
		return this.message;
	}
	public void setMessage(String message){
		this.message = message;
	}
 	public boolean getUser_likes(){
		return this.user_likes;
	}
	public void setUser_likes(boolean user_likes){
		this.user_likes = user_likes;
	}
}
