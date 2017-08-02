package com.assignment.quizapplication2;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {

    private String mText;
    private int mIndex;
    private boolean mTrue;

    public Answer(String text, int no) {
        this.mText = text;
        this.mIndex = no;
    }

    private Answer() {
    }

    private Answer(Parcel source) {
        this.mText = source.readString();
        this.mIndex = source.readInt();
        this.mTrue = source.readByte() != 0;
    }

    public int getmIndex() {
        return mIndex;
    }

    public void setmIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public boolean getmTrue() {
        return mTrue;
    }

    public void setmTrue(boolean mTrue) {
        this.mTrue = mTrue;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mText);
        dest.writeInt(mIndex);
        dest.writeByte((byte) (mTrue ? 1 : 0));
    }
}
