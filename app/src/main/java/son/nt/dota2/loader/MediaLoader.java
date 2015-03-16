package son.nt.dota2.loader;

import org.apache.http.client.methods.HttpUriRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.loader.base.ContentLoader;


public abstract class MediaLoader extends ContentLoader<File> {

    public MediaLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected File handleStream(InputStream in) throws IOException {
        try {
            File file = new File(ResourceManager.getInstance().folderAudio, File.separator + "file_temp");
            OutputStream outputStream = new FileOutputStream(file, true);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            in.close();
            return file;
        } catch (Exception e) {
        }
        return null;
    }

}
