package com.assignment.quizapplication2;

import java.util.ArrayList;

public class Category {

    static ArrayList<Category> mCategoryList = new ArrayList<>();

    private String mCategoryName;
    private ArrayList<Question> mQuestionList;

    private Category() {
    }

    ;

    public Category(String name, ArrayList<Question> questionList) {
        this.mCategoryName = name;
        this.mQuestionList = questionList;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public void setmCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public ArrayList<Question> getmQuestionList() {
        return mQuestionList;
    }

    public void setmQuestionList(ArrayList<Question> mQuestionList) {
        this.mQuestionList = mQuestionList;
    }

    @Override
    public String toString() {
        return mCategoryName;
    }
}
