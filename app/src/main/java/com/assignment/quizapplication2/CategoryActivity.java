package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CategoryActivity extends AppCompatActivity implements CategoryFragment.CategoryListListener {

    private User mUser;
    private CategoryFragment mCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mUser = getIntent().getExtras().getParcelable("user");

        mCategoryFragment = (CategoryFragment) getFragmentManager().findFragmentById(R.id.list_category_fragment);
    }

    @Override
    public void itemClicked(int position) {
        Bundle bundle = mCategoryFragment.getmBundle();
        bundle.putParcelable("user", mUser);
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer == null) {
            Intent intent = new Intent(this, PointsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
