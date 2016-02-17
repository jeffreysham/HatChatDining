package com.jeffreysham.hatchatdining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import com.trnql.smart.base.SmartCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class MainActivity extends SmartCompatActivity implements AdapterView.OnItemSelectedListener{

    private Context context = this;

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Save food preference.
        SharedPreferences pref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String phoneNumber = pref.getString("phone number", null);
        String name = pref.getString("name", null);
        String desc = pref.getString("description", null);
        int age = pref.getInt("age", 0);
        String cuisine = parent.getItemAtPosition(position).toString();

        if (cuisine.equals("Surprise Me")) {
            cuisine = "";
        }

        if (phoneNumber != null && name != null
                && desc != null && age > 0) {
            getPeopleManager().setUserToken(phoneNumber);
            String dataString = "";
            JSONObject object = new JSONObject();
            try {
                object.put("name", name);
                object.put("number", phoneNumber);
                object.put("description", desc);
                object.put("age", age);
                object.put("cuisine", cuisine);
                dataString = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getPeopleManager().setDataPayload(dataString);

            pref.edit().putString("name", name).apply();
            pref.edit().putString("phone number", phoneNumber).apply();
            pref.edit().putString("description", desc).apply();
            pref.edit().putInt("age", age).apply();
            pref.edit().putString("cuisine", cuisine).apply();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAppData().setApiKey("afa2ba5f-818a-4756-9c23-282da864593f");

        startSmartServices(true);

        //Set product name to this application
        getPeopleManager().setSearchRadius(5000);

        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String phoneNumber = pref.getString("phone number", null);
        String name = pref.getString("name", null);
        String desc = pref.getString("description", null);
        int age = pref.getInt("age", 0);
        String cuisine = pref.getString("cuisine", "");

        if (phoneNumber == null || name == null
                || desc == null || age <= 0) {
            getUserPhoneNumber();
        } else {
            getPeopleManager().setUserToken(phoneNumber);
            String dataString = "";
            JSONObject object = new JSONObject();
            try {
                object.put("name", name);
                object.put("number", phoneNumber);
                object.put("description", desc);
                object.put("age", age);
                object.put("cuisine", cuisine);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getPeopleManager().setDataPayload(dataString);
            Log.i("test", "The cuisine: " + cuisine);
        }

        //Go to NearbyPeopleActivity
        ImageButton findPeopleButton = (ImageButton) findViewById(R.id.toPeopleButton);
        findPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NearbyPersonPickerActivity.class);
                startActivity(intent);
            }
        });

        //Go to NearbyRestaurantActivity
        ImageButton findRestaurantButton = (ImageButton) findViewById(R.id.toRestaurantButton);
        findRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NearbyRestaurantPickerActivity.class);
                startActivity(intent);
            }
        });

        Spinner cuisineSpinner = (Spinner) findViewById(R.id.cuisine_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cuisine_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpinner.setAdapter(adapter);
        cuisineSpinner.setOnItemSelectedListener(this);
    }

    public void getUserPhoneNumber() {
        LayoutInflater li = LayoutInflater.from(this);
        View alertView = li.inflate(R.layout.phone_prompt, null);

        AlertDialog.Builder getInfoDialog = new AlertDialog.Builder(this);
        getInfoDialog.setView(alertView);
        final EditText nameInput = (EditText) alertView.findViewById(R.id.nameInput);
        final EditText userInput = (EditText) alertView.findViewById(R.id.phoneNumberInput);
        final EditText descInput = (EditText) alertView.findViewById(R.id.phoneDescriptionInput);
        final EditText ageInput = (EditText) alertView.findViewById(R.id.ageInput);

        getInfoDialog.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setUserPhoneNumber(nameInput.getText().toString(), userInput.getText().toString(),
                                descInput.getText().toString(), ageInput.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        getInfoDialog.create().show();
    }

    public void setUserPhoneNumber(String name, String number, String description, String age) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String realPhoneNumber = "";
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(number, "US");
            realPhoneNumber = "" + phoneNumber.getNationalNumber();
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        int theAge = 0;

        try {
            theAge = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (realPhoneNumber.length() > 0 && name.length() > 0
                && description.length() > 0 && theAge > 0) {
            getPeopleManager().setUserToken(realPhoneNumber);
            String dataString = "";
            JSONObject object = new JSONObject();
            try {
                object.put("name", name);
                object.put("number", realPhoneNumber);
                object.put("description", description);
                object.put("age", theAge);
                object.put("cuisine", "");
                dataString = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getPeopleManager().setDataPayload(dataString);

            SharedPreferences pref = this.getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
            pref.edit().putString("name", name).apply();
            pref.edit().putString("phone number", realPhoneNumber).apply();
            pref.edit().putString("description", description).apply();
            pref.edit().putInt("age", theAge).apply();
            pref.edit().putString("cuisine", "").apply();

        } else {
            Toast.makeText(this, "Error with input.", Toast.LENGTH_SHORT).show();
            getUserPhoneNumber();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            editSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void editSettings() {
        LayoutInflater li = LayoutInflater.from(this);
        View alertView = li.inflate(R.layout.settings_prompt, null);

        AlertDialog.Builder getInfoDialog = new AlertDialog.Builder(this);
        getInfoDialog.setView(alertView);
        final EditText nameInput = (EditText) alertView.findViewById(R.id.editNameInput);
        final EditText descInput = (EditText) alertView.findViewById(R.id.editDescriptionInput);
        final EditText ageInput = (EditText) alertView.findViewById(R.id.editAgeInput);

        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String desc = pref.getString("description", "");
        String name = pref.getString("name", "");
        int age = pref.getInt("age", 0);

        if (desc.length() > 0) {
            descInput.setText(desc);
        }

        if (name.length() > 0) {
            nameInput.setText(name);
        }

        if (age > 0) {
            ageInput.setText(age + "");
        }

        getInfoDialog.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setEditedInformation(nameInput.getText().toString(),
                                descInput.getText().toString(), ageInput.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        getInfoDialog.create().show();
    }

    public void setEditedInformation(String name, String description, String age) {
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);

        if (name.length() > 0) {
            String tempNumber = pref.getString("phone number", null);
            String tempDesc = pref.getString("description", "");
            String cuisine = pref.getString("cuisine", "");
            int tempAge = pref.getInt("age", 0);
            getPeopleManager().setUserToken(tempNumber);

            String dataString = "";
            JSONObject object = new JSONObject();
            try {
                object.put("name", name);
                object.put("number", tempNumber);
                object.put("description", tempDesc);
                object.put("age", tempAge);
                object.put("cuisine", cuisine);
                dataString = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getPeopleManager().setDataPayload(dataString);

            pref.edit().putString("name", name).apply();
        }

        if (description.length() > 0) {
            String tempNumber = pref.getString("phone number", "");
            String tempName = pref.getString("name", "");
            String cuisine = pref.getString("cuisine", "");
            int tempAge = pref.getInt("age", 0);
            getPeopleManager().setUserToken(tempNumber);

            String dataString = "";
            JSONObject object = new JSONObject();
            try {
                object.put("name", tempName);
                object.put("number", tempNumber);
                object.put("description", description);
                object.put("age", tempAge);
                object.put("cuisine", cuisine);
                dataString = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getPeopleManager().setDataPayload(dataString);

            pref.edit().putString("description", description).apply();
        }

        if (age.length() > 0) {
            String tempNumber = pref.getString("phone number", "");
            String tempName = pref.getString("name", "");
            String cuisine = pref.getString("cuisine", "");
            String tempDesc = pref.getString("description", "");
            getPeopleManager().setUserToken(tempNumber);

            String dataString = "";
            JSONObject object = new JSONObject();
            try {
                object.put("name", tempName);
                object.put("number", tempNumber);
                object.put("description", tempDesc);
                object.put("age", Integer.parseInt(age));
                object.put("cuisine", cuisine);
                dataString = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NumberFormatException n) {
                n.printStackTrace();
            }

            getPeopleManager().setDataPayload(dataString);

            pref.edit().putInt("age", Integer.parseInt(age)).apply();
        }
    }

}
