package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable, WriteableToSQL {

    private String mUID;
    private String mName;
    private String mNickname;
    private int mScore;
    private ArrayList<User> mFriends;


    public User() {
    }

    public User(User user) {
        this.mUID = user.getmUID();
        this.mName = user.getmName();
        this.mNickname = user.getmNickname();
        this.mScore = user.getmScore();
        this.mFriends = user.getmFriends();
    }

    public User(String uid, String name, String nickname) {
        this.mUID = uid;
        this.mName = name;
        this.mNickname = nickname;
        this.mScore = 0;
        this.mFriends = new ArrayList<>();
    }

    public User(String name, String nickname, int score) {
        this.mName = name;
        this.mNickname = nickname;
        this.mScore = score;
        this.mFriends = new ArrayList<>();
    }

    private User(Parcel source) {
        this.mUID = source.readString();
        this.mName = source.readString();
        this.mNickname = source.readString();
        this.mScore = source.readInt();
        this.mFriends = source.readArrayList(User.class.getClassLoader());
    }

    public String getmUID() {
        return mUID;
    }

    public void setmUID(String uid) {
        this.mUID = uid;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        this.mName = name;
    }

    public String getmNickname() {
        return mNickname;
    }

    public void setmNickname(String nickname) {
        this.mNickname = nickname;
    }

    public int getmScore() {
        return mScore;
    }

    public void setmScore(int score) {
        this.mScore = score;
    }

    public ArrayList<User> getmFriends() {
        return mFriends;
    }

    public void setmFriends(ArrayList<User> friends) {
        this.mFriends = friends;
    }

    public void answeredCorrectly(int won) {
        this.mScore += won;
    }

    public void answeredWrong(int lost) {
        this.mScore -= lost;
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUID);
        dest.writeString(mName);
        dest.writeString(mNickname);
        dest.writeInt(mScore);
        dest.writeList(mFriends);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return mUID.equals(((User) obj).getmUID()) ? true : false;
        }
        return false;
    }
}
