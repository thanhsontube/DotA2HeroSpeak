package son.nt.dota2.facebook;

import android.content.Context;
import android.os.Bundle;

import com.facebook.HttpMethod;

public abstract class FbLoaderPost<T> extends FbLoader<T>{
	public FbLoaderPost(Context context, String graphpath, Bundle params) {
		super(context, graphpath, params, HttpMethod.POST);
	}
}
