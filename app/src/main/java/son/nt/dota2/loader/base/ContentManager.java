package son.nt.dota2.loader.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import son.nt.dota2.utils.FilterLog;

public class ContentManager {

    private static final FilterLog log = new FilterLog(ContentManager.class.getSimpleName());

    private final Context context;

    private File mCacheDir;
    private LruCache<String, File> mCache;
    private LruCache<String, SoftReference<Bitmap>> mBitmapCache;

    public ContentManager(Context context, int cacheMB) throws IOException {
        this.context = context.getApplicationContext();
        this.mCacheDir = new File(context.getCacheDir(), ".cache");
        this.mCache = new LruCache<String, File>(cacheMB * 1024) {
            @Override
            protected int sizeOf(String key, File value) {
                return (int) (value.length() / 1024);
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, File oldValue, File newValue) {
                synchronized (this) {
                    log.v("entryRemoved evicted:" + evicted + " " + oldValue.getAbsolutePath());
                    oldValue.delete();
                }
            }
        };
        this.mBitmapCache = new LruCache<String, SoftReference<Bitmap>>(5 * 1024) {

            @Override
            protected int sizeOf(String key, SoftReference<Bitmap> value) {
                if (value.get() != null) {
                    return value.get().getByteCount();
                }
                return super.sizeOf(key, value);
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, SoftReference<Bitmap> oldValue, SoftReference<Bitmap> newValue) {
                // Why does it crash?
//                if (oldValue.get() != null && !oldValue.get().isRecycled()) {
//                    oldValue.get().recycle();
//                }
                mBitmapCache.remove(key);
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };

        initCache();
        for (File f : mCacheDir.listFiles()) {
            final String name = f.getName();
            if (".nomedia".equals(name) == false) {
                log.v("rebuild cache: " + name + " " + f.getAbsolutePath());
                mCache.put(name, f);
            }
        }
        log.v("init with " + cacheMB + "MB");
    }

    private void initCache() throws IOException {
        if (mCacheDir.exists() == false) {
            if (mCacheDir.mkdirs() == false) {
                throw new IOException("mkdir failed :" + mCacheDir.getAbsolutePath());
            }
            new File(mCacheDir, ".nomedia").createNewFile();
        }
    }

    /* package */ File getCache(String key) {
        synchronized (mCache) {
            return mCache.get(key);
        }
    }

    /* package */ void putCache(String key, InputStream in) throws IOException {

        initCache();
        BufferedOutputStream out = null;
        try {
            File f = new File(mCacheDir, key);
            out = new BufferedOutputStream(new FileOutputStream(f));
            int buf;
            while ((buf = in.read()) >= 0) {
                out.write(buf);
            }
            out.close();
            out = null;
            synchronized (mCache) {
                mCache.put(key, f);
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /* package */ void removeCache(String key) {
        synchronized (mCache) {
            log.v("remove key=" + key);
            mCache.remove(key);
        }
    }

    public synchronized Bitmap getCachedBitmap(String key) {
        SoftReference<Bitmap> softRef = mBitmapCache.get(key);
        if (softRef != null) {
            Bitmap bmp = softRef.get();
            if (bmp != null) {
                if (bmp.isRecycled()) {
                    mBitmapCache.remove(key);
                    bmp = null;
                }
                return bmp;
            }
        }
        return null;
    }

    public synchronized void putBitmapToCache(String key, Bitmap value) {
        mBitmapCache.put(key, new SoftReference<Bitmap>(value));
    }

    public void load(final ContentLoader<?> loader) {
        loader.execute(this);
    }

    public void cancel(ContentLoader<?> loader) {
        loader.cancel();
    }
}

