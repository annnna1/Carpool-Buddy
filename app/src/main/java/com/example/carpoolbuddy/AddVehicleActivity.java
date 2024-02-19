package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

public class AddVehicleActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private Spinner vehicleTypes;
    private EditText model;
    private EditText maxCapacity;
    private EditText price;
    private EditText description;
    private String prActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        vehicleTypes = findViewById(R.id.vehicleTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.vehicleType , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        vehicleTypes.setAdapter(adapter);
        model = findViewById(R.id.editModel);
        maxCapacity = findViewById(R.id.editMaxCap);
        price = findViewById(R.id.editPrice);
        description = findViewById(R.id.editDescription);
        prActivity = getIntent().getStringExtra("prActivity");
    }

    public boolean formValid()
    {
        //if null
        if(model.getText() == null || maxCapacity.getText() == null || price.getText() == null)
            return false;
        //if capacity and price are not integers
        for (char ch: maxCapacity.getText().toString().toCharArray())
        {
            if(ch<'0'||ch>'9')
                return false;
        }
        for (char ch: price.getText().toString().toCharArray())
        {
            if(ch<'0'||ch>'9')
                return false;
        }
        return true;
    }

    public void addNewVehicle(View v)
    {
        if(formValid())
        {
            Vehicle vehicle;
            String selected = vehicleTypes.getSelectedItem().toString();
            switch (selected)
            {
                case "Electric car":
                    vehicle = new ElectricCar();
                    break;
                case "Car":
                    vehicle = new Car();
                    break;
                case "Bicycle":
                    vehicle = new Bicycle();
                    break;
                case "Helicopter":
                    vehicle = new Helicopter();
                    break;
                case "Segway":
                    vehicle = new Segway();
                    break;
                default:
                    vehicle = null;
            }
            vehicle.setVehicleType(selected);
            vehicle.setModel(model.getText().toString());
            vehicle.setCapacity(Integer.parseInt(maxCapacity.getText().toString()));
            vehicle.setBasePrice(Double.parseDouble(price.getText().toString()));
            vehicle.setOpen(true);
            vehicle.setOpenSeats(vehicle.getCapacity());
            vehicle.setVehicleID(UUID.randomUUID().toString());
            vehicle.setOwnerUID(mUser.getUid().toString());
            vehicle.setDescription(description.getText().toString());
            db.collection("users").document(mUser.getUid().toString()).get().addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        User user = task.getResult().toObject(User.class);
                        vehicle.setOwner(user.getName());
                        db.collection("vehicles").document(vehicle.getVehicleID()).set(vehicle);
                        ArrayList<String> ownedVehicles = user.getOwnedVehicles();
                        ownedVehicles.add(vehicle.getVehicleID());
                        db.collection("users").document(user.getuID()).set(user);
                    }
                    else
                    {
                        Log.d("Retrieve owner name", "Failure for some reason");
                    }
                }
            });
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Log.d("Add new vehicle", "Users fault bad info");
            Toast.makeText(getBaseContext(), "New vehicle addition failed. Enter valid information", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCancel (View v)
    {
        Intent intent;
        if(prActivity.equals("Main"))
        {
            intent = new Intent(this, MainActivity.class);
        }
        else
        {
            intent = new Intent(this, VehiclesInfoActivity.class);
        }
        startActivity(intent);
    }
}