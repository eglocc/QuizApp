package com.assignment.quizapplication2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class UserProfileFragment extends Fragment {

    private ArrayList<User> mFriends;

    public void setmFriends(ArrayList<User> friends) {
        this.mFriends = friends;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        EditInfoFragment editInfoFragment = (EditInfoFragment) fm.findFragmentByTag("edit_info_fragment");
        FriendsListFragment listFragment = (FriendsListFragment) fm.findFragmentByTag("friends_list_fragment");
        FragmentTransaction ft = fm.beginTransaction();
        if (editInfoFragment == null) {
            editInfoFragment = new EditInfoFragment();
            ft.add(R.id.edit_info_container, editInfoFragment, "edit_info_fragment");
        }
        if (listFragment == null) {
            listFragment = new FriendsListFragment();
            listFragment.setmFriendsList(mFriends);
            ft.add(R.id.friends_list_container, listFragment, "friends_list_fragment");
        }
        ft.commit();

    }
}
