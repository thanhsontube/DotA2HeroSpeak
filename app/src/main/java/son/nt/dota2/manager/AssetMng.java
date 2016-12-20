package son.nt.dota2.manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by sonnt on 12/18/16.
 * copy file in asset folder to app folder.
 */

public class AssetMng implements IAssetMng {
    Context mContext;

    public AssetMng() {
    }

    public AssetMng(Context context) {
        mContext = context;
    }

    @Override
    public Observable<String> copyDataFromAsset(String assetFolder, String output) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    copyAssets(mContext, assetFolder, output);

                    subscriber.onNext(output);
                } catch (Exception e) {
                    Timber.d(">>>" + "Error copyDataFromAsset:" + e);
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public void copyAssets(Context context, String assetFolder, String outPath) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(assetFolder);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        for (String filename : files) {
            try {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(assetFolder + File.separator + filename);
                    File outFile = new File(outPath, filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                } catch (IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


}
