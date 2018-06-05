package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DBNAME = "AppMaps.db";
    private static final int DB_VERSION = 2;
    public Database(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS localizacao(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitude DOUBLE," +
                "longitude DOUBLE, " +
                "descricao VARCHAR" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS localizacao");
        onCreate(db);

    }
}
