package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.carpoolbuddy.databinding.ActivityMainBinding;
import com.example.carpoolbuddy.databinding.ActivityVehiclesInfoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VehiclesInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<Vehicle> vList;
    ActivityVehiclesInfoBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVehiclesInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //instantiating nec var + setting filter spinner's adapter
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Spinner filter = findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.filter_options, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        filter.setAdapter(adapter);
        // changes vehicles displayed depending on selected filter
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vList = new ArrayList<>();
                //finds the minimum open seats from the selected filter option
                String selected = (String) parent.getSelectedItem();
                int minOpenSeats = (int)(selected.charAt(0)-'0');

                //Retrieve list of vehicles from firebase
                db.collection("vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            //For each vehicle in the vehicles collection, add to list if fulfills qualifications
                            List<DocumentSnapshot> vehicles = task.getResult().getDocuments();
                            for (DocumentSnapshot v: vehicles)
                            {
                                Vehicle vehicle = v.toObject(Vehicle.class);
                                if(vehicle.getOpen()&&((vehicle.getOpenSeats()>=minOpenSeats)||selected.equals("Filter: Any open seats")))
                                    vList.add(vehicle);
                                VehiclesListAdapter vehiclesListAdapter = new VehiclesListAdapter(VehiclesInfoActivity.this, vList);
                                binding.ridesList.setAdapter(vehiclesListAdapter);
                                //Allows for vehicles to be clicked for vehicleprofile screen
                                binding.ridesList.setClickable(true);
                                binding.ridesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                    {
                                        Intent intent = new Intent (VehiclesInfoActivity.this, VehicleProfileActivity.class);
                                        intent.putExtra("vehicleUID", vList.get(position).getVehicleID());
                                        intent.putExtra("prActivity", "VehiclesInfoActivity");
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                        else{}
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    public void onAddRide(View v)
    {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        intent.putExtra("prActivity", "vehiclesInfoActivity");
        startActivity(intent);
    }
    public void onRefresh(View v)
    {
        Intent intent = new Intent(this, VehiclesInfoActivity.class);
        startActivity(intent);
    }
    public void onBackHome(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}