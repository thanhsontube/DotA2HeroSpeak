package son.nt.dota2.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;

/**
 * Created by tiennt on 2/5/15.
 */
public class FileUtil {

    public static void saveHeroList(Context context, HeroData data) throws IOException {
        File woFile = new File(ResourceManager.getInstance().fileHeroList);
        if (!woFile.exists()) {
            woFile.createNewFile();
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(woFile));
        oos.writeObject(data);
        oos.close();
    }

    public static HeroData readHeroList(Context context) throws IOException, ClassNotFoundException {
        File woFile = new File(ResourceManager.getInstance().fileHeroList);
        if (!woFile.exists()) {
            return null;
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(woFile));
        HeroData wo = ((HeroData) ois.readObject());
        ois.close();
        return wo;
    }

    public static void saveHeroSpeak(Context context, HeroDto data, String name) throws IOException {
        File woFile = new File(ResourceManager.getInstance().folderHero, File.separator + name);
        if (!woFile.exists()) {
            woFile.createNewFile();
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(woFile));
        oos.writeObject(data);
        oos.close();
    }

    public static HeroDto readHeroSpeak(Context context, String name) throws IOException, ClassNotFoundException {
        File woFile = new File(ResourceManager.getInstance().folderHero, File.separator + name);
        if (!woFile.exists()) {
            return null;
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(woFile));
        HeroDto wo = ((HeroDto) ois.readObject());
        ois.close();
        return wo;
    }

    public static String createPathFromUrl(String url) {
        String path = url.replaceAll("[|?*<\":>+\\[\\]/']", "_");
        return path;
    }

    public static boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
