
package son.nt.dota2.facebook.object;

import java.util.ArrayList;
import java.util.List;

public class FbComments{
   	private List<FbCmtData> data;
   	private FbCmtPaging paging;
   	private FbCmtSummary summary;
   	
   	public FbComments() {
   		data = new ArrayList<FbCmtData>();
   		paging = new FbCmtPaging();
   	}

 	public List<FbCmtData> getData(){
		return this.data;
	}
	public void setData(List<FbCmtData> data){
		this.data = data;
	}
 	public FbCmtPaging getPaging(){
		return this.paging;
	}
	public void setPaging(FbCmtPaging paging){
		this.paging = paging;
	}
 	public FbCmtSummary getSummary(){
		return this.summary;
	}
	public void setSummary(FbCmtSummary summary){
		this.summary = summary;
	}
}
