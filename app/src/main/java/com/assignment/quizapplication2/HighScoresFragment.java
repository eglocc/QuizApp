package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CopyOnWriteArrayList;

public class HighScoresFragment extends Fragment {

    private Context mContext;
    private ListView mTop10List;
    private Button mStartButton;
    private CopyOnWriteArrayList<User> mTop10Users;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);
        mTop10List = (ListView) view.findViewById(R.id.top_10_players_list);
        mStartButton = (Button) view.findViewById(R.id.start_button);

        return view;
    }

    private void loadTop10Players() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        Query query = ref.orderByChild("mScore").limitToFirst(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTop10Users = new CopyOnWriteArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    mTop10Users.add(child.getValue(User.class));
                }

                mTop10List.setAdapter(new HighScoreAdapter(mContext, android.R.layout.simple_list_item_2, android.R.id.text1, mTop10Users));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, getString(R.string.loading_data_failed), Toast.LENGTH_SHORT);
            }
        });
    }
}
