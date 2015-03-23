
package son.nt.dota2.facebook.object;

import java.util.ArrayList;
import java.util.List;

public class FbLikes {
   	private List<FbLikesData> data;
   	private FbLikesPaging paging;
   	private FbLikesSummary summary;
   	
   	public FbLikes() {
   		data = new ArrayList<FbLikesData>();
   		paging = new FbLikesPaging();
   	}

 	public List<FbLikesData> getData(){
		return this.data;
	}
	public void setData(List<FbLikesData> data){
		this.data = data;
	}
 	public FbLikesPaging getPaging(){
		return this.paging;
	}
	public void setPaging(FbLikesPaging paging){
		this.paging = paging;
	}
 	public FbLikesSummary getSummary(){
		return this.summary;
	}
	public void setSummary(FbLikesSummary summary){
		this.summary = summary;
	}
}
