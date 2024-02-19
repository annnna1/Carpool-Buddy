package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        //identifying views
        TextView profile = findViewById(R.id.profileText2);
        TextView email = findViewById(R.id.emailText);
        TextView userType = findViewById(R.id.userTypeText);
        ImageView profileImage = findViewById(R.id.profileImage2);

        //retrieve current user information
        db.collection("users").document(mUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    //Displays Your profile vs (Name)'s profile depending on whether user sets name
                    User user = task.getResult().toObject(User.class);
                    if(user.getName()!=null)
                        profile.setText(user.getName() + "'s profile");
                    else
                        profile.setText("Your profile");

                    email.setText(user.getEmail());
                    userType.setText(user.getUserType());
                    profileImage.setImageResource(user.getProfileID());
                }
                else
                {
                    Log.d("Retrieving user document", "Failure!");
                }
            }
        });
    }

    public void onSaveProfile(View v)
    {
        //Saves new display name if the editText field is not empty when the save button is clicked
        EditText editName = findViewById(R.id.editDisplayName);
        if((!editName.getText().toString().isEmpty())&&(editName.getText().toString()!=null))
        {
            //retrieves current user information
            db.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        User user = task.getResult().toObject(User.class);
                        //sets new username in the database
                        user.setName(editName.getText().toString());
                        db.collection("users").document(mUser.getUid()).set(user);

                        //retrieves list of vehicles and sets new owner in database
                        db.collection("vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<DocumentSnapshot> vehicles = task.getResult().getDocuments();
                                    for (DocumentSnapshot v : vehicles) {
                                        Vehicle vehicle = v.toObject(Vehicle.class);
                                        if(vehicle.getOwnerUID().equals(mUser.getUid()))
                                            vehicle.setOwner(user.getName());
                                        db.collection("vehicles").document(vehicle.getVehicleID()).set(vehicle);
                                    }
                                } else {
                                    Log.d("Retrieving vehicles documents", "Failure to retrieve");
                                }
                            }
                        });
                    }
                    else
                    {
                        Log.d("Retrieving user document", "Failure to retrieve");
                    }
                }
            });
        }
    }

    public void onBackMain(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onLogOut(View v)
    {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }
}