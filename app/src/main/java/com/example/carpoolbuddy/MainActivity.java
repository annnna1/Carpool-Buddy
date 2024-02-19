package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.carpoolbuddy.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<Vehicle> vList;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        vList = new ArrayList<>();

        //Retrieve list of owned vehicles IDs from firebase
        db.collection("users").document(mUser.getUid()).get()
                .addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            User user = task.getResult().toObject(User.class);
                            ArrayList<String> vUIDList = user.getOwnedVehicles();
                            //vUIDList aOK
                            vList.clear();
                            System.out.println(vUIDList.size());
                            //retrieve owned vehicles from firebase
                            for(int i = 0 ; i < vUIDList.size(); i ++)
                            {
                                db.collection("vehicles").document(vUIDList.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            vList.add(task.getResult().toObject(Vehicle.class));
                                            VehiclesListAdapter vehiclesListAdapter = new VehiclesListAdapter(MainActivity.this, vList);
                                            binding.vehiclesList.setAdapter(vehiclesListAdapter);
                                            binding.vehiclesList.setClickable(true);
                                            binding.vehiclesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                                {
                                                    Intent intent = new Intent (MainActivity.this, VehicleProfileActivity.class);
                                                    intent.putExtra("vehicleUID", vList.get(position).getVehicleID());
                                                    intent.putExtra("prActivity", "Main");
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                        else
                                        {
                                            Log.d("Vehicle retrieval", "Failure retrieving document");
                                        }
                                    }
                                });
                            }
                        }
                        else
                        {
                            Log.d("User retrieval", "Failure retrieving document");
                        }
                    }
                });
    }

    public void onAddVehicle(View v)
    {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    public void onRides(View v)
    {
        Intent intent = new Intent(this, VehiclesInfoActivity.class);
        startActivity(intent);
    }

    public void onProfile(View v)
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void onLogOut(View v)
    {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }
}