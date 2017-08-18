package com.assignment.quizapplication2;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOnQueryTextListener implements SearchView.OnQueryTextListener {

    private Context mContext;
    private Fragment mVisibleFragment;
    private DatabaseReference mUsersRef;

    public MyOnQueryTextListener(Context context) {
        this.mContext = context;
        mUsersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void setmVisibleFragment(Fragment fragment) {
        this.mVisibleFragment = fragment;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mVisibleFragment != null &&
                (mVisibleFragment instanceof FriendsListFragment || mVisibleFragment instanceof UserProfileFragment
                        || mVisibleFragment instanceof SearchFragment)) {
            if (mContext instanceof MainActivity) {
                final ArrayList<User> searchedUsers = new ArrayList<>();
                Query search = mUsersRef.orderByChild("mNickname").equalTo(query);
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //((MainActivity) mContext).showProgressDialog();
                        if (dataSnapshot != null) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                searchedUsers.add(child.getValue(User.class));
                            }
                        }
                        //((MainActivity) mContext).hideProgressDialog();
                        SearchFragment fragment = new SearchFragment();
                        fragment.setmSearchedUsers(searchedUsers);
                        FragmentTransaction ft = ((MainActivity) mContext).getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment, "visible_fragment");
                        setmVisibleFragment(fragment);
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ((MainActivity) mContext).getSupportActionBar().setTitle("Search");
            }
        } else {
            if (mContext instanceof MainActivity) {
                ((MainActivity) mContext).doMySearch(query);
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
