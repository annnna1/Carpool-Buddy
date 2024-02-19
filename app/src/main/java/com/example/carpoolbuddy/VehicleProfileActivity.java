package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class VehicleProfileActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private String vehicleUID;
    private String prActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        //instantiate prereq var
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        vehicleUID = getIntent().getStringExtra("vehicleUID");
        prActivity = getIntent().getStringExtra("prActivity");
        setUpButton();
    }

    public void setUpButton()
    {
        //retrieve specific vehicle from firebase using UID
        db.collection("vehicles").document(vehicleUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //vehicle = result of retrieval
                    Vehicle vehicle = task.getResult().toObject(Vehicle.class);

                    //retrieve current user information from firebase
                    db.collection("users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                //user = result of retrieval
                                User user = task.getResult().toObject(User.class);

                                //set TextViews
                                TextView nameInfo = findViewById(R.id.nameInfoText);
                                nameInfo.setText(vehicle.getOwner() + "'s " + vehicle.getVehicleType().toLowerCase());
                                TextView owner = findViewById(R.id.ownerText);
                                owner.setText(vehicle.getOwner());
                                TextView model = findViewById(R.id.modelText);
                                model.setText(vehicle.getModel());
                                TextView maxCap = findViewById(R.id.maxCapText);
                                maxCap.setText("" + vehicle.getCapacity());
                                TextView openSeats = findViewById(R.id.openSeatsText);
                                openSeats.setText("" + vehicle.getOpenSeats());
                                TextView price = findViewById(R.id.priceText);
                                TextView priceBase = findViewById(R.id.priceBaseText);
                                TextView description = findViewById(R.id.descriptionText);

                                //description is optional and if user does not enter text will display N/A
                                //only changes N/A if description has content
                                if ((vehicle.getDescription() != null) && !(vehicle.getDescription().isEmpty()))
                                    description.setText(vehicle.getDescription());

                                //semantics changes dependent on the vehicle being owned by the user
                                if (vehicle.getOwnerUID().equals(mUser.getUid())) {
                                    nameInfo.setText("Your " + vehicle.getVehicleType().toLowerCase());
                                    price.setText("" + vehicle.getBasePrice());
                                    priceBase.setText("Base price:");
                                    //button changes if vehicle is open or closed
                                    if (!vehicle.getOpen()) {
                                        Button open = findViewById(R.id.openButton);
                                        open.setVisibility(View.VISIBLE);
                                    } else {
                                        Button close = findViewById(R.id.closeButton);
                                        close.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Button book = findViewById(R.id.reserveButton);
                                    book.setVisibility(View.VISIBLE);
                                    //25% off for environmental options --> incentivise environmentally conscious decisions
                                    if(vehicle.getVehicleType().equals("Electric car")||vehicle.getVehicleType().equals("Bicycle"))
                                        price.setText("" + (vehicle.getBasePrice() * user.getPriceMultiplier()*0.75));
                                    //Price changes depending on price multiplier
                                    else
                                        price.setText("" + (vehicle.getBasePrice() * user.getPriceMultiplier()));
                                }

                                //Back button returns user to different activities depending on which they came from
                                if (prActivity.equals("Main")) {
                                    Button backMain = findViewById(R.id.backMainButton);
                                    backMain.setVisibility(View.VISIBLE);
                                } else {
                                    Button backRides = findViewById(R.id.backRidesButton);
                                    backRides.setVisibility(View.VISIBLE);
                                }
                            }
                            else
                            {
                                Log.d("Retrieve user document", "Failed for some reason");
                            }
                        }
                    });
                }
                else
                {
                    Log.d("Retrieve vehicle document", "Failed for some reason");
                }
            }
        });
    }
    //Opens vehicle when open is pressed
    public void onOpen(View v)
    {
        db.collection("vehicles").document(vehicleUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    Vehicle vehicle = task.getResult().toObject(Vehicle.class);
                    vehicle.setOpen(true);
                    vehicle.setOpenSeats(vehicle.getCapacity());
                    db.collection("vehicles").document(vehicleUID).set(vehicle);
                    Intent intent = new Intent(VehicleProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Log.d("Retrieve vehicle document", "Failed to retrieve");
                }
            }
        });
    }
    //closes vehicle when close is pressed
    public void onClose(View v)
    {
        db.collection("vehicles").document(vehicleUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    Vehicle vehicle = task.getResult().toObject(Vehicle.class);
                    vehicle.setOpen(false);
                    vehicle.setOpenSeats(0);
                    db.collection("vehicles").document(vehicleUID).set(vehicle);
                    Intent intent = new Intent(VehicleProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Log.d("Retrieve vehicle document", "Failed to retrieve");
                }
            }
        });
    }
    //decreases seat availability when a ride is booked, closes if availability reaches zero
    public void bookRide(View v)
    {
        db.collection("vehicles").document(vehicleUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    Vehicle vehicle = task.getResult().toObject(Vehicle.class);
                    vehicle.setOpenSeats(vehicle.getOpenSeats()-1);
                    if(vehicle.getOpenSeats()==0)
                        vehicle.setOpen(false);
                    db.collection("vehicles").document(vehicleUID).set(vehicle);
                    Toast.makeText(VehicleProfileActivity.this, "Successful reservation attempt", Toast.LENGTH_SHORT);
                }
                else
                {
                    Toast.makeText
                            (VehicleProfileActivity.this, "Unsuccessful reservation attempt", Toast.LENGTH_SHORT);
                    Log.d("Booking", "Booking failed");
                }
            }
        });
        Intent intent = new Intent(this, VehiclesInfoActivity.class);
        startActivity(intent);
    }
    public void onBackMain(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBackRides(View v)
    {
        Intent intent = new Intent(this, VehiclesInfoActivity.class);
        startActivity(intent);
    }
}