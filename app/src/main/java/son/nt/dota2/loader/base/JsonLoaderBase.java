package son.nt.dota2.loader.base;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import son.nt.dota2.utils.TsLog;

abstract public class JsonLoaderBase<T> extends ContentLoader<T> {

    private final TsLog log = new TsLog(JsonLoaderBase.class.getSimpleName());

    public JsonLoaderBase(boolean useCache) {
        super(useCache);
    }

    public JsonLoaderBase(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    public JsonLoaderBase(URI uri, boolean useCache) {
        super(new HttpGet(uri), useCache);
    }

    @Override
    public T handleStream(InputStream in) throws IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int buf = -1;
            while ((buf = br.read()) >= 0) {
                sb.append((char) buf);
            }
//            log.d(">>>JsonLoaderBase:" + sb.toString());
            return handleJson(new JSONObject(sb.toString()));
        } catch (JSONException e) {
            log.e(e.getMessage(), e);
            throw new IOException(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract T handleJson(JSONObject json) throws IOException, JSONException;
}
