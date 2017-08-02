package com.assignment.quizapplication2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends ListFragment {

    private DatabaseReference mDatabase;
    private List<Category> mCategoryList;


    static interface CategoryListListener {
        void itemClicked(long id);
    }

    private CategoryListListener mListener;

    private void loadCategoryNames(final LayoutInflater inflater) {
        Query queryRef = mDatabase.child("categories").orderByValue();
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> nameList = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("key", child.getKey());
                    Log.d("value", child.getValue().toString());
                    String name = child.getValue(String.class);
                    mCategoryList.add(new Category(name));
                    nameList.add(name);
                    Log.d("category", mCategoryList.get(0).toString());
                }

                String names[] = new String[nameList.size()];
                names = nameList.toArray(names);
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
        this.mListener = (CategoryListListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCategoryList = new ArrayList<Category>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadCategoryNames(inflater);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null)
            mListener.itemClicked(id);
    }
}
