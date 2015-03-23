
package son.nt.dota2.facebook.object;


public class FbAlbumsDto{
   	private boolean can_upload;
   	private int count;
   	private String cover_photo;
   	private String created_time;
   	private FbAlbumsFrom from;
   	private String id;
   	private String link;
   	private String name;
   	private String type;
   	private String updated_time;

   	
	public FbAlbumsDto(String id, String name, String type, int count) {
	    super();
	    this.id = id;
	    this.name = name;
	    this.type = type;
	    this.count = count;
    }
	public FbAlbumsDto() {
	    super();
	    // TODO Auto-generated constructor stub
    }
	public boolean getCan_upload(){
		return this.can_upload;
	}
	public void setCan_upload(boolean can_upload){
		this.can_upload = can_upload;
	}
 	public int getCount(){
		return this.count;
	}
	public void setCount(int count){
		this.count = count;
	}
 	public String getCover_photo(){
		return this.cover_photo;
	}
	public void setCover_photo(String cover_photo){
		this.cover_photo = cover_photo;
	}
 	public String getCreated_time(){
		return this.created_time;
	}
	public void setCreated_time(String created_time){
		this.created_time = created_time;
	}
 	public FbAlbumsFrom getFrom(){
		return this.from;
	}
	public void setFrom(FbAlbumsFrom from){
		this.from = from;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getLink(){
		return this.link;
	}
	public void setLink(String link){
		this.link = link;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
 	public String getUpdated_time(){
		return this.updated_time;
	}
	public void setUpdated_time(String updated_time){
		this.updated_time = updated_time;
	}
}
