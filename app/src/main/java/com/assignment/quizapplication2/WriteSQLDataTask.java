package com.assignment.quizapplication2;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

public class WriteSQLDataTask extends AsyncTask<WriteableToSQL, Void, Boolean> {

    private Context mContext;
    private TaskType mTaskType;

    enum TaskType {ADD, REMOVE, UPDATE}

    public WriteSQLDataTask(Context c, TaskType type) {
        this.mContext = c;
        this.mTaskType = type;
    }

    @Override
    protected Boolean doInBackground(WriteableToSQL... objects) {
        SQLFriendsDatabaseHelper friendsDatabaseHelper = new SQLFriendsDatabaseHelper(mContext);
        if (objects instanceof User[]) {
            for (User friend : (User[]) objects) {
                try {
                    if (mTaskType == TaskType.ADD) {
                        friendsDatabaseHelper.addFriend(friend);
                    } else if (mTaskType == TaskType.REMOVE) {
                        friendsDatabaseHelper.removeFriend(friend);
                    } else if (mTaskType == TaskType.UPDATE) {
                        friendsDatabaseHelper.updateFriend(friend);
                    }
                } catch (SQLiteException e) {
                    return false;
                }
            }
            //TODO writeCategoriesTOSQL
        } else if (objects.getClass().equals(Category.class)) {
            // Do something
        }

        return true;
    }
}
