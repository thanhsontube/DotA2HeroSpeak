package son.nt.dota2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by Sonnt on 8/7/15.
 */
public class SoundUtils {
    public static void setRingTone(Context context, SpeakDto dto) {
        try {
            File f1 = new File(ResourceManager.getInstance().getPathAudio(dto.link, dto.heroId));
            File f2 = new File(ResourceManager.getInstance().getPathRingtone(dto.link, dto.heroId));
            FileUtil.copyFile(f1.getPath(), f2.getPath());
            String mimeType = "audio/mp3";

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, f2.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, dto.text);
            values.put(MediaStore.MediaColumns.SIZE, f2.length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

            values.put(MediaStore.Audio.Media.ARTIST, dto.heroId);
            values.put(MediaStore.Audio.Media.IS_RINGTONE,
                    true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_MUSIC, false);


            // Insert it into the database
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(f2.getAbsolutePath());
            final Uri newUri = context.getContentResolver().insert(uri, values);

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);


        } catch (Exception e) {
            Logger.error("SoundUtils", ">>>" + "ERROR set ringtone:" + e.toString());
            e.printStackTrace();
        }
    }

    public static void setRingTone(Context context, ISound dto) {
        try {
            File f1 = new File(ResourceManager.getInstance().getPathSound(dto.getLink(), dto.getSavedRootFolder(), dto.getSavedBranchFolder()));
            File f2 = new File(ResourceManager.getInstance().getPathRingtone(dto.getLink(), dto.getSavedRootFolder()));
            FileUtil.copyFile(f1.getPath(), f2.getPath());
            String mimeType = "audio/mp3";

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, f2.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, dto.getTitle());
            values.put(MediaStore.MediaColumns.SIZE, f2.length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

            values.put(MediaStore.Audio.Media.ARTIST, dto.getTitle());
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_MUSIC, false);


            // Insert it into the database
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(f2.getAbsolutePath());
            final Uri newUri = context.getContentResolver().insert(uri, values);

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
            Toast.makeText(context, "Set [" + dto.getTitle() + "] as a ringtone", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Logger.error("SoundUtils", ">>>" + "ERROR set ringtone:" + e.toString());
            e.printStackTrace();
        }
    }

    public static void setNotificationSound(Context context, ISound dto) {
        try {
            File f1 = new File(ResourceManager.getInstance().getPathSound(dto.getLink(), dto.getSavedRootFolder(), dto.getSavedBranchFolder()));
            File f2 = new File(ResourceManager.getInstance().getPathNotification(dto.getLink(), dto.getSavedRootFolder()));
            Logger.debug("TAG", ">>>Notification f1:" + f1.getPath() + ";length f1:" + f1.length() + ">>>" + "f2:" + f2.getPath());
            FileUtil.copyFile(f1.getPath(), f2.getPath());
            String outPath = f2.getPath();
            String mimeType = "audio/mp3";

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, outPath);
            values.put(MediaStore.MediaColumns.TITLE, dto.getTitle());
            values.put(MediaStore.MediaColumns.SIZE, new File(outPath).length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

            values.put(MediaStore.Audio.Media.ARTIST, dto.getTitle());
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
            // Insert it into the database
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
            final Uri newUri = context.getContentResolver().insert(uri, values);

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, newUri);
            Toast.makeText(context, "Set [" + dto.getTitle() + "] as a notification", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Logger.error("SoundUtils", ">>>" + "ERROR set setNotificationSound:" + e.toString());
            e.printStackTrace();
        }
    }

    public static void setAlarmSound(Context context, ISound dto) {
        try {
            File f1 = new File(ResourceManager.getInstance().getPathSound(dto.getLink(), dto.getSavedRootFolder(), dto.getSavedBranchFolder()));
            File f2 = new File(ResourceManager.getInstance().getPathAlarm(dto.getLink(), dto.getSavedRootFolder()));

            FileUtil.copyFile(f1.getPath(), f2.getPath());
            String outPath = f2.getPath();
            String mimeType = "audio/mp3";

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, outPath);
            values.put(MediaStore.MediaColumns.TITLE, dto.getTitle());
            values.put(MediaStore.MediaColumns.SIZE, new File(outPath).length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

            values.put(MediaStore.Audio.Media.ARTIST, dto.getTitle());
            values.put(MediaStore.Audio.Media.IS_ALARM, true);
            // Insert it into the database
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
            final Uri newUri = context.getContentResolver().insert(uri, values);

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, newUri);
            Toast.makeText(context, "Set [" + dto.getTitle() + "] as an alarm", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
