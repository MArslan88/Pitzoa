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
public class StartersFragment extends Fragment {

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    public StartersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View startersFragmentView =  inflater.inflate(R.layout.fragment_starters, container, false);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        // Create a list of words
        final ArrayList<Flavour> flavours = new ArrayList<Flavour>();
        flavours.add(new Flavour("garlic bread","Freshly baked French bread with garlic butter and herb toppings.",R.drawable.garlic_bread));
        flavours.add(new Flavour("garlic bread supreme","Four baked pieces of garlic bread smothered with melted, real mozzarella cheese.",R.drawable.garlic_bread_supreme));
        flavours.add(new Flavour("Potato skins","Baked potato sides with pure mozzarella cheese topped with tomatoes and special seasoning served with our scrumptious sour cream; all our own recipe.",R.drawable.potato_skins));
        flavours.add(new Flavour("spicy wedges","Crispy potato wedges covered with spicy herbs and seasonings.",R.drawable.spicy_wedges));
        flavours.add(new Flavour("bbq chicken spin roll","Behari masala marinated chicken chunks, onions and green chilies topped with mozzarella cheese.",R.drawable.bbq_chicken_spin_rolls3));
        flavours.add(new Flavour("behari chicken spin roll","Behari chicken chunks, sweet corn and jalapenos rolled in a light tortilla.",R.drawable.behari_spin_rolls));
        flavours.add(new Flavour("mix salad","A scrumptious variety of garden fresh vegetables that will tempt you to create your very own favorite salad topped with our classic dressings.",R.drawable.salad));
        flavours.add(new Flavour("chicken wings","Oven baked hot and spicy chicken wings.",R.drawable.chicken_wings));
        flavours.add(new Flavour("flaming wing","Juicy chicken wings, marinated in our peri peri sauce with just the right mix of spices. Served with chilli garlic mayo dip.",R.drawable.flaming_wings3));


        ListView listView = (ListView)startersFragmentView.findViewById(R.id.listView);
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
                if (currentFlavour.getmFlavourHeading() == "garlic bread"){
                    priceCall("165");
                }else if (currentFlavour.getmFlavourHeading() == "garlic bread supreme"){
                    priceCall("335");
                }else if (currentFlavour.getmFlavourHeading() == "Potato skins"){
                    priceCall("265");
                }else if (currentFlavour.getmFlavourHeading() == "spicy wedges"){
                    priceCall("265");
                }else if (currentFlavour.getmFlavourHeading() == "bbq chicken spin roll"){
                    priceCall("300");
                }else if (currentFlavour.getmFlavourHeading() == "behari chicken spin roll"){
                    priceCall("300");
                }else if (currentFlavour.getmFlavourHeading() == "mix salad"){
                    priceCall("500");
                }else if (currentFlavour.getmFlavourHeading() == "chicken wings"){
                    priceCall("300");
                }else if (currentFlavour.getmFlavourHeading() == "flaming wing"){
                    priceCall("265");
                }

                Toast.makeText(getActivity(), ""+currentFlavour.getmFlavourHeading(), Toast.LENGTH_SHORT).show();
            }
        });

        return startersFragmentView;
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
