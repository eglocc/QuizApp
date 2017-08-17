package com.assignment.quizapplication2;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mTitles;
    private int mDrawerListItemClickedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitles = getResources().getStringArray(R.array.drawer_items);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, mTitles));
    }

    private void updateFrameLayout(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new UserProfileFragment();
                break;
            case 2:
                fragment = new CategoryFragment();
                break;
            case 3:
                fragment = new FriendsListFragment();
                break;
            default:
                fragment = new HighScoresFragment();
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
