package com.assignment.quizapplication2;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TOP_10_HIGH_SCORES_FRAGMENT = 0;
    private static final int USER_PROFILE_FRAGMENT = 1;
    private static final int CATEGORY_FRAGMENT = 2;
    private static final int FRIENDS_LIST_FRAGMENT = 3;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitles = getResources().getStringArray(R.array.drawer_items);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, mTitles));
        updateUI(TOP_10_HIGH_SCORES_FRAGMENT);
    }

    private void updateUI(int position) {
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new UserProfileFragment();
                break;
            case 2:
                Intent i = new Intent(this, CategoryActivity.class);
                startActivity(i);
                break;
            case 3:
                fragment = new FriendsListFragment();
                break;
            default:
                fragment = new HighScoresFragment();
        }

        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                updateUI(CATEGORY_FRAGMENT);
                break;
        }
    }
}
