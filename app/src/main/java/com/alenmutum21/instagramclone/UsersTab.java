package com.alenmutum21.instagramclone;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment {
    private ListView listView;
    private TextView waitingText;
    private ArrayList arrayList;
    private ArrayAdapter arrayAdapter;
    private AlertDialog.Builder builder;

    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView = view.findViewById(R.id.listView);
        waitingText = view.findViewById(R.id.waitingText);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);

        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                if (e == null){
                    if (users.size() > 0){
                        for (ParseUser user : users){
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        listView.setVisibility(View.VISIBLE);
                        waitingText.setVisibility(View.GONE);

                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(),UsersPostsActivity.class);
                intent.putExtra("username", arrayList.get(position).toString());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
                userParseQuery.whereEqualTo("username",arrayList.get(position));
                userParseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        if (object != null && e == null){
                            final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                            prettyDialog.setTitle(object.get("username")+ "'s Info")
                                    .setMessage("Bio: "+object.get("profileBio") + "\n" +
                                            "Profession: "+object.get("profession") +"\n" +
                                            "Hobbies: "+object.get("hobbies")+"\n" +
                                            "Fav Sport: "+object.get("sport")+"\n")
                                    .setIcon(R.drawable.ic_person_black_24dp)
                                    .addButton(
                                            "ok",
                                            R.color.pdlg_color_white,
                                            R.color.pdlg_color_green,
                                            new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    prettyDialog.dismiss();
                                                }
                                            }
                                    )
                            .show();
                        }
                    }
                });

                return true;
            }
        });


        return view;
    }
}
