package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.util.ArrayList;

public class NewProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "NewProfileActivity";

    private ArrayList<Profile> allProfiles;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        allProfiles = ProfileDBHelper.readAllProfiles(this);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private boolean doesProfileExist(Profile newProfile) {
        for (Profile existingProfile : allProfiles) {
            if (newProfile.getTeamNumber() == existingProfile.getTeamNumber()) {
                return true;
            }
        }

        return false;
    }

    public void createProfileOnClick(View view) {
        boolean allDataValid = true;

        // Get the team number from the interface
        EditText teamNumberInput = (EditText) findViewById(R.id.team_number);
        String teamNumberString = teamNumberInput.getText().toString().trim();

        if (teamNumberString.isEmpty()) {
            allDataValid = false;
            teamNumberInput.setError("Can't be empty.");

        } else if (Integer.parseInt(teamNumberString) < 0) {
            allDataValid = false;
            teamNumberInput.setError("Can't be negative.");
        }

        if (allDataValid) {
            int teamNumber = Integer.parseInt(teamNumberString);
            String robotFunction = ((Spinner) findViewById(R.id.robot_function))
                    .getSelectedItem().toString();

            Profile profile = new Profile(teamNumber, robotFunction);

            if (doesProfileExist(profile)) {
                allDataValid = false;
                teamNumberInput.setError("Team " + teamNumber + " already exists.");
            } else {
                long profileID = ProfileDBHelper.writeProfile(this, profile);
                startProfileActivity(profileID);
            }
        }
    }

    private void startProfileActivity(long profileID) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.INTENT_PROFILE_ID, profileID);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting ProfileActivity");
    }

}
