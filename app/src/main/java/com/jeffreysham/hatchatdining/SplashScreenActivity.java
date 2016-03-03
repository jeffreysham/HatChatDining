package com.jeffreysham.hatchatdining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Fresco.initialize(this);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        final Context context = this;

        if (!isNetworkAvailable()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("No internet access")
                    .setMessage("Please turn on internet for better experience.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alert.create().show();
        } else {
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(!gps_enabled && !network_enabled) {
                // notify user
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Error: Location Services");
                dialog.setMessage("Location Services are disabled. Please enable it on your phone.");
                dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        //get gps
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
                dialog.show();
            } else {

                SharedPreferences pref = this.getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                String phoneNumber = pref.getString("phone number", null);

                if (phoneNumber != null) {
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                } else {
                    String message = "HatChat Dining asks users for their location, name, and phone number. " +
                            "This information is used in order to create your profile and locate nearby users. " +
                            "Also, your phone number will be used so that you can call or message nearby users or restaurants. " +
                            "This application will never collect or transmit your personal data without " +
                            "your consent.";

                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Privacy Policy")
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context,MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                    alert.create().show();
                }
            }
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
