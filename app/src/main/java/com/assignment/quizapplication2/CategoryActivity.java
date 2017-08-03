package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CategoryActivity extends AppCompatActivity implements FragmentListener {

    public static final String CLICKED_CATEGORY_POSITION = "clicked_category_position";

    private CategoryFragment mCategoryFragment;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mBundle = getIntent().getExtras();

        mCategoryFragment = (CategoryFragment) getFragmentManager().findFragmentById(R.id.list_category_fragment);
    }

    @Override
    public void itemClicked(int position) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer == null) {
            Intent intent = new Intent(this, PointsActivity.class);
            mBundle.putInt(CLICKED_CATEGORY_POSITION, position);
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }
}
