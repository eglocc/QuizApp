package com.assignment.quizapplication2;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.assignment.quizapplication2.Category.mCategoryList;

public class CategoryFragment extends ListFragment {

    private DatabaseReference mDatabase;
    private FragmentItemListener mListener;

    private void loadCategoryNames(final LayoutInflater inflater) {
        Query queryRef = mDatabase.child("categories").orderByKey();
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String names[] = new String[(int) dataSnapshot.getChildrenCount()];
                int count = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Category category = child.getValue(Category.class);
                    mCategoryList.add(category);
                    names[count] = category.toString();
                    count++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, names);
                setListAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mListener = (FragmentItemListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadCategoryNames(inflater);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mListener.itemClicked(position);
        }
    }
}
