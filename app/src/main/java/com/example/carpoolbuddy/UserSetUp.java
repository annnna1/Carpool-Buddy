package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserSetUp extends AppCompatActivity {
//set up profile picture
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set_up);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //set profile picture to monkeycar
        ImageView profileImage = findViewById(R.id.profileImage);
        profileImage.setImageResource(R.drawable.monkey_car);
    }

    public void onContinue(View v)
    {
        FirebaseUser user = mAuth.getCurrentUser();
        //Set display name
        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Log.d("Set up", "Successfully set up");
                            User mUser = task.getResult().toObject(User.class);
                            nameField = findViewById(R.id.editName);
                            String name = nameField.getText().toString();
                            if(!(name.isEmpty())&&!(name.equals(null)))
                            {
                                mUser.setName(name);
                                db.collection("users").document(mUser.getuID()).set(mUser);
                            }
                            updateUI(user);
                        }
                    }
                });
    }

    public void onReturn(View v)
    {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

    public void updateUI(FirebaseUser currentUser)
    {
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}