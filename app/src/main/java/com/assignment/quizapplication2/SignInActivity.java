package com.assignment.quizapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.assignment.quizapplication2.LoginActivity.sUser;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private static final String msOnAuthStateChangedSignedIn = "onAuthStateChanged:signed_in:";
    private static final String msOnAuthStateChangedSignedOut = "onAuthStateChanged:signed_out";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabaseRef;
    private DatabaseReference mUsersRef;

    private EditText mNameField;
    private EditText mSurnameField;
    private EditText mNicknameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabaseRef = FirebaseDatabase.getInstance();
        mUsersRef = mDatabaseRef.getReference("users");

        mNameField = (EditText) findViewById(R.id.set_name);
        mSurnameField = (EditText) findViewById(R.id.set_surname);
        mNicknameField = (EditText) findViewById(R.id.set_nickname);
        findViewById(R.id.create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, msOnAuthStateChangedSignedIn);
                } else {
                    // User is signed out
                    Log.d(TAG, msOnAuthStateChangedSignedOut);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        hideProgressDialog();
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Map<String, Object> users = new HashMap<>();
                    User customUser = new User(mNameField.getText().toString() + " " + mSurnameField.getText().toString(), mNicknameField.getText().toString());
                    users.put(user.getUid(), customUser);
                    mUsersRef.updateChildren(users);
                    sUser = customUser;
                    updateUI(user);
                } else {
                    Toast.makeText(SignInActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                }

                hideProgressDialog();
            }
        });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Query query = mUsersRef.orderByKey().equalTo(user.getUid());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null || dataSnapshot.getValue() != null) {
                                sUser = dataSnapshot.getValue(User.class);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    updateUI(user);
                } else {
                    Toast.makeText(SignInActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                }

                hideProgressDialog();
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_account_button:
                createAccount(mNameField.getText().toString().toLowerCase() + mSurnameField.getText().toString().toLowerCase() + "@ergizgizer.com", mNicknameField.getText().toString());
                break;
            case R.id.sign_in_button:
                signIn(mNameField.getText().toString().toLowerCase() + mSurnameField.getText().toString().toLowerCase() + "@ergizgizer.com", mNicknameField.getText().toString());
                break;
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        String name = mNameField.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNameField.setError("Required.");
            valid = false;
        } else {
            mNameField.setError(null);
        }

        String surname = mSurnameField.getText().toString();
        if (TextUtils.isEmpty(surname)) {
            mSurnameField.setError("Required.");
            valid = false;
        } else {
            mSurnameField.setError(null);
        }

        String nickname = mNicknameField.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            mNicknameField.setError("Required.");
            valid = false;
        } else {
            mNicknameField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}
