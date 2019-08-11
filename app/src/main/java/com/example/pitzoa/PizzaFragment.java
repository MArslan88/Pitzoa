package com.example.pitzoa;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class PizzaFragment extends Fragment {

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

        public PizzaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pizzaFragmentView = inflater.inflate(R.layout.fragment_pizza, container, false);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        // Create a list of words
        final ArrayList<Flavour> flavours = new ArrayList<Flavour>();
        flavours.add(new Flavour("Seekh Kabab Overloaded","Topped with Seekh Kabab chunks on tantalizing blend of spicy chicken, onions, capsicum, green pepper and cheese.",R.drawable.seekh_kabab_overload));
        flavours.add(new Flavour("bbq buzz","Smoked chicken, sweet corn, onions and black olives with sweet and smoky BBQ sauce.",R.drawable.bbq_buzz));
        flavours.add(new Flavour("spicy ranch","Creamy base topped with chicken chunks, capsicum, onions and spicy peri sauce.",R.drawable.spicy_ranch));
        flavours.add(new Flavour("afghani tikka","Topped with Afghani Tikka chunks and onion on a special creamy pizza sauce.",R.drawable.afghani_tikka_img1));
        flavours.add(new Flavour("behari chicken","Behari masala marinated chicken chunks, onions and green chilies topped with mozzarella cheese.",R.drawable.behari_chicken));
        flavours.add(new Flavour("chicken arabia","Dip in to our succulent pieces of grilled chicken, green peppers, onions and fresh tomatoes.",R.drawable.chicken_arabia));

        ListView listView = (ListView)pizzaFragmentView.findViewById(R.id.listView);
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
                SentUserToForm();
                Toast.makeText(getActivity(), ""+currentFlavour.getmFlavourHeading(), Toast.LENGTH_SHORT).show();
            }
        });

        return pizzaFragmentView;
    }

    private void SentUserToForm() {
        Intent formIntent = new Intent(getActivity(), FormActivity.class);
        startActivity(formIntent);
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
