package com.usamabennadeemspeechix.usama.speechix.addNewScript.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "scriptsDB.db";
    public static final String TABLE_SCRIPTS = "scripts";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCRIPT_NAME = "scriptname";
    public static final String COLUMN_SCRIPT_BODY = "scriptbody";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SCRIPTS_TABLE = "CREATE TABLE " +
                TABLE_SCRIPTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCRIPT_NAME + " TEXT, " +
                COLUMN_SCRIPT_BODY + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_SCRIPTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addScript(Script script) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCRIPT_NAME, script.getName());
        values.put(COLUMN_SCRIPT_BODY, script.getBody());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(TABLE_SCRIPTS, null, values);
        sqLiteDatabase.close();
    }

    public List<String> getAllScriptNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + COLUMN_SCRIPT_NAME + " from " + TABLE_SCRIPTS, null);

        List<String> scriptNames = new ArrayList<>();
        if (cursor.moveToFirst()) {
            scriptNames.add(cursor.getString(cursor.getColumnIndex(COLUMN_SCRIPT_NAME)));
            while (cursor.moveToNext()) {
                scriptNames.add(cursor.getString(cursor.getColumnIndex(COLUMN_SCRIPT_NAME)));
            }
        }
        cursor.close();
        db.close();
        return scriptNames;
    }

    public Script getScript(String scriptName) {
        String query = "SELECT * FROM " + TABLE_SCRIPTS + " WHERE " + COLUMN_SCRIPT_NAME + " = \"" + scriptName + "\"";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        Script script = new Script();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            script.set_id(cursor.getInt(0));
            script.setName(cursor.getString(1));
            script.setBody(cursor.getString(2));
            cursor.close();
        } else {
            script = null;
        }
        sqLiteDatabase.close();
        return script;
    }

    public void updateRow(int _id, String name, String body) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String updateBodyQuery = "update " + TABLE_SCRIPTS + " set " + COLUMN_SCRIPT_BODY + " = " + "'" + body + "', " + COLUMN_SCRIPT_NAME + " = " + "'" + name + "'" + " where " + COLUMN_ID + "  = " + "'" + _id + "'";
        sqLiteDatabase.execSQL(updateBodyQuery);
        sqLiteDatabase.close();
    }

    public boolean deleteScript(String scriptName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        boolean deleted = sqLiteDatabase.delete(TABLE_SCRIPTS, COLUMN_SCRIPT_NAME + " = " + "'" + scriptName + "'", null) > 0;
        sqLiteDatabase.close();
        return deleted;
    }

    public boolean checkIsDataAlreadyInDBorNot(String fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select " + COLUMN_SCRIPT_NAME + " from " + TABLE_SCRIPTS + " where " + COLUMN_SCRIPT_NAME + " = " + "\'" + fieldValue + "\'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            sqldb.close();
            return false;
        }
        cursor.close();
        sqldb.close();
        return true;
    }

}
