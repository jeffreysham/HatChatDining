package com.jeffreysham.hatchatdining;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class HandleRestaurantChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_restaurant_choice);
        Bundle extras = getIntent().getExtras();
        final String number = extras.getString("number");
        String desc = extras.getString("description");
        String name = extras.getString("name");
        String photoURL = extras.getString("photoURL");
        final String address = extras.getString("address");
        final double lat = extras.getDouble("latitude");
        final double lon = extras.getDouble("longitude");

        TextView numberView = (TextView)findViewById(R.id.number_view);
        TextView addressView = (TextView)findViewById(R.id.address_view);
        TextView descriptionView = (TextView)findViewById(R.id.rest_description);
        TextView nameView = (TextView)findViewById(R.id.rest_name_view);
        SimpleDraweeView photoView = (SimpleDraweeView)findViewById(R.id.rest_view);
        ImageButton callButton = (ImageButton) findViewById(R.id.call_button);
        ImageButton mapButton = (ImageButton) findViewById(R.id.map_button);

        final Context context = this;

        if (photoURL != null) {
            Uri uri = Uri.parse(photoURL);
            photoView.setImageURI(uri);
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

    }
}
