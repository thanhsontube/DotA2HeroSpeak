package son.nt.dota2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

    public long insert(SaveDto dto) {
        if(isInsert(dto)) {
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

    public boolean isInsert (SaveDto dto) {
        String selection = "speak_content = ?";
        String []selectionArgs = {dto.speakContent};
        Cursor cursor = database.query(TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public int delete(int id) {
        String selection = "_id = ?";
        String[] selectionArgs = new String[] { String.valueOf(id) };
        return database.delete(TABLE, selection, selectionArgs);
    }

    public List<SaveDto> getList () {
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


}
