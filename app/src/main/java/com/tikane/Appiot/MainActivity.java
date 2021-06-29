package com.tikane.Appiot;

import android.app.Notification;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.tikane.Appiot.App.CHANNEL_1_ID;
import static com.tikane.Appiot.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {

    //region Variables
    private DatabaseReference mDatabase;

    private Button motionSensorButton;
    private Button fireSensorButton;
    private Button gasSensorButton;
    private Button faceRecognitionButton;

    private TextView motionSensorStatusTextView;
    private TextView fireSensorStatusTextView;
    private TextView gasSensorStatusTextView;

    private String motionSensorValue;
    private String motionSensorSwitch;
    private String fireSensorValue;
    private String fireSensorSwitch;
    private String gasSensorValue;
    private String gasSensorSwitch;
    private String CHANNEL_ID = "1";

    private boolean isMotionSensorActive = false;
    private boolean isFireSensorActive = false;
    private boolean isGasSensorActive = false;

    int white = Color.parseColor("#FFFFFF");
    int blue = Color.parseColor("#023047");
    int green = Color.parseColor("#8AC926");

    private NotificationManagerCompat notificationCompat;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //createNotificationChannel();
        notificationCompat = NotificationManagerCompat.from(this);


        //region Buttons
        motionSensorButton = findViewById(R.id.motionSensorBtn);
        fireSensorButton = findViewById(R.id.fireSensorBtn);
        gasSensorButton = findViewById(R.id.GasSensorBtn);
        faceRecognitionButton = findViewById(R.id.faceRecognitionStatusBtn);
        //endregion

        //region TextViews
        motionSensorStatusTextView = findViewById(R.id.motionSensorStatus);
        fireSensorStatusTextView = findViewById(R.id.fireSensorStatus);
        gasSensorStatusTextView = findViewById(R.id.gasSensorStatus);
        //endregion


        //Set up firebase database....
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //results.setText(dataSnapshot.getValue().toString());

                //sensor readings
                motionSensorValue = dataSnapshot.child("Sensor_Read").child("PIR_M").getValue().toString();
                fireSensorValue = dataSnapshot.child("Sensor_Read").child("Fire").getValue().toString();
                gasSensorValue = dataSnapshot.child("Sensor_Read").child("GAS").getValue().toString();

                motionSensorSwitch = dataSnapshot.child("Sensor_Read").child("motionSensorSwitch").getValue().toString();
                fireSensorSwitch = dataSnapshot.child("Sensor_Read").child("fireSensorSwitch").getValue().toString();
                gasSensorSwitch = dataSnapshot.child("Sensor_Read").child("gasSensorSwitch").getValue().toString();


                activateMotionSensor(motionSensorSwitch);
                activateFireSensor(fireSensorSwitch);
                activateGasSensor(gasSensorSwitch);
                toggleMotionSensor(motionSensorValue);
                toggleFire(fireSensorValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //Open web for camera

        //call functions


    }

    public void toggleMotionSensor(String num) {
        if (num.equals("1")) {
            //Notification Builder
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_one)
                    .setContentTitle("Warning")
                    .setContentText("Motion Detected!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .build();
            notificationCompat.notify(1, notification);

            motionSensorStatusTextView.setText("Motion Detected!");
        } else if (num.equals("0")) {

            motionSensorStatusTextView.setText("No Motion Detected.");
        }
    }

    public void toggleFire(String num) {
        if (num.equals("1")) {

            fireSensorStatusTextView.setText("Fire Detected!");

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                    .setSmallIcon(R.drawable.ic_one)
                    .setContentTitle("Warning")
                    .setContentText("Fire Detected!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            notificationCompat.notify(2, notification);
        } else if (num.equals("0")) {

            fireSensorStatusTextView.setText("No Fire Detected.");
        }
    }

    public void activateMotionSensor(String m) {

        if (m.equals("0")) {
           // Toast.makeText(MainActivity.this, "0", Toast.LENGTH_LONG).show();
            motionSensorButton.setText("Inactive");
            isMotionSensorActive = false;
            motionSensorButton.setBackgroundResource(R.drawable.button_border);
            motionSensorButton.setTextColor(blue);
        } else if (m.equals("1")) {
          //  Toast.makeText(MainActivity.this, "1", Toast.LENGTH_LONG).show();
            motionSensorButton.setText("Active");
            motionSensorButton.setBackgroundColor(green);
            motionSensorButton.setTextColor(white);
            isMotionSensorActive = true;
        }

        motionSensorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (isMotionSensorActive) {
                    mDatabase.child("Sensor_Read").child("motionSensorSwitch").setValue(0);
                    motionSensorButton.setText("Inactive");
                    motionSensorButton.setBackgroundResource(R.drawable.button_border);
                    motionSensorButton.setTextColor(blue);
                    //motionSensorButton
                }
                else{
                    mDatabase.child("Sensor_Read").child("motionSensorSwitch").setValue(1);
                    motionSensorButton.setText("Active");
                    motionSensorButton.setBackgroundColor(green);
                    motionSensorButton.setTextColor(white);
                }
            }
        });
    }

    public  void activateFireSensor(String f){

        if (f.equals("0")) {
           // Toast.makeText(MainActivity.this, "0", Toast.LENGTH_LONG).show();
            fireSensorButton.setText("Inactive");
            fireSensorButton.setBackgroundResource(R.drawable.button_border);
            fireSensorButton.setTextColor(blue);
            isFireSensorActive = false;
        } else if (f.equals("1")) {
           // Toast.makeText(MainActivity.this, "1", Toast.LENGTH_LONG).show();
            fireSensorButton.setText("Active");
            fireSensorButton.setBackgroundColor(green);
            fireSensorButton.setTextColor(white);
            isFireSensorActive = true;
        }

        fireSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isFireSensorActive){
                    mDatabase.child("Sensor_Read").child("fireSensorSwitch").setValue(0);
                    fireSensorButton.setText("InActive");
                    fireSensorButton.setBackgroundResource(R.drawable.button_border);
                    fireSensorButton.setTextColor(blue);
                }
                else {
                    mDatabase.child("Sensor_Read").child("fireSensorSwitch").setValue(1);
                    fireSensorButton.setText("Active");
                    fireSensorButton.setBackgroundColor(green);
                    fireSensorButton.setTextColor(white);
                }
            }
        });
    }

    public void activateGasSensor(String g){
        if (g.equals("0")) {
            // Toast.makeText(MainActivity.this, "0", Toast.LENGTH_LONG).show();
            gasSensorButton.setText("Inactive");
            gasSensorButton.setBackgroundResource(R.drawable.button_border);
            gasSensorButton.setTextColor(blue);
            isGasSensorActive = false;
        } else if (g.equals("1")) {
            // Toast.makeText(MainActivity.this, "1", Toast.LENGTH_LONG).show();
            gasSensorButton.setText("Active");
            gasSensorButton.setBackgroundColor(green);
            gasSensorButton.setTextColor(white);
            isGasSensorActive = true;
        }

        gasSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isGasSensorActive){
                    mDatabase.child("Sensor_Read").child("gasSensorSwitch").setValue(0);
                    gasSensorButton.setText("InActive");
                    gasSensorButton.setBackgroundResource(R.drawable.button_border);
                    gasSensorButton.setTextColor(blue);
                }
                else {
                    mDatabase.child("Sensor_Read").child("gasSensorSwitch").setValue(1);
                    gasSensorButton.setText("Active");
                    gasSensorButton.setBackgroundColor(green);
                    gasSensorButton.setTextColor(white);
                }
            }
        });
    }

}
