package com.jeffreysham.hatchatdining;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.uber.sdk.android.rides.RequestButton;
import com.uber.sdk.android.rides.RideParameters;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class HandleRestaurantChoiceActivity extends AppCompatActivity {

    private final Context context = this;
    private String restID;
    private String theMobileURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_handle_restaurant_choice);
        } catch (RuntimeException e)  {
            e.printStackTrace();
            Fresco.initialize(this);
            setContentView(R.layout.activity_handle_restaurant_choice);
        }

        Bundle extras = getIntent().getExtras();
        final String number = extras.getString("number");
        String desc = extras.getString("description");
        String name = extras.getString("name");
        String photoURL = extras.getString("photoURL");
        final String address = extras.getString("address");
        final double lat = extras.getDouble("latitude");
        final double lon = extras.getDouble("longitude");
        theMobileURL = extras.getString("mobileURL");
        restID = extras.getString("id");
        float destLat = extras.getFloat("destination latitude");
        float destLon = extras.getFloat("destination longitude");

        TextView numberView = (TextView)findViewById(R.id.number_view);
        TextView addressView = (TextView)findViewById(R.id.address_view);
        TextView descriptionView = (TextView)findViewById(R.id.rest_description);
        TextView nameView = (TextView)findViewById(R.id.rest_name_view);
        SimpleDraweeView photoView = (SimpleDraweeView)findViewById(R.id.rest_view);
        ImageButton callButton = (ImageButton) findViewById(R.id.call_button);
        ImageButton mapButton = (ImageButton) findViewById(R.id.map_button);

        if (photoURL != null) {
            Uri uri = Uri.parse(photoURL);
            photoView.setImageURI(uri);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Visit Yelp for more information")
                            .setMessage("Are you sure you want to navigate to Yelp?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(theMobileURL));
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alert.create().show();
                }
            });
        }

        if (name != null) {
            nameView.setText(name);
        }

        if (desc != null) {
            descriptionView.setText(desc);
        }

        if (address != null) {
            addressView.setText(address);
        }

        if (number != null) {
            numberView.setText(number);
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context,
                            "Call failed, please try again later!",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+lat+","
                        +lon+"?q="+address);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(context,
                            "Navigation failed: Google Maps is not installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestButton uberButton = (RequestButton)findViewById(R.id.uber_button);
        RideParameters rideParameters = new RideParameters.Builder()
                .setPickupToMyLocation()
                .setDropoffLocation(destLat,destLon,name,address)
                .build();
        uberButton.setRideParameters(rideParameters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.open_online) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Visit Yelp for more information")
                    .setMessage("Are you sure you want to navigate to Yelp?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(theMobileURL));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alert.create().show();
            return true;
        } else if (id == R.id.open_menu_online) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Visit Yelp for Menu information")
                    .setMessage("Are you sure you want to navigate to Yelp? Note: Yelp may not have menu information for this restaurant.")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://www.yelp.com/menu/" + restID));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alert.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
