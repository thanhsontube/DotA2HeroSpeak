package son.nt.dota2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.SpeakDto;

/**
 * Created by Sonnt on 8/7/15.
 */
public class SoundUtils {
    public static void setRingTone (Context context, SpeakDto dto) {
        try {
            File f1 = new File(ResourceManager.getInstance().getPathAudio(dto.link, dto.heroId));
            File f2 = new File(ResourceManager.getInstance().getPathRingtone(dto.link, dto.heroId));
            f1.renameTo(f2);
            String outPath =  f2.getPath();
            String mimeType = "audio/mpeg";

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, outPath);
            values.put(MediaStore.MediaColumns.TITLE, dto.text);
            values.put(MediaStore.MediaColumns.SIZE, new File(outPath).length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

            values.put(MediaStore.Audio.Media.ARTIST, dto.heroId);
            values.put(MediaStore.Audio.Media.IS_RINGTONE,
                    true);


            // Insert it into the database
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
            final Uri newUri = context.getContentResolver().insert(uri, values);

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNotificationSound (Context context, SpeakDto dto) {
        try {
            File f1 = new File(ResourceManager.getInstance().getPathAudio(dto.link, dto.heroId));
            File f2 = new File(ResourceManager.getInstance().getPathNotification(dto.link, dto.heroId));
            f1.renameTo(f2);
            String outPath =  f2.getPath();
            String mimeType = "audio/mpeg";

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, outPath);
            values.put(MediaStore.MediaColumns.TITLE, dto.text);
            values.put(MediaStore.MediaColumns.SIZE, new File(outPath).length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

            values.put(MediaStore.Audio.Media.ARTIST, dto.heroId);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION,
                    true);


            // Insert it into the database
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
            final Uri newUri = context.getContentResolver().insert(uri, values);

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, newUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
