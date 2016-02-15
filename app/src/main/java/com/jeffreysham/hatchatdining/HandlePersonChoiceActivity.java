package com.jeffreysham.hatchatdining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HandlePersonChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_person_choice);
        Bundle extras = getIntent().getExtras();
        final String number = extras.getString("number");
        String desc = extras.getString("description");
        final String name = extras.getString("name");
        int age = extras.getInt("age");

        final EditText messageText = (EditText) findViewById(R.id.message_text);
        ImageButton callButton = (ImageButton) findViewById(R.id.callButton);
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        TextView initalsView = (TextView) findViewById(R.id.person_initials_view);
        TextView nameView = (TextView) findViewById(R.id.name_view);
        TextView numberView = (TextView) findViewById(R.id.number_view);
        TextView ageView = (TextView) findViewById(R.id.age_view);
        TextView descView = (TextView) findViewById(R.id.description_view);

        final Context context = this;

        if (name != null && name.length() > 0) {
            nameView.setText(name);

            String[] nameArray = name.split(" ");
            if (nameArray.length == 1) {
                initalsView.setText(name.charAt(0));
            } else {
                initalsView.setText(nameArray[0].charAt(0) + nameArray[nameArray.length-1].charAt(0));
            }
        }

        if (number != null) {
            numberView.setText(number);
        }

        ageView.setText(age + "");

        if (desc != null) {
            descView.setText(desc);
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(context,
                            "Call failed, please try again later!",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String theMessageToSend = messageText.getText().toString().trim();

                if (theMessageToSend.length() > 0) {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, "HatChat Dining: " + theMessageToSend, null, null);
                        Toast.makeText(context, "SMS Sent to " + name + "!",
                                Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context,
                                "SMS failed, please try again later!",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please write a message before sending.")
                            .setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                }
            }
        });
    }
}
