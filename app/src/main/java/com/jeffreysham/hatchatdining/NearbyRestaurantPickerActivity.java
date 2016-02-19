package com.jeffreysham.hatchatdining;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class NearbyRestaurantPickerActivity extends AppCompatActivity implements LocationListener{

    List<RestaurantCardModel> restaurantList;
    List<RestaurantCardModel> finalRestaurantList;
    private CardContainer cardContainer;
    private LocationManager locationManager;
    private Location currentLocation;
    private String provider;
    private String cuisine;
    private Context context = this;
    private int modelCount;
    private ImageView hatChatSpinner;
    private Button shuffleButton;

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        findRestaurants(cuisine);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void startAnimation() {
        RotateAnimation rotation = new RotateAnimation(-45f, 45f, Animation.RELATIVE_TO_SELF,.5f, Animation.RELATIVE_TO_SELF,.5f);
        rotation.setDuration(1000);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatMode(Animation.REVERSE);
        rotation.setRepeatCount(Animation.INFINITE);

        hatChatSpinner.startAnimation(rotation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_restaurant_picker);
        restaurantList = new ArrayList<>();
        finalRestaurantList = new ArrayList<>();
        cardContainer = (CardContainer) findViewById(R.id.restaurant_card_container);
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        cuisine = pref.getString("cuisine", "");

        hatChatSpinner = (ImageView) findViewById(R.id.spinning_hat);
        startAnimation();
        shuffleButton = (Button) findViewById(R.id.shuffle_button);
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        if (!isNetworkAvailable()) {
            Log.i("test", "network not available");
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
            Log.i("test", "network available");
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(!gps_enabled && !network_enabled) {
                Log.i("test", "gps not available");
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
                Log.i("test", "gps available");
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);

                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    currentLocation = location;
                    findRestaurants(cuisine);
                } else {
                    //Set Location for emulator
                    currentLocation = new Location(provider);
                    currentLocation.setLatitude(39.330531);
                    currentLocation.setLongitude(-76.616709);
                    findRestaurants(cuisine);
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

    public void findRestaurants(String cuisine) {
        Log.i("test", "findRestaurants");
        YelpAPIFactory apiFactory = new YelpAPIFactory("q4aBeYvODqkXIhDkhWlI4w", "OhIuI9IGalx18eR-smSXHimletU",
                "a3lYe77-Jaatw7LLRsGyqr1r4xophYBp", "GqwClJMg6d7wThGy3fTCHCYPy3o");
        YelpAPI yelpAPI = apiFactory.createAPI();
        Map<String, String> params = new HashMap<>();

        params.put("limit", "20");
        //params.put("latitude", currentLocation.getLatitude() + "");
        //params.put("longitude", currentLocation.getLongitude() + "");

        Call<SearchResponse> searchResponseCall;

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                Log.i("test", "in callback");
                SearchResponse searchResponse = response.body();

                for (Business business: searchResponse.businesses()) {
                    String name = business.name();
                    double rating = business.rating();
                    ArrayList<String> addressList = business.location().displayAddress();
                    String address = "";
                    for (int i = 0; i < addressList.size(); i++) {
                        address += addressList.get(i) + " ";
                    }
                    String photoURL = business.imageUrl();
                    double lat = business.location().coordinate().latitude();
                    double lon = business.location().coordinate().longitude();

                    Location tempLoc = new Location(provider);
                    tempLoc.setLatitude(lat);
                    tempLoc.setLongitude(lon);

                    double distance = currentLocation.distanceTo(tempLoc);

                    String description = business.snippetText();
                    String number = business.phone();
                    String mobileURL = business.mobileUrl();
                    RestaurantCardModel model = new RestaurantCardModel(name, description, rating, distance,
                            address, photoURL,number, mobileURL);
                    restaurantList.add(model);
                }

                setupCards();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "Error retrieving restaurants.", Toast.LENGTH_SHORT).show();
            }
        };
        if (cuisine.length() > 0) {
            params.put("term", cuisine);
        } else {
            params.put("term", "food");
        }

        CoordinateOptions coordinateOptions = CoordinateOptions.builder()
                .latitude(currentLocation.getLatitude())
                .longitude(currentLocation.getLongitude()).build();

        searchResponseCall = yelpAPI.search(coordinateOptions, params);
        searchResponseCall.enqueue(callback);
    }

    public void setupCards() {

        Log.i("test", "setupCards");
        RestaurantCardStackAdapter adapter = new RestaurantCardStackAdapter(this);
        modelCount = restaurantList.size()-1;

        for (int i = 0; i < restaurantList.size(); i++) {
            RestaurantCardModel model = restaurantList.get(i);
            model.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                @Override
                public void onLike() {
                    //Do nothing
                    //This is swipe left
                    modelCount--;
                    if (modelCount < 0) {
                        shuffleButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onDislike() {
                    //Choose between calling or navigation
                    //This is swipe right
                    RestaurantCardModel tempModel = restaurantList.get(modelCount);

                    finalRestaurantList.add(tempModel);

                    modelCount--;
                    if (modelCount < 0) {
                        shuffleButton.setVisibility(View.VISIBLE);
                    }
                }
            });

            adapter.add(model);
        }

        hatChatSpinner.setAnimation(null);
        hatChatSpinner.setVisibility(View.GONE);
        cardContainer.setAdapter(adapter);

    }

    public void shuffle() {
        Random random = new Random();

        if (finalRestaurantList.size() > 0) {
            int value = random.nextInt(finalRestaurantList.size());

            RestaurantCardModel tempModel = finalRestaurantList.get(value);
            Intent intent = new Intent(context, HandleRestaurantChoiceActivity.class);
            intent.putExtra("name", tempModel.getTitle());
            intent.putExtra("address", tempModel.getAddress());
            intent.putExtra("description", tempModel.getDescription());
            intent.putExtra("photoURL", tempModel.getPhotoURL());
            intent.putExtra("number", tempModel.getNumber());
            intent.putExtra("latitude", currentLocation.getLatitude());
            intent.putExtra("longitude", currentLocation.getLongitude());
            intent.putExtra("mobileURL", tempModel.getMobileURL());
            startActivity(intent);

        } else {
            Toast.makeText(this, "Please choose at least 1 restaurant next time.", Toast.LENGTH_SHORT).show();
        }

    }
}
