package com.example.pitzoa;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class DealsFragment extends Fragment {

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    public DealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dealsFragmentView = inflater.inflate(R.layout.fragment_deals, container, false);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        // Create a list of words
        final ArrayList<Flavour> flavours = new ArrayList<Flavour>();
        flavours.add(new Flavour("buy 1 get 2 free","Buy a Large Pizza, Get a Regular and a Small Pizza FREE!.",R.drawable.b1g2));
        flavours.add(new Flavour("xxl special","1 XXL Pizza, 12 Potato Wedges, 2 Dip Sauce, 1 Large Drink.",R.drawable.xx_combo));
        flavours.add(new Flavour("pan 4 all","4 Personal Pan Pizzas, 4 Pcs Garlic Bread and 1.5 Ltr. Drink.",R.drawable.pan_4_all));
        flavours.add(new Flavour("wow deals","1 Personal Pan Pizza, 2 Pcs. Garlic Bread and 345ml soft Drink.",R.drawable.wow_deal_pro_img_new));

        ListView listView = (ListView)dealsFragmentView.findViewById(R.id.listView);
        final FlavourAdapter listViewAdapter = new FlavourAdapter(
                getActivity(),
                flavours
        );
        listView.setAdapter(listViewAdapter);

        // for listView click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Flavour currentFlavour = listViewAdapter.getItem(position);

                FlavourCall(currentFlavour.getmFlavourHeading());
                SentUserToStartersForm();
                if (currentFlavour.getmFlavourHeading() == "buy 1 get 2 free"){
                    priceCall("1650");
                }else if (currentFlavour.getmFlavourHeading() == "xxl special"){
                    priceCall("2599");
                }else if (currentFlavour.getmFlavourHeading() == "pan 4 all"){
                    priceCall("1299");
                }else if (currentFlavour.getmFlavourHeading() == "wow deals"){
                    priceCall("399");
                }

                Toast.makeText(getActivity(), ""+currentFlavour.getmFlavourHeading(), Toast.LENGTH_SHORT).show();
            }
        });

        return dealsFragmentView;
    }


    private void priceCall(String price) {
        HashMap<String,String> flavourMap = new HashMap<>();
        flavourMap.put("price",price);

        RootRef.child("Users").child(currentUserID).child("prices").setValue(flavourMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                        }
                    }
                });
    }

    private void SentUserToStartersForm() {
        Intent startersFormIntent = new Intent(getActivity(), StartersFormActivity.class);
        startActivity(startersFormIntent);
    }

    private void FlavourCall(String flavour) {
        HashMap<String,String> flavourMap = new HashMap<>();
        flavourMap.put("flavour",flavour);

        RootRef.child("Users").child(currentUserID).child("flavours").setValue(flavourMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                        }
                    }
                });
    }


}
