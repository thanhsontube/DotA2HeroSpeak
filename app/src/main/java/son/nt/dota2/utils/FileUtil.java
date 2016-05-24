package son.nt.dota2.utils;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;


public class FileUtil {

    public static void saveObject(Context context, AObject data, String name) throws IOException {
        File woFile = new File(ResourceManager.getInstance().getFolderObject(), name);
        if (woFile.exists()) {
            woFile.delete();
        }
        woFile.createNewFile();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(woFile));
        oos.writeObject(data);
        oos.close();
    }

    public static AObject getObject(Context context, String name) throws IOException, ClassNotFoundException {
        File woFile = new File(ResourceManager.getInstance().getFolderObject(), name);
        if (!woFile.exists()) {
            return null;
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(woFile));
        AObject wo = ((AObject) ois.readObject());
        ois.close();
        return wo;
    }

    public static void saveAbilityObject (Context context, AObject data, String name) throws Exception {
        saveObject(context, data, "abi_" + name);
    }

    public static AObject getAbilityObject(Context context, String name) throws IOException, ClassNotFoundException {
        return getObject(context, "abi_" +name);
    }

    public static AObject getMusicPackObject(Context context) throws IOException, ClassNotFoundException {
        return getObject(context, "musicPackData.json");
    }

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
        File woFile = new File(ResourceManager.getInstance().getFolderHero(), File.separator + name);
        if (!woFile.exists()) {
            woFile.createNewFile();
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(woFile));
        oos.writeObject(data);
        oos.close();
    }

    public static HeroDto readHeroSpeak(Context context, String name) throws IOException, ClassNotFoundException {
        File woFile = new File(ResourceManager.getInstance().getFolderHero(), File.separator + name);
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
    public static void copy(Context context, String label, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    public static void setRingtone (Context context, String link, String name) {
        try {
            File ringtoneFile = new File(ResourceManager.getInstance().folderAudio, File.separator + FileUtil.createPathFromUrl(link).replace(".mp3", ".dat"));
            if (!ringtoneFile.exists()) {
                Toast.makeText(context, "RingtoneFile is not Available", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues content = new ContentValues();
            content.put(MediaStore.MediaColumns.DATA,ringtoneFile.getAbsolutePath());
            content.put(MediaStore.MediaColumns.TITLE, name);
            content.put(MediaStore.MediaColumns.SIZE, 25454);
            content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
            content.put(MediaStore.Audio.Media.ARTIST, name);
            content.put(MediaStore.Audio.Media.DURATION, 230);
            content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            content.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            content.put(MediaStore.Audio.Media.IS_ALARM, false);
            content.put(MediaStore.Audio.Media.IS_MUSIC, false);

            Uri uri = MediaStore.Audio.Media.getContentUriForPath(
                    ringtoneFile.getAbsolutePath());
            Uri newUri = context.getContentResolver().insert(uri, content);
            Log.i("","the ringtone uri is :"+newUri.getPath());
            RingtoneManager.setActualDefaultRingtoneUri(context,
                    RingtoneManager.TYPE_RINGTONE, newUri);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "RingtoneFile is not Available", Toast.LENGTH_SHORT).show();
        }

    }


    public static void copyAssets(Context context, String outPath) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        for(String filename : files) {
            try {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(filename);
                    File outFile = new File(outPath, filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                } catch(IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                }
                finally {
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
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static void copyFile(String inputPath,  String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist



            in = new FileInputStream(inputPath );
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }


}
