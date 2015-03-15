package son.nt.dota2.loader.base;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

import son.nt.dota2.utils.FilterLog;


public abstract class ContentLoader<T> {

    protected static final long HOUR = 1 * 60 * 60 * 1000;
    private static final FilterLog log = new FilterLog(ContentLoader.class.getSimpleName());
    private final boolean useCache;
    private final MessageDigest mDigester;
    private HttpUriRequest request;
    private final AsyncTask<ContentManager, Void, T> task = new AsyncTask<ContentManager, Void, T>() {

        private Throwable error = null;

        @Override
        protected void onPreExecute() {
            if (isCancelled()) {
                return;
            }
            super.onPreExecute();
            onContentLoaderStart();
        }

        @Override
        protected T doInBackground(ContentManager... params) {
            if (isCancelled()) {
                return null;
            }
            final ContentManager manager = params[0];
            final DefaultHttpClient client = new DefaultHttpClient();
            try {
                T value = handleCache(manager);
                if (value != null) {
                    log.v("from cache: " + value);
                    return value;
                }

                HttpConnectionParams.setConnectionTimeout(client.getParams(), 45000);
                HttpConnectionParams.setSoTimeout(client.getParams(), 300000);

                final String auth = request.getURI().getUserInfo();
                if (auth != null) {
                    client.getCredentialsProvider().setCredentials(
                            AuthScope.ANY, new UsernamePasswordCredentials(auth));
                }
                log.d("ContentLoader request: " + request.getURI());
                return client.execute(request, new ResponseHandler<T>() {

                    @Override
                    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        final int stat = response.getStatusLine().getStatusCode();
                        log.v("status: " + stat + " " + getRequest().getURI().toASCIIString());
                        if (stat < 200 || stat >= 300) {
                            return null;
                        }
                        InputStream in = null;
                        try {
                            in = response.getEntity().getContent();

                            if (useCache) {
                                ByteArrayOutputStream data = new ByteArrayOutputStream();
                                try {
                                    int buf;
                                    while ((buf = in.read()) >= 0) {
                                        data.write(buf);
                                    }
                                } finally {
                                    if (in != null) {
                                        try {
                                            in.close();
                                        } catch (IOException e) {
                                        }
                                    }
                                    try {
                                        data.close();
                                    } catch (IOException e) {
                                    }
                                }
                                in = new ByteArrayInputStream(data.toByteArray());
                                synchronized (manager) {
                                    final String fname = getCacheFileName();
                                    log.v("put cache " + fname + " " + getRequest().getURI().toASCIIString());
                                    manager.putCache(fname, in);
                                }
                                in = new ByteArrayInputStream(data.toByteArray());
                            }
                            log.v("from network: " + getRequest().getURI().toASCIIString());
                            return handleStream(in);

                        } finally {
                            if (in != null) {
                                in.close();
                            }
                        }
                    }

                });
            } catch (Exception e) {
                error = e;
            } finally {
                request.abort();
                client.getConnectionManager().shutdown();
            }
            return null;
        }

        @Override
        protected void onPostExecute(T result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                return;
            }

            if (error != null) {
                onContentLoaderFailed(error);
            } else if (result == null) {
                onContentLoaderFailed(new NullPointerException("value is null"));
            } else {
                onContentLoaderSucceed(result);
            }
        }
    };

    public ContentLoader(boolean useCache) {
        this(null, useCache);
    }

    public ContentLoader(HttpUriRequest request, boolean useCache) {
        this.request = request;
        this.useCache = useCache;
        try {
            this.mDigester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // no occurs.
            throw new IllegalStateException(e);
        }
    }

    public abstract void onContentLoaderStart();

    public abstract void onContentLoaderSucceed(T entity);

    public abstract void onContentLoaderFailed(Throwable e);

    protected abstract T handleStream(InputStream in) throws IOException;

    public HttpUriRequest getRequest() {
        return request;
    }

    protected void setRequest(HttpUriRequest request) {
        this.request = request;
    }

    protected Executor getExecutor() {
//		return AsyncTask.SERIAL_EXECUTOR;
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    /* package */ boolean enableCache() {
        return useCache;
    }

    /* package */ public void execute(ContentManager manager) {
        task.executeOnExecutor(getExecutor(), manager);
    }

    /* package */ void cancel() {
        task.cancel(true);
    }

    public T handleCache(ContentManager manager) throws Exception {
        if (!useCache) {
            return null;
        }

        InputStream in = null;
        try {
            synchronized (manager) {
                final String key = getCacheFileName();
                final File f = manager.getCache(key);
                if (f == null) {
                    return null;
                }
                if (isValidCache(f) == false) {
                    log.v("invalid cache " + key);
                    manager.removeCache(key);
                    return null;
                }
                in = new BufferedInputStream(new FileInputStream(f));

                return handleStream(in);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    protected String getCacheFileName() {
        return md5sum(getRequest().getURI().toASCIIString());
    }

    protected boolean isValidCache(File file) {
        return file.exists() && file.length() > 0;
    }

    protected synchronized String md5sum(String s) {
        try {
            StringBuilder sb = new StringBuilder();
            mDigester.update(s.getBytes("ISO-8859-1"));
            for (byte b : mDigester.digest()) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            // no occurs.
            throw new IllegalStateException(e);
        }
    }
}
