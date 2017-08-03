package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

    private String mName;
    private int mCategoryId;
    private Quiz mQuiz;

    private Category() {
    }

    public Category(String name, int id) {
        this.mName = name;
        this.mCategoryId = id;
        this.mQuiz = new Quiz();
    }

    private Category(Parcel source) {
        mName = source.readString();
        mCategoryId = source.readInt();
        mQuiz = source.readParcelable(Quiz.class.getClassLoader());
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        this.mName = name;
    }

    public int getmCategoryId() {
        return mCategoryId;
    }

    public Quiz getmQuiz() {
        return mQuiz;
    }

    public void setmQuiz(Quiz quiz) {
        this.mQuiz = quiz;
    }

    @Override
    public String toString() {
        return mName;
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mCategoryId);
        dest.writeParcelable(mQuiz, flags);
    }
}
