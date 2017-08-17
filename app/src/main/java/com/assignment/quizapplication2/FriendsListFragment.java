package com.assignment.quizapplication2;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.assignment.quizapplication2.SignInActivity.sUser;

public class FriendsListFragment extends ListFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (sUser.getmFriends() != null) {
            setListAdapter(new HighScoreAdapter(inflater.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, sUser.getmFriends()));
        } else {
            setListAdapter(new HighScoreAdapter(inflater.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, new ArrayList<User>()));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
