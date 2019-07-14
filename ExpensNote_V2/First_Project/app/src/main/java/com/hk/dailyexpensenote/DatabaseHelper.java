package com.hk.dailyexpensenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "Expenses.db";
    private static String TABLE_NAME = "Expense";

    public static String COL_TYPE = "Type";
    public static String COL_Amount = "Amount";
    public static String COL_Date = "Date";
    public static String COL_TIME = "Time";
    public static String COL_Document = "Document";
    public static String COL_ID = "Id";
    public static String TOT_EXPENSE="Total";

    private String DROAP_TABLE = "drop table if exists " + TABLE_NAME;
    private static String CREATE_TABLE = "create table " + TABLE_NAME + "( Id INTEGER primary key AUTOINCREMENT ,Type TEXT,Amount INTEGER,Date INTEGER,Time TEXT,Document TEXT)";
    private static int VERSION = 8;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL(DROAP_TABLE);
            onCreate(db);

        } catch (Exception e) {
            Toast.makeText(context, "exception : " + e, Toast.LENGTH_SHORT).show();
        }


    }

    public long insertData(String type, int amount, long date, String time, String document) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_Amount, amount);
        contentValues.put(COL_Date, date);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_Document, document);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long id = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return id;

    }

    //long fromDate,long toDate,String type
    public Cursor showAllData() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Expense ", null);
        return cursor;
    }


    //view total expense
    public Cursor showTotalExpense(long fromDate, long toDate, String type) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select sum(Amount) as Total from Expense where Type="+type+" AND Date between "+fromDate+" and "+toDate, null);
        return cursor;
    }




    //delete
    public void deleteData(int id) {
        getWritableDatabase().delete(TABLE_NAME, "Id=?", new String[]{String.valueOf(id)});
    }


    //update

    public void update(String id, String type, int amount, long date, String time) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_Amount, amount);
        contentValues.put(COL_Date, date);
        contentValues.put(COL_TIME, time);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "Id=?", new String[]{id});


    }


    //getDocument
    public Cursor getDocument(int id){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select "+COL_Document+" from Expense where "+COL_ID+"="+id, null);
        return cursor;
    }


    public void updateDocument(String id,String documentUrl){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_Document,documentUrl);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "Id=?", new String[]{id});
    }


}
