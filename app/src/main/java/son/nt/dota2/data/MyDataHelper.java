package son.nt.dota2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "dota2.db";
    //favorite comment text
    public static final String DATABASE_TABLE = "table_saved";
    
    //favorite name of image was favorite
    public static final String DATABASE_TABLE_FAVORITE = "table_favorite";


    private static final String SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE table_favorite" + " (" + "_id"
            + " INTEGER PRIMARY KEY,"
            + "hero_name text, "
            +  "hero_link text,"
            + "save_time,"
            + "speak_content text,"
            + "speak_link text"
            + ")";

    private static final String SQL_DELETE_TABLE_FAVORITE = "DROP TABLE IF EXISTS table_favorite";

    public MyDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_FAVORITE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
