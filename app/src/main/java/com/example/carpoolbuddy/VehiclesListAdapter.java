package com.example.carpoolbuddy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class VehiclesListAdapter extends ArrayAdapter <Vehicle>
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    public VehiclesListAdapter(Context context, ArrayList<Vehicle> vehicleArrayList)
    {
        super(context, R.layout.vehicle_list_item, vehicleArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        Vehicle vehicle = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vehicle_list_item, parent, false);
        }
        //Assigning views to designated layout items
        ImageView vTypeImage = convertView.findViewById(R.id.vehicleTypeImage);
        TextView vTypeText = convertView.findViewById(R.id.vehicleTypeText);
        TextView click = convertView.findViewById(R.id.clickText);
        TextView status = convertView.findViewById(R.id.statusText);

        //Changes icon and text depending on vehicle type
        String vType = vehicle.getVehicleType();
        vTypeText.setText(vType);
        switch(vType)
        {
            case "Electric car":
                vTypeImage.setImageResource(R.drawable.electric_car);
                break;
            case "Car":
                vTypeImage.setImageResource(R.drawable.car);
                break;
            case "Bicycle":
                vTypeImage.setImageResource(R.drawable.bicycle);
                break;
            case "Helicopter":
                vTypeImage.setImageResource(R.drawable.helicopter);
                break;
            case "Segway":
                vTypeImage.setImageResource(R.drawable.segway);
                break;
            default:
                vTypeImage.setImageResource(R.drawable.monkey_car);
        }

        //Retrieves current user information from firebase
        db.collection("users").document(mUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    User user = task.getResult().toObject(User.class);
                    //semantic changes depending on whether user is vehicle owner
                    if(user.getuID().equals(vehicle.getOwnerUID()))
                    {
                        if (vehicle.getOpen())
                            status.setText("open");
                        else
                            status.setText("closed");
                        click.setText("Click to see vehicle details");
                    }
                    else
                    {
                        status.setText("$"+(vehicle.getBasePrice()*user.getPriceMultiplier()));
                        click.setText(vehicle.getOpenSeats() + " open seat(s)");
                    }
                }
                else
                {
                    Log.d("Retrieve user information", "Failed");
                }
            }
        });


        return convertView;
    }
}
