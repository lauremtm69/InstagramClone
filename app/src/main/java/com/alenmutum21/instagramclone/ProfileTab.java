package com.alenmutum21.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText profileName,profileBio,profession,hobbies,sport;
    private Button update;
    private ProgressBar loading;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        profileName = view.findViewById(R.id.profileName);
        profileBio = view.findViewById(R.id.profileBio);
        profession = view.findViewById(R.id.profession);
        hobbies = view.findViewById(R.id.hobbies);
        sport = view.findViewById(R.id.sport);
        update = view.findViewById(R.id.update);
        loading = view.findViewById(R.id.loading);

        final ParseUser parseUser = ParseUser.getCurrentUser();


        if (parseUser.get("profileName") == null || parseUser.get("profileBio") == null || parseUser.get("profession") == null ||
                parseUser.get("hobbies") == null || parseUser.get("sport") == null) {
            profileName.setText("");
            profileBio.setText("");
            profession.setText("");
            hobbies.setText("");
            sport.setText("");
        } else {
            profileName.setText(parseUser.get("profileName").toString());
            profileBio.setText(parseUser.get("profileBio").toString());
            profession.setText(parseUser.get("profession").toString());
            hobbies.setText(parseUser.get("hobbies").toString());
            sport.setText(parseUser.get("sport").toString());
        }


        if (profileName.getText().toString().equals("") || profileBio.getText().toString().equals("") ||
                profession.getText().toString().equals("") || hobbies.getText().toString().equals("")
                || sport.getText().toString().equals("")) {
            FancyToast.makeText(getContext(), "please update all your profile fields", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
        }


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                parseUser.put("profileName", profileName.getText().toString());
                parseUser.put("profileBio", profileBio.getText().toString());
                parseUser.put("profession", profession.getText().toString());
                parseUser.put("hobbies", hobbies.getText().toString());
                parseUser.put("sport", sport.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(getContext(), "profile updated succesfully ", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            loading.setVisibility(View.GONE);

                        } else {
                                FancyToast.makeText(getContext(), "error " + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                loading.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        return view;
    }

}
