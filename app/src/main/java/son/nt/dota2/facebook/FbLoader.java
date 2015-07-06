package son.nt.dota2.facebook;

import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import son.nt.dota2.utils.TsLog;

public abstract class FbLoader<T> {
    private static final String TAG = "FbLoader";

    public abstract void onFbLoaderStart();

    public abstract void onFbLoaderSuccess(T entry);

    public abstract void onFbLoaderFail(Throwable e);

    TsLog log = new TsLog(TAG);

    private String grathPath;
    private Bundle params;
    private HttpMethod httpMethod;

    public FbLoader(Context context, String graphpath, Bundle params, HttpMethod httpMethod) {
        log.d("log>>>" + "graphpath:" + graphpath);
        this.grathPath = graphpath;
        this.params = params;
        this.httpMethod = httpMethod;
    }

    protected abstract T handleResult(GraphResponse response);

    public void request(FbLoaderManager fbLoaderManager) {
        if(params == null) {
            params = new Bundle();
        }
        onFbLoaderStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            onFbLoaderFail(new Exception("TOKEN FB is NULL"));
        }

        GraphRequest graphRequest = new GraphRequest(accessToken, grathPath, params, httpMethod, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                try {
                    T results = handleResult(graphResponse);
                    onFbLoaderSuccess(results);
                } catch (Exception e) {
                    e.printStackTrace();
                    onFbLoaderFail(e);
                }
            }
        });
        graphRequest.executeAsync();
    }
}
