package com.akh.balasakthi.thirukural.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.akh.balasakthi.thirukural.model.Thirukural;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by balasakthi on 12/2/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "thirukural.db";
    public static final String DBLOCATION = "/data/data/com.akh.balasakthi.thirukural/databases/";
    private Context context;
    private SQLiteDatabase database;
    private int favVal;
    private String chapterTitle;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDatabase() {
        String dbPath = context.getDatabasePath(DBNAME).getPath();
        if (database != null && database.isOpen()) {
            return;
        }
        database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (database != null) {
            database.close();
        }
    }

    //Getting the kural for 133 chapters

    public List<Thirukural> getkuralList(int from, int to) {
        Thirukural thirukural = null;
        List<Thirukural> thirukuralList = new ArrayList<>();
        openDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM kural WHERE kural_id >=" + from + " AND kural_id <=" + to, null);
        // Cursor cursor=database.rawQuery("SELECT * FROM kural", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            thirukural = new Thirukural(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            thirukuralList.add(thirukural);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return thirukuralList;
    }


    //Getting the Favourite kural

    public List<Thirukural> getFavKuralList() {
        Thirukural thirukural = null;
        List<Thirukural> thirukuralList = new ArrayList<>();
        openDatabase();
       // Cursor cursor = database.rawQuery("SELECT * FROM kural WHERE favourite = 1", null);

        Cursor cursor = database.rawQuery("SELECT * FROM kural left outer join chapters, parts, sections on chapters.chapter_id = kural.chapter_id and " +
                "chapters.part_id = parts.part_id and parts.section_id = sections.section_id WHERE favourite = 1", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          //  thirukural = new Thirukural(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            thirukural = new Thirukural(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10),
                    cursor.getString(11), cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getString(15), cursor.getString(16),
                    cursor.getInt(17), cursor.getString(18), cursor.getString(19));

            thirukuralList.add(thirukural);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return thirukuralList;
    }


    public Cursor getFavourite(int rowId) throws SQLException {
        openDatabase();
        Cursor mCursor = database.rawQuery("SELECT favourite FROM kural WHERE kural_id =" + rowId, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int getFavouriteVal(int rowId) throws SQLException {
        openDatabase();
        Cursor mCursor = database.rawQuery("SELECT favourite FROM kural WHERE kural_id =" + rowId, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            favVal = mCursor.getInt(mCursor.getColumnIndex("favourite"));
        }
        mCursor.close();
        closeDatabase();
        return favVal;
    }


    // Updating the favourite kural
    public void updateFavourite(int kuralID, int favourite) {
        openDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("favourite", favourite);

        int affectedRows = database.update("kural", values,
                "kural_id" + "=?", new String[]{String.valueOf(kuralID)});

        Log.d("KuralID,fav", kuralID + ":" + favourite);
        Log.d("affected rows:", String.valueOf(affectedRows));

    }


    // Removing the favourite kural Updates favourite = 0
    public void removeFavourite() {
        openDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("favourite", 0);

        int affectedRows = database.update("kural", values,
                "favourite" + "=?", new String[]{String.valueOf(1)});

        Log.d("affected rows:", String.valueOf(affectedRows));

    }


    // Setting all kUral as favourtes for testing
    public void addAllFavourites() {
        openDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("favourite", 1);

        int affectedRows = database.update("kural", values,
                "favourite" + "=?", new String[]{String.valueOf(0)});

        Log.d("affected rows:", String.valueOf(affectedRows));

    }

    //For testing updating the chapter value
    public void updateChapterID(int kural_id, int chapterID) {
        openDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("chapter_id", chapterID);

        int affectedRows = database.update("kural", values,
                "kural_id =?", new String[]{String.valueOf(kural_id)});

        Log.d("KuralID form", String.valueOf(kural_id));
        Log.d("affected rows:", String.valueOf(affectedRows));
    }


    //Getting the kural with chapter title

    public List<Thirukural> getkuralList2(int from, int to) {
        Thirukural thirukural = null;
        List<Thirukural> thirukuralList = new ArrayList<>();
        openDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM kural left outer join chapters, parts, sections on chapters.chapter_id = kural.chapter_id and " +
                "chapters.part_id = parts.part_id and parts.section_id = sections.section_id WHERE kural_id >=" + from + " AND kural_id <=" + to, null);
        // Cursor cursor=database.rawQuery("SELECT * FROM kural", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            thirukural = new Thirukural(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10),
                    cursor.getString(11), cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getString(15), cursor.getString(16),
                    cursor.getInt(17), cursor.getString(18), cursor.getString(19));
            thirukuralList.add(thirukural);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return thirukuralList;
    }
}
