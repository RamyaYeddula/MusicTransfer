package com.example.Ramya.MusicTransfer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bluetoothDataTransferManager";

    // Contacts table name
    public static final String TABLE_MUSIC = "Music";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "music_name";
    private static final String KEY_OWNER = "owner";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MUSIC_TABLE = "CREATE TABLE " + TABLE_MUSIC + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_OWNER + " TEXT" + ")";
        db.execSQL(CREATE_MUSIC_TABLE);

        createGraphTable(db);
    }

    public void createGraphTable(SQLiteDatabase db){
        String query = "CREATE TABLE IF NOT EXISTS " + GlobalConstants.GRAPH_TABLE_NAME + "(" +
                GlobalConstants.GRAPH_COL_SOURCE + " TEXT PRIMARY KEY, " +
                GlobalConstants.GRAPH_COL_NEIGHBORS + " TEXT)";
        Log.d(GlobalConstants.TAG_DB, "================= Graph table create query: " + query);
        db.execSQL(query);

        Log.d(GlobalConstants.TAG_DB, "Graph table created");

        query = "SELECT * FROM " + GlobalConstants.GRAPH_TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() > 0){
            // Delete all rows from Graph table.
            query = "DELETE FROM " + GlobalConstants.GRAPH_TABLE_NAME;
            db.execSQL(query);
            Log.d(GlobalConstants.TAG_DB, "Data deleted from Graph table");
        }
        populateGraphTable(db);
        Log.d(GlobalConstants.TAG_DB, "Graph table populated");
    }

    public void createWeightedGraphTable(SQLiteDatabase db){
        String query = "CREATE TABLE IF NOT EXISTS " + GlobalConstants.WEIGHTED_GRAPH_TABLE + "(" +
                GlobalConstants.WEIGHTED_GRAPH_COL_ID + " TEXT PRIMARY KEY, " +
                GlobalConstants.WEIGHTED_GRAPH_COL_SOURCE + " TEXT, " +
                GlobalConstants.WEIGHTED_GRAPH_COL_DEST + " TEXT, " +
                GlobalConstants.WEIGHTED_GRAPH_COL_WEIGHT + " INTEGER)";
        Log.d(GlobalConstants.TAG_DB, "================= WeightedGraph table create query: " + query);
        db.execSQL(query);

        Log.d(GlobalConstants.TAG_DB, "WeightedGraph table created");

        query = "SELECT * FROM " + GlobalConstants.WEIGHTED_GRAPH_TABLE;
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() > 0){
            // Delete all rows from Graph table.
            query = "DELETE FROM " + GlobalConstants.WEIGHTED_GRAPH_TABLE;
            db.execSQL(query);
            Log.d(GlobalConstants.TAG_DB, "Data deleted from WeightedGraph table");
        }
        populateWeightedGraphTable(db);
        Log.d(GlobalConstants.TAG_DB, "WeightedGraph table populated");
    }

    private void populateWeightedGraphTable(SQLiteDatabase db) {
        addWeightedGraphEntry(db, "MD", "M", "D", 3);
        addWeightedGraphEntry(db, "MN6", "M", "N6", 2);
        addWeightedGraphEntry(db, "DM", "D", "M", 3);
        addWeightedGraphEntry(db, "DN6", "D", "N6", 5);
        addWeightedGraphEntry(db, "DY", "D", "Y", 4);
        addWeightedGraphEntry(db, "N6M", "N6", "M", 2);
        addWeightedGraphEntry(db, "N6D", "N6", "D", 5);
        addWeightedGraphEntry(db, "N6N7", "N6", "N7", 1);
        addWeightedGraphEntry(db, "N7N6", "N7", "N6", 1);
        addWeightedGraphEntry(db, "N7Y", "N7", "Y", 3);
        addWeightedGraphEntry(db, "YD", "Y", "D", 4);
        addWeightedGraphEntry(db, "YN7", "Y", "N7", 3);
    }

    private void addWeightedGraphEntry(SQLiteDatabase db, String ID, String source, String destination, int weight) {
        String query = "SELECT * FROM " + GlobalConstants.WEIGHTED_GRAPH_TABLE +
                " WHERE " + GlobalConstants.WEIGHTED_GRAPH_COL_ID + "='" + ID + "'";
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() > 0){
            query = "UPDATE " + GlobalConstants.WEIGHTED_GRAPH_TABLE +
                    " SET " + GlobalConstants.WEIGHTED_GRAPH_COL_WEIGHT + "=" + weight +
                    " WHERE " + GlobalConstants.GRAPH_COL_SOURCE + "='" + source +"'" +
                    " AND " + GlobalConstants.WEIGHTED_GRAPH_COL_DEST + "='" + destination + "'";
        } else {
            query = "INSERT INTO " + GlobalConstants.WEIGHTED_GRAPH_TABLE +
                    " (" + GlobalConstants.WEIGHTED_GRAPH_COL_SOURCE + ", " + GlobalConstants.WEIGHTED_GRAPH_COL_DEST + ", "
                    + GlobalConstants.WEIGHTED_GRAPH_COL_WEIGHT + ", " + GlobalConstants.WEIGHTED_GRAPH_COL_ID + ")" +
                    " VALUES ('" + source +"','" + destination + "', " + weight + ",'" + ID + "')";
        }
        Log.d(GlobalConstants.TAG_DB, query);
        db.execSQL(query);
    }

    private void populateGraphTable(SQLiteDatabase db) {
        addGraphEntry(db, "M", "N7|D");
        addGraphEntry(db, "N6", "N7|D|Y");
        addGraphEntry(db, "D", "M|N6");
        addGraphEntry(db, "N7", "N6|M");
        addGraphEntry(db, "Y", "N6");
    }

    private void addGraphEntry(SQLiteDatabase db, String node, String neighbors) {
        String query = "SELECT * FROM " + GlobalConstants.GRAPH_TABLE_NAME +
                " WHERE " + GlobalConstants.GRAPH_COL_SOURCE + "='" + node + "'";
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() > 0){
            query = "UPDATE " + GlobalConstants.GRAPH_TABLE_NAME +
                    " SET " + GlobalConstants.GRAPH_COL_NEIGHBORS + "='" + neighbors + "' " +
                    " WHERE " + GlobalConstants.GRAPH_COL_SOURCE + "='" + node +"'";
        } else {
            query = "INSERT INTO " + GlobalConstants.GRAPH_TABLE_NAME +
                    " VALUES ('" + node +"','" + neighbors + "')";
        }
        Log.d(GlobalConstants.TAG_DB, "");
        db.execSQL(query);
    }

    public String getNeighborsFromDB(String node, SQLiteDatabase db){
        String query = "SELECT " + GlobalConstants.GRAPH_COL_NEIGHBORS +
                " FROM " + GlobalConstants.GRAPH_TABLE_NAME +
                " WHERE " + GlobalConstants.GRAPH_COL_SOURCE + "='" + node + "'";
        Log.d(GlobalConstants.TAG_DB, "#############################" + query);
        Cursor c = db.rawQuery(query, null);
        String result = null;
        Log.d(GlobalConstants.TAG_DB, "Total rows returned: " + c.getCount());
        if(c.moveToFirst())
            result = c.getString(c.getColumnIndex(GlobalConstants.GRAPH_COL_NEIGHBORS));
        return result;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);

        // Create tables again
        onCreate(db);
    }

    // Adding new Music
    public void addMusic(Music music) {
        SQLiteDatabase db = this.getWritableDatabase();

        String checkMusicQuery = "SELECT * FROM " + TABLE_MUSIC +
                " WHERE " + KEY_NAME + "='" + music.getName() + "' ";

        Cursor cursor = db.rawQuery(checkMusicQuery, null);
        Log.d("Database", "Number of entries in Music table: " + cursor.getCount());
        if(cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, music.getName()); // Contact Name
            values.put(KEY_OWNER, music.getOwner()); // Contact Phone Number

            // Inserting Row
            db.insert(TABLE_MUSIC, null, values);
        }
        cursor.close();
        db.close(); // Closing database connection

    }

    // Getting All Music
    public List<Music> getAllMusic() {
        List<Music> musicList = new ArrayList<Music>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MUSIC;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Music music = new Music();
                music.setID(Integer.parseInt(cursor.getString(0)));
                music.setName(cursor.getString(1));
                music.setOwner(cursor.getString(2));
                // Adding contact to list
                musicList.add(music);
            } while (cursor.moveToNext());
        }

        // return contact list
        return musicList;
    }



}
