package com.assignment.quizapplication2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.assignment.quizapplication2.SignInActivity.sUser;

public class EditInfoFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    private ArrayList<EditText> mEditTexts;
    private ArrayList<Button> mEditButtons;
    private Button mSaveChangesButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mEditTexts = new ArrayList<>();
        mEditButtons = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_info, container, false);
        mEditTexts.add((EditText) view.findViewById(R.id.user_name_field));
        mEditTexts.add((EditText) view.findViewById(R.id.user_surname_field));
        mEditTexts.add((EditText) view.findViewById(R.id.user_nickname_field));
        mEditButtons.add((Button) view.findViewById(R.id.edit_button_name));
        mEditButtons.add((Button) view.findViewById(R.id.edit_button_surname));
        mEditButtons.add((Button) view.findViewById(R.id.edit_button_nickname));
        mSaveChangesButton = (Button) view.findViewById(R.id.save_changes);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] name = sUser.getmName().split(" ");
        mEditTexts.get(0).setText(name[0]);
        mEditTexts.get(1).setText(name[1]);
        mEditTexts.get(2).setText(sUser.getmNickname());
        for (Button b : mEditButtons) {
            b.setOnClickListener(this);
        }
        mSaveChangesButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button_name:
                setEnabled(0, true);
                break;
            case R.id.edit_button_surname:
                setEnabled(1, true);
                break;
            case R.id.edit_button_nickname:
                setEnabled(2, true);
                break;
            case R.id.save_changes:
                saveChanges();
                break;
        }
    }

    private void setEnabled(int position, boolean enabled) {
        mEditTexts.get(position).setEnabled(enabled);
        mSaveChangesButton.setEnabled(enabled);
    }

    private void saveChanges() {
        String name = mEditTexts.get(0).getText().toString() + " " + mEditTexts.get(1).getText().toString();
        String nickname = mEditTexts.get(2).getText().toString();
        sUser.setmName(name);
        sUser.setmNickname(nickname);
        DatabaseReference userRef = mDatabaseRef.child(mUser.getUid());
        userRef.setValue(sUser);
        for (EditText e : mEditTexts) {
            e.setEnabled(false);
        }
        mSaveChangesButton.setEnabled(false);
    }
}
