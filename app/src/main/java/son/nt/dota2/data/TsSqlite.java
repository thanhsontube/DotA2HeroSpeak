package son.nt.dota2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.SpeakDto;

public class TsSqlite {
    private MyDataHelper helper;
    private SQLiteDatabase database;
    static TsSqlite instance = null;

    public static String TABLE = MyDataHelper.DATABASE_TABLE_FAVORITE;

    public static void createInstance(Context context) {
        instance = new TsSqlite(context);
    }

    public static TsSqlite getInstance() {
        return instance;
    }

    private TsSqlite(Context context) {
        try {
            helper = new MyDataHelper(context);
            database = helper.getWritableDatabase();
        } catch (Exception e) {
            database = helper.getReadableDatabase();
        }
    }

    /*
    public int no;
    public String heroId;
    public String voiceGroup;
    public String link;
    public String text;
    public String rivalImage;
    public String rivalName;
     */
    public long insert(SpeakDto dto) {
        if (isInsert(dto.link)) {
            return -2;
        }
        ContentValues values = new ContentValues();
        values.put("no", dto.no);
        values.put("heroID", dto.heroId);
        values.put("voiceGroup", dto.voiceGroup);
        values.put("link", dto.link);
        values.put("text", dto.text);
        values.put("rivalImage", dto.rivalImage);
        values.put("rivalName", dto.rivalName);
        values.put("saveTime", String.valueOf(System.currentTimeMillis()));
        return  database.insert(TABLE, null, values);
    }

    public long insert(SaveDto dto) {
        if (isInsert(dto)) {
            return -2;
        }
        ContentValues values = new ContentValues();
        values.put("hero_name", dto.heroName);
        values.put("hero_link", dto.heroLink);
        values.put("speak_content", dto.speakContent);
        values.put("speak_link", dto.speakLink);
        values.put("save_time", dto.saveTime);
        return database.insert(TABLE, null, values);
    }

    public boolean isInsert(String link) {
        try {
            String selection = "link = ?";
            String[] selectionArgs = {link};
            Cursor cursor = database.query(TABLE, null, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int remove(String link) {
        try {
            String selection = "link = ?";
            String[] selectionArgs = {link};
            return database.delete(TABLE, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;

    }

    public boolean isInsert(SaveDto dto) {
        try {
            String selection = "link = ?";
            String[] selectionArgs = {dto.speakContent};
            Cursor cursor = database.query(TABLE, null, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int remove(SaveDto dto) {
        try {
            String selection = "link = ?";
            String[] selectionArgs = {dto.speakContent};
            return database.delete(TABLE, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;

    }

    public int delete(int id) {
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        return database.delete(TABLE, selection, selectionArgs);
    }

    public List<SaveDto> getList() {
        List<SaveDto> list = new ArrayList<>();
        Cursor cursor = database.query(TABLE, null, null, null, null, null, null);
        SaveDto dto;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String heroName = cursor.getString(cursor.getColumnIndex("hero_name"));
                String heroLink = cursor.getString(cursor.getColumnIndex("hero_link"));
                String speakContent = cursor.getString(cursor.getColumnIndex("speak_content"));
                String speakLink = cursor.getString(cursor.getColumnIndex("speak_link"));
                String saveTime = cursor.getString(cursor.getColumnIndex("save_time"));
                dto = new SaveDto(heroName, heroLink, speakContent, speakLink, saveTime);
                list.add(dto);
            } while (cursor.moveToNext());
        }
        return list;
    }

     /*
    public int no;
    public String heroId;
    public String voiceGroup;
    public String link;
    public String text;
    public String rivalImage;
    public String rivalName;
     */

    public List<SpeakDto> getPlaylist() {
        List<SpeakDto> list = new ArrayList<>();
        Cursor cursor = database.query(TABLE, null, null, null, null, null, null);
        SpeakDto dto;
        int i = 1;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String heroID = cursor.getString(cursor.getColumnIndex("heroID"));
                String voiceGroup = cursor.getString(cursor.getColumnIndex("voiceGroup"));
                String link = cursor.getString(cursor.getColumnIndex("link"));
                String text = cursor.getString(cursor.getColumnIndex("text"));
                String rivalImage = cursor.getString(cursor.getColumnIndex("rivalImage"));
                String rivalName = cursor.getString(cursor.getColumnIndex("rivalName"));
                dto = new SpeakDto();
                dto.heroId = heroID;
                dto.voiceGroup = voiceGroup;
                dto.link = link;
                dto.text = text;
                dto.rivalImage = rivalImage;
                dto.rivalName = rivalName;
                dto.no = i ++;
                list.add(dto);
            } while (cursor.moveToNext());
        }
        return list;
    }


}
