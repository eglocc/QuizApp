package com.assignment.quizapplication2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.assignment.quizapplication2.SignInActivity.sUser;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TOP_10_HIGH_SCORES_FRAGMENT = 0;
    private static final int USER_PROFILE_FRAGMENT = 1;
    private static final int CATEGORY_FRAGMENT = 2;
    private static final int FRIENDS_LIST_FRAGMENT = 3;
    private static final int SEARCH_USER_FRAGMENT = 4;

    private Bundle mBundle;

    private SearchView mSearchView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mDrawerListItemSelectedPosition;

    private ListView mDrawerList;
    private String[] mTitles;

    private MyOnQueryTextListener mSearchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitles = getResources().getStringArray(R.array.drawer_items);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, mTitles));
        mDrawerList.setOnItemClickListener(this);
        mSearchListener = new MyOnQueryTextListener(this);

        mBundle = getIntent().getExtras();
        if (mBundle == null) {
            mBundle = new Bundle();
        } else {
            mDrawerListItemSelectedPosition = mBundle.getInt("previous_position");
            mDrawerList.setItemChecked(mDrawerListItemSelectedPosition, true);
        }

        if (savedInstanceState != null) {
            mDrawerListItemSelectedPosition = savedInstanceState.getInt("position");
        }

        updateUI(mDrawerListItemSelectedPosition);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
            }

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getFragmentManager().findFragmentByTag("visible_fragment");
                if (fragment instanceof HighScoresFragment) {
                    mDrawerListItemSelectedPosition = 0;
                } else if (fragment instanceof UserProfileFragment) {
                    mDrawerListItemSelectedPosition = 1;
                } else if (fragment instanceof FriendsListFragment) {
                    mDrawerListItemSelectedPosition = 3;
                } else if (fragment instanceof SearchFragment) {
                    mDrawerListItemSelectedPosition = 4;
                }
                setActionBarTitle(mDrawerListItemSelectedPosition);
                mDrawerList.setItemChecked(mDrawerListItemSelectedPosition, true);
            }
        });
    }

    private void updateUI(int position) {
        mBundle.putInt("previous_position", mDrawerListItemSelectedPosition);
        mDrawerListItemSelectedPosition = position;
        Fragment fragment = null;
        switch (position) {
            case USER_PROFILE_FRAGMENT:
                fragment = new UserProfileFragment();
                break;
            case CATEGORY_FRAGMENT:
                Intent i = new Intent(this, CategoryActivity.class);
                i.putExtras(mBundle);
                startActivity(i);
                break;
            case FRIENDS_LIST_FRAGMENT:
                fragment = new FriendsListFragment();
                break;
            case SEARCH_USER_FRAGMENT:
                fragment = new SearchFragment();
                break;
            default:
                fragment = new HighScoresFragment();
        }

        if (fragment != null) {
            if (fragment instanceof UserProfileFragment) {
                ((UserProfileFragment) fragment).setmFriends(sUser.getmFriends());
            } else if (fragment instanceof FriendsListFragment) {
                ((FriendsListFragment) fragment).setmFriendsList(sUser.getmFriends());
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "visible_fragment");
            mSearchListener.setmVisibleFragment(fragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }

        setActionBarTitle(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void setActionBarTitle(int position) {
        String title = mTitles[position];
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                updateUI(CATEGORY_FRAGMENT);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.drawer_list:
                updateUI(position);
                break;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_start_game).setVisible(!drawerOpen);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(mSearchListener);
        return super.onCreateOptionsMenu(menu);
    }

    protected void doMySearch(String query) {
        ArrayList<User> subFriendList = new ArrayList<>();
        if (sUser.getmFriends() != null) {
            for (User user : sUser.getmFriends()) {
                if (user.getmNickname().equals(query)) {
                    subFriendList.add(user);
                }
            }
        }
        FriendsListFragment fragment = new FriendsListFragment();
        fragment.setmFriendsList(subFriendList);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "visible_fragment");
        mSearchListener.setmVisibleFragment(fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_start_game:
                updateUI(CATEGORY_FRAGMENT);
                return true;
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mDrawerListItemSelectedPosition);
    }
}
