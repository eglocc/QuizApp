package com.assignment.quizapplication2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity implements CategoryFragment.CategoryListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }

    @Override
    public void itemClicked(long id) {

    }
}
