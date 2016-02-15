package com.jeffreysham.hatchatdining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.trnql.smart.base.SmartCompatActivity;
import com.trnql.smart.people.PersonEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class NearbyPersonPickerActivity extends SmartCompatActivity {

    private List<PersonEntry> peopleList;
    private List<PersonCardModel> peopleCardList;
    private CardContainer cardContainer;
    private Context context = this;
    private int modelCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_person_picker);

        Log.i("test", "person picker activity onCreate method");
        cardContainer = (CardContainer) findViewById(R.id.person_card_container);

        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

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
    protected void smartPeopleChange(List<PersonEntry> people) {
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String cuisine = pref.getString("cuisine", "");

        Log.i("test", "smart people change");

        if (cuisine.length() > 0) {
            //Cuisine matters
            peopleList = new ArrayList<>();
            for (int i = 0; i < people.size(); i++) {
                PersonEntry person = people.get(i);
                String dataString = person.getDataPayload();
                String theCuisine;
                try {
                    JSONObject jsonObject = new JSONObject(dataString);
                    theCuisine = jsonObject.getString("cuisine");
                } catch (JSONException e) {
                    e.printStackTrace();
                    theCuisine = "";
                }

                if (theCuisine.equals("") || theCuisine.equals(cuisine)) {
                    peopleList.add(person);
                }
            }
            showPeople();
        } else {
            if (people != null) {
                peopleList = people;
            } else {
                peopleList = new ArrayList<>();
            }
            showPeople();
        }

    }

    public void showPeople() {
        Log.i("test", "show people");
        PersonCardStackAdapter adapter = new PersonCardStackAdapter(this);
        modelCount = peopleList.size() - 1;
        peopleCardList = new ArrayList<>();
        for (int i = 0; i < peopleList.size(); i++) {
            PersonEntry p = peopleList.get(i);
            String dataString = p.getDataPayload();
            try {
                JSONObject jsonObject = new JSONObject(dataString);
                String name = jsonObject.getString("name");
                String number = jsonObject.getString("number");
                String desc = jsonObject.getString("description");
                int age = jsonObject.getInt("age");

                final PersonCardModel cardModel = new PersonCardModel(name, desc, number, age);
                cardModel.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                    @Override
                    public void onLike() {
                        Log.i("Swipe able Cards", "I dislike the card");
                        //Do nothing
                        modelCount--;
                    }

                    @Override
                    public void onDislike() {
                        Log.i("Swipe able Cards", "I like the card");
                        //Send a message or call
                        PersonCardModel tempModel = peopleCardList.get(modelCount);
                        Intent intent = new Intent(context, HandlePersonChoiceActivity.class);
                        intent.putExtra("name", tempModel.getTitle());
                        intent.putExtra("number", tempModel.getNumber());
                        intent.putExtra("age", tempModel.getAge());
                        intent.putExtra("description", tempModel.getDescription());
                        startActivity(intent);
                        modelCount--;
                    }
                });
                adapter.add(cardModel);
                peopleCardList.add(cardModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        cardContainer.setAdapter(adapter);
    }

}
