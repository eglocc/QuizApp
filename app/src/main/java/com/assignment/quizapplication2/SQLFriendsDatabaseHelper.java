package com.assignment.quizapplication2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class SQLFriendsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Friends.db";
    private static final int DB_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FriendContractSQL.FriendEntry.TABLE_NAME + " (" +
                    FriendContractSQL.FriendEntry.COLUMN_NAME_UID + " TEXT PRIMARY KEY," +
                    FriendContractSQL.FriendEntry.COLUMN_NAME_REAL_NAME + " TEXT," +
                    FriendContractSQL.FriendEntry.COLUMN_NAME_NICKNAME + " TEXT," +
                    FriendContractSQL.FriendEntry.COLUMN_NAME_SCORE + " INTEGER);";

    private static final String SQL_ALTER_TABLE = "ALTER TABLE " + FriendContractSQL.FriendEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FriendContractSQL.FriendEntry.TABLE_NAME;

    private static final String SQL_SELECT_ALL = "SELECT * FROM " + FriendContractSQL.FriendEntry.TABLE_NAME;

    public SQLFriendsDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    private void updateMyDatabase(SQLiteDatabase db) {
        db.execSQL(SQL_ALTER_TABLE);
    }

    public void addFriend(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FriendContractSQL.FriendEntry.COLUMN_NAME_UID, user.getmUID());
        values.put(FriendContractSQL.FriendEntry.COLUMN_NAME_REAL_NAME, user.getmName());
        values.put(FriendContractSQL.FriendEntry.COLUMN_NAME_NICKNAME, user.getmNickname());
        values.put(FriendContractSQL.FriendEntry.COLUMN_NAME_SCORE, user.getmScore());
        db.insert(FriendContractSQL.FriendEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void removeFriend(User user) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = FriendContractSQL.FriendEntry.COLUMN_NAME_UID + " = ?";
        String[] selectionArgs = {};
        db.delete(FriendContractSQL.FriendEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void updateFriend(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FriendContractSQL.FriendEntry.COLUMN_NAME_UID, user.getmUID());
        values.put(FriendContractSQL.FriendEntry.COLUMN_NAME_REAL_NAME, user.getmName());
        values.put(FriendContractSQL.FriendEntry.COLUMN_NAME_NICKNAME, user.getmNickname());
        values.put(FriendContractSQL.FriendEntry.COLUMN_NAME_SCORE, user.getmScore());
        String selection = FriendContractSQL.FriendEntry.COLUMN_NAME_UID + " = ?";
        String[] selectionArgs = {user.getmUID()};
        db.update(FriendContractSQL.FriendEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    /**
     * Get friends from SQLDatabase according to given criteria
     *
     * @param selectionCriteria @Nullable Column name of the table, if null returns all friends the user has
     * @param selectionArg      @Nullable Searched attribute, if null returns all friends the user has
     * @return list of users according to given criteria
     */
    public ArrayList<User> getFriendsByCriteria(@Nullable String selectionCriteria, @Nullable String selectionArg) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> selectedFriends = new ArrayList<>();
        String[] projection = {selectionCriteria};
        String selection = selectionCriteria + " = ?";
        String[] selectionArgs = {selectionArg};
        String sortOrder = selectionCriteria + "ASC";

        Cursor cursor;
        if (selectionCriteria != null && selectionArg != null) {
            cursor = db.query(
                    FriendContractSQL.FriendEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
        } else {
            cursor = db.rawQuery(SQL_SELECT_ALL, null);
        }

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setmUID(cursor.getString(0));
                    user.setmName(cursor.getString(1));
                    user.setmNickname(cursor.getString(2));
                    user.setmScore(Integer.parseInt(cursor.getString(3)));
                    selectedFriends.add(user);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return selectedFriends;
    }

    public int getFriendsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL, null);
        cursor.close();
        db.close();

        return cursor.getCount();
    }
}
