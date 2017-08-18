package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendsListFragment extends Fragment {

    private Context mContext;
    private ListView mListView;
    private ArrayList<User> mFriends;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void setmFriendsList(ArrayList<User> friends) {
        this.mFriends = friends;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        mListView = (ListView) view.findViewById(R.id.friends_list);
        if (mFriends != null) {
            mListView.setAdapter(new HighScoreAdapter(mContext, android.R.layout.simple_list_item_2, android.R.id.text1, mFriends));
        }
        return view;
    }
}
