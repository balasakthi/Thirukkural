package com.akh.balasakthi.thirukural.database;

import android.content.Context;
import android.util.Log;

import com.akh.balasakthi.thirukural.database.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by balasakthi on 8/4/17.
 */

public class CopyDB {
    public static boolean copyDataBase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.v(context.getPackageName(), "DB Copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
