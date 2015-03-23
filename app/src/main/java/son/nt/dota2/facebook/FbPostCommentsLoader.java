package son.nt.dota2.facebook;

import android.content.Context;
import android.os.Bundle;

import com.facebook.Response;

public abstract class FbPostCommentsLoader extends FbLoaderPost<String> {

	public FbPostCommentsLoader(Context context, String graphpath, Bundle params) {
	    super(context, graphpath, params);
    }
	@Override
	protected String handleResult(Response response) {
	    return response.toString();
	}
}
