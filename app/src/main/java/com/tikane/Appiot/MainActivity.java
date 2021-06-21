package com.tikane.Appiot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Switch;
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

    private FirebaseDatabase fbdb;
    private DatabaseReference mDatabase ;


    private Button camera1;
    private Switch PIR;
    private Switch fire;
    private Switch GAS;
    private TextView motionTxtV;
    private TextView fireTxtV;
    private TextView gasTxtV;
    private String val;
    private String val2;
    private String val3;
    private String buz;
    private String CHANNEL_ID = "1";
    private NotificationManagerCompat notificationCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  creatNotificationChannel();
        notificationCompat = NotificationManagerCompat.from(this);
        //Buttons


        //Switches
        PIR = findViewById(R.id.switch_PIR);
        fire = findViewById(R.id.FireSensor_Switch);
        GAS = findViewById(R.id.switch_GAS);

        //TextViews
        motionTxtV = findViewById(R.id.textView_Motion);
        fireTxtV = findViewById(R.id.textView_fire);


        //Set up firebase database....
        mDatabase = FirebaseDatabase.getInstance().getReference();

        /*onbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "On", Toast.LENGTH_LONG).show();

                mDatabase.child("Sensor_Read").child("Buzzer").setValue("1");
                results.setText("Alarm is ON!!!");
            }
        });*/




        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //results.setText(dataSnapshot.getValue().toString());
                val = dataSnapshot.child("Sensor_Read").child("PIR_M").getValue().toString();
                val2 = dataSnapshot.child("Sensor_Read").child("Fire").getValue().toString();
                val3 = dataSnapshot.child("Sensor_Read").child("GAS").getValue().toString();

                togglePIR(val);
                toggleFire(val2);
                toggleGAS(val3);
           }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //Open web for camera

    }

    public void togglePIR(String num)
    {
        if(num.equals("1"))
        {
            PIR.setChecked(true);

            //Notification Builder
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_one)
                    .setContentTitle("Warning")
                    .setContentText("Motion Detected!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .build();
            notificationCompat.notify(1, notification);

            motionTxtV.setText("Motion Detected..!");
        }
        else if (num.equals("0"))
        {
            PIR.setChecked(false);

            motionTxtV.setText("No motion detected.");
        }
    }

    public void toggleFire(String num)
    {
        if(num.equals("1"))
        {
            fire.setChecked(true);

            fireTxtV.setText("Fire detected..!");

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                    .setSmallIcon(R.drawable.ic_one)
                    .setContentTitle("Warning")
                    .setContentText("Fire Detected!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            notificationCompat.notify(2, notification);

        }
        else if (num.equals("0"))
        {
            fire.setChecked(false);

            fireTxtV.setText("No fire detected.");
        }
    }

    public void toggleGAS(String num)
    {
        if(num.equals("1"))
        {
            GAS.setChecked(true);
        }
        else if(num.equals("0"))
        {
            GAS.setChecked(false);
        }

    }

}
