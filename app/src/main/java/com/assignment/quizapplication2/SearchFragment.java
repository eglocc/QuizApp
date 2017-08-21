package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.assignment.quizapplication2.SignInActivity.sUser;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private Context mContext;
    private DatabaseReference mUsersRef;
    private FirebaseUser mUser;
    private SearchView mSearchView;

    private ListView mListView;
    private ArrayList<User> mSearchedUsers;
    private boolean[] mSelectedUsers;

    public void setmSearchedUsers(ArrayList<User> users) {
        this.mSearchedUsers = users;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsersRef = FirebaseDatabase.getInstance().getReference("users");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mListView = (ListView) view.findViewById(R.id.searched_users_list);
        if (mSearchedUsers != null) {
            mListView.setAdapter(new HighScoreAdapter(mContext, android.R.layout.simple_list_item_activated_2, android.R.id.text1, mSearchedUsers));
            mSelectedUsers = new boolean[mSearchedUsers.size()];
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView list = ((ListView) parent);
                    mSelectedUsers[position] = list.isItemChecked(position);
                    Log.d(TAG, Boolean.toString(mSelectedUsers[position]));
                }
            });
            Button addButton = (Button) view.findViewById(R.id.add_friend_button);
            Button removeButton = (Button) view.findViewById(R.id.remove_friend_button);
            if (mSearchedUsers.size() > 0) {
                addButton.setOnClickListener(this);
                removeButton.setOnClickListener(this);
            } else {
                addButton.setVisibility(View.INVISIBLE);
                removeButton.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        ArrayList<User> currentFriends = sUser.getmFriends();
        switch (v.getId()) {
            case R.id.add_friend_button:
                if (currentFriends == null) {
                    sUser.setmFriends(new ArrayList<User>());
                    //Check
                    currentFriends = sUser.getmFriends();
                }
                for (int i = 0; i < mSelectedUsers.length; i++) {
                    if (mSelectedUsers[i])
                        currentFriends.add(mSearchedUsers.get(i));

                }
                Log.d(TAG, currentFriends.toString());
                int currentFriendsSize = currentFriends.size();
                if (currentFriendsSize > 0) {
                    User[] addFriends = new User[currentFriendsSize];
                    addFriends = currentFriends.toArray(addFriends);
                    new WriteSQLDataTask(mContext, WriteSQLDataTask.TaskType.ADD).execute(addFriends);
                }
                break;
            case R.id.remove_friend_button:
                ArrayList<User> removedFriends = new ArrayList<>();
                if (currentFriends != null) {
                    for (int i = 0; i < mSelectedUsers.length; i++) {
                        User candidateForRemoval = mSearchedUsers.get(i);
                        if (mSelectedUsers[i] && currentFriends.contains(candidateForRemoval)) {
                            currentFriends.remove(candidateForRemoval);
                            removedFriends.add(candidateForRemoval);
                        }
                    }
                }
                int removedFriendsSize = removedFriends.size();
                if (removedFriendsSize > 0) {
                    User[] removal = new User[removedFriendsSize];
                    removal = removedFriends.toArray(removal);
                    new WriteSQLDataTask(mContext, WriteSQLDataTask.TaskType.REMOVE).execute(removal);
                }
                break;
            default:
                Log.d(TAG, "What!?");
        }
        DatabaseReference newRef = mUsersRef.child(mUser.getUid());
        newRef.setValue(sUser);
    }
}
