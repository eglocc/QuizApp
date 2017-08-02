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

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends ListFragment {

    public static final String CATEGORY_LIST = "category_list";
    public static final String CLICKED_CATEGORY_POSITION = "clicked_category_position";

    private DatabaseReference mDatabase;
    private List<Category> mCategoryList;
    private Bundle mBundle;

    static interface CategoryListListener {
        void itemClicked(int position);
    }

    private CategoryListListener mListener;

    private void loadCategoryNames(final LayoutInflater inflater) {
        Query queryRef = mDatabase.child("categories").child("names").orderByValue();
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String names[] = new String[(int) dataSnapshot.getChildrenCount()];
                int position = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String name = child.getValue(String.class);
                    mCategoryList.add(new Category(name, position));
                    names[position] = name;
                    position++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, names);
                setListAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Category> getmCategoryList() {
        return (ArrayList<Category>) mCategoryList;
    }

    public Bundle getmBundle() {
        return mBundle;
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
        mBundle = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadCategoryNames(inflater);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mBundle.putParcelableArrayList(CATEGORY_LIST, (ArrayList<Category>) mCategoryList);
            mBundle.putInt(CLICKED_CATEGORY_POSITION, position);
            mListener.itemClicked(position);
        }
    }
}
