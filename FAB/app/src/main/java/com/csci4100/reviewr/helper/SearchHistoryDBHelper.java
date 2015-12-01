package com.csci4100.reviewr.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mattmili
 * Search History Database Helper
 */
public class SearchHistoryDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_FILENAME = "searchHistory.db";
    public static final String TABLE_NAME = "SearchHistory";

    // don't forget to use the column name '_id' for your primary key
    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "(" +
            "  _id integer primary key autoincrement, " +
            "  searchQuery text not null" +
            ")";
    public static final String DROP_STATEMENT = "DROP TABLE " + TABLE_NAME;

    public SearchHistoryDBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(DROP_STATEMENT);
        database.execSQL(CREATE_STATEMENT);
    }

    // add new query
    public InputItem createitem(String query){

        InputItem item = new InputItem(query);

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // insert the data into the database
        ContentValues values = new ContentValues();

        values.put("searchQuery", item.query());
        long id = database.insert(TABLE_NAME, null, values);

        // assign the Id of the new database row as the Id of the object
        item.setID(id);

        return item;
    }

    // delete an item
    public boolean deleteProduct(long id){
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the contact
        int numRowsAffected = database.delete(TABLE_NAME, "_id = ?", new String[] { "" + id });

        Log.i("DatabaseAccess", "deleteItem(" + id + "):  numRowsAffected: " + numRowsAffected);

        // verify that the contact was deleted successfully
        return (numRowsAffected == 1);
    }

    // delete all items
    public void deleteAllItems() {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the contact
        int numRowsAffected = database.delete(TABLE_NAME, "", new String[] {});

        Log.i("DatabaseAccess", "deleteAlItems():  numRowsAffected: " + numRowsAffected);
    }

    // Find all Items
    public ArrayList<InputItem> getAllitems() {
        ArrayList<InputItem> items = new ArrayList<InputItem>();

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the contact from the database
        String[] columns = new String[] { "_id", "searchQuery" };
        Cursor cursor = database.query(TABLE_NAME, columns, "", new String[]{}, "", "", "");
        cursor.moveToFirst();
        do {
            // collect the contact data, and place it into a contact object
            long id = Long.parseLong(cursor.getString(0));
            String query = cursor.getString(1);
            InputItem item = new InputItem(query);
            item.setID(id);

            // add the current contact to the list
            items.add(item);

            // advance to the next row in the results
            cursor.moveToNext();
        } while (!cursor.isAfterLast());

        Log.i("DatabaseAccess", "getAllProducts():  num: " + items.size());

        return items;
    }

    // check if table is empty
    public boolean isEmpty(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT count(*) FROM "+TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);

        if(count>0){
            return false;
        }

        return true;

    }
}
