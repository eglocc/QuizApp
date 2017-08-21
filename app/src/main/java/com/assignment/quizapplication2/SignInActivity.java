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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private static final String msOnAuthStateChangedSignedIn = "onAuthStateChanged:signed_in:";
    private static final String msOnAuthStateChangedSignedOut = "onAuthStateChanged:signed_out";

    static User sUser;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabaseRef;
    private DatabaseReference mUsersRef;

    private EditText mNameField;
    private EditText mSurnameField;
    private EditText mNicknameField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabaseRef = FirebaseDatabase.getInstance();
        mUsersRef = mDatabaseRef.getReference("users");

        mNameField = (EditText) findViewById(R.id.set_name);
        mSurnameField = (EditText) findViewById(R.id.set_surname);
        mNicknameField = (EditText) findViewById(R.id.set_nickname);
        mPasswordField = (EditText) findViewById(R.id.set_password);
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
                    String nickname = mNicknameField.getText().toString();
                    if (TextUtils.isEmpty(nickname)) {
                        nickname = "Anonymous";
                    }
                    User customUser = new User(user.getUid(), mNameField.getText().toString() + " " + mSurnameField.getText().toString(), nickname);
                    users.put(user.getUid(), customUser);
                    mUsersRef.updateChildren(users);
                    sUser = new User(customUser);
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
                    DatabaseReference userRef = mUsersRef.child(user.getUid());
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            sUser = new User(dataSnapshot.getValue(User.class));
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
                createAccount(mNameField.getText().toString().toLowerCase() + mSurnameField.getText().toString().toLowerCase() + "@ergizgizer.com", mPasswordField.getText().toString());
                break;
            case R.id.sign_in_button:
                signIn(mNameField.getText().toString().toLowerCase() + mSurnameField.getText().toString().toLowerCase() + "@ergizgizer.com", mPasswordField.getText().toString());
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

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else {
            mPasswordField.setError(null);
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
