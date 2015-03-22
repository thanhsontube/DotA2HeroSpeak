package son.nt.dota2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyData {
    MyDataHelper helper;
    SQLiteDatabase db;

    public MyData(Context context) {
        helper = new MyDataHelper(context);
        try {
            db = helper.getWritableDatabase();
        } catch (Exception e) {
            db = helper.getReadableDatabase();
        }
    }

    public boolean insertData(String text) {
        try {
            ContentValues values = new ContentValues();
            values.put("value", text);
            db.insertOrThrow(MyDataHelper.DATABASE_TABLE, null, values);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor getData(String tableName) {
        return db.query(tableName, null, null, null, null, null, null);
    }

    public void deleteRow(Cursor cursor) {
        String whereClause = "_id = ?";
        String[] whereArgs = new String[] { cursor.getString(0) };
        db.delete(MyDataHelper.DATABASE_TABLE, whereClause, whereArgs);
    }

    public void deleteRow(String message) {
        String whereClause = "value = ?";
        String[] whereArgs = new String[] { message };
        db.delete(MyDataHelper.DATABASE_TABLE, whereClause, whereArgs);
    }

    public void deleteRow(String tableName, String message) {
        String whereClause = "value = ?";
        String[] whereArgs = new String[] { message };
        db.delete(tableName, whereClause, whereArgs);
    }

    // TABLE FAVORITE
    public boolean addFavorite(String text) {
        try {
            ContentValues values = new ContentValues();
            values.put("value", text);
            db.insertOrThrow(MyDataHelper.DATABASE_TABLE_FAVORITE, null, values);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFavorite(String text) {
        String whereClause = "value = ?";
        String[] whereArgs = new String[] { text };
        Cursor cursor = db.query(MyDataHelper.DATABASE_TABLE_FAVORITE, null, whereClause, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public void removeFavorite(String text) {
        String whereClause = "value = ?";
        String[] whereArgs = new String[] { text };
        db.delete(MyDataHelper.DATABASE_TABLE_FAVORITE, whereClause, whereArgs);

    }

}
