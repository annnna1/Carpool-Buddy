package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailField;
    private EditText passwordField;
    private String selected;
    private Spinner sUserType;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //instantiates all necessary Firebase objects and layout fields
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        emailField = findViewById(R.id.editEmail);
        passwordField = findViewById(R.id.editPassword);
        sUserType = findViewById(R.id.inputUserType);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.userType , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sUserType.setAdapter(adapter);
    }

    public void signIn (View v)
    {
        //if login/signin is clicked when on signup screen
        if (sUserType.getVisibility() == View.VISIBLE) {
            TextView txt = findViewById(R.id.logSignText);
            txt.setText("Log in to continue");
            Button sign = findViewById(R.id.signButton);
            sign.setTranslationY(0);
            Button log = findViewById(R.id.logButton);
            log.setTranslationY(0);
            sUserType.setVisibility(View.INVISIBLE);
            return;
        }

        //Log failure if user enters invalid details
        String e = emailField.getText().toString();
        String p = passwordField.getText().toString();
        if (e.isEmpty()||p.isEmpty())
        {   Log.w("Sign in", "signInUserWithEmail:failure");
            Toast.makeText(getBaseContext(),"Authentification failed.",Toast.LENGTH_SHORT).show();
            updateUI(null);
            return;
        }
        //signs in Firebase authentification with email and password given by user
        mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Sign in", "Successfully signed in");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else
                {
                    Log.w("Sign in", "signInUserWithEmail:failure", task.getException());
                    Toast.makeText(getBaseContext(), "Log in failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void signUp (View v)
    {
        //if signUp is pressed when on login screen
        if(sUserType.getVisibility()==View.INVISIBLE)
        {
            TextView txt = findViewById(R.id.logSignText);
            txt.setText("Create an account");
            Button sign = findViewById(R.id.signButton);
            sign.setTranslationY(220);
            Button log = findViewById(R.id.logButton);
            log.setTranslationY(220);
            sUserType.setVisibility(View.VISIBLE);
            return;
        }
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            selected = sUserType.getSelectedItem().toString();
            //Log failure if user enters invalid details
            if (email.isEmpty()||password.isEmpty())
            {
                Log.w("Sign up", "createUserWithEmail:failure");
                Toast.makeText(getBaseContext(), "Sign up failed.", Toast.LENGTH_SHORT).show();
                updateUI(null);
                return;
            }
            //signs in Firebase authentification with email and password given by user
            mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Log.d("Sign up", "Successfully signed up");

                        //Creates new user document and populated its fields
                        FirebaseUser mUser = mAuth.getCurrentUser();
                        User user;
                        switch(selected)
                        {
                            case "Student":
                                user = new Student ();
                                break;
                            case "Parent":
                                user = new Parent ();
                                break;
                            case "Teacher":
                                user = new Teacher ();
                                break;
                            case "Alumni":
                                user = new Alumni ();
                                break;
                            default:
                                user = null;
                        }
                        user.setEmail(email);
                        user.setUserType(selected);
                        user.setuID(mUser.getUid().toString());
                        db.collection("users").document(user.getuID()).set(user);
                        updateUI(mUser);
                   }
                    else
                    {
                       Log.w("Sign up", "createUserWithEmail:failure", task.getException());
                       Toast.makeText(getBaseContext(), "Sign up failed.", Toast.LENGTH_SHORT).show();
                       updateUI(null);
                    }

                }
            });
    }
    public void updateUI(FirebaseUser currentUser)
    {
        if(currentUser!=null)
        {
            if(sUserType.getVisibility()==View.VISIBLE)
            {
                Intent intent = new Intent(this, UserSetUp.class);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}