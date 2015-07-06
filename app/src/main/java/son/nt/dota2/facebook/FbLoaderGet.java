package son.nt.dota2.facebook;

import android.content.Context;
import android.os.Bundle;

import com.facebook.HttpMethod;

public abstract class FbLoaderGet<T> extends FbLoader<T> {
    public FbLoaderGet(Context context, String graphpath, Bundle params) {
        super(context, graphpath, params, HttpMethod.GET);
    }
}
