package com.example.pitzoa;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.net.Proxy.Type.HTTP;

public class FormActivity extends AppCompatActivity {

    int quantity = 1;

    private Button submitOrder;
    String addSmallStatus,addRegularStatus,addLargeStatus;
    String selectedFlavour, userName,userAddress, userNumber;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        submitOrder = (Button)findViewById(R.id.submit_button);

        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckOut();
            }
        });

        RetrieveUserInfo(); // to retrieve user information
        RetrieveFlavour(); // to retrieve user selected Flavour
    }

    private void CheckOut() {

        // Figure out if the user want small size pizza
        CheckBox smallSizeCheckBox = (CheckBox) findViewById(R.id.smallSize);
        boolean hasSmallSize = smallSizeCheckBox.isChecked();

        // Figure out if the user want regular size pizza
        CheckBox regularSizeCheckBox = (CheckBox) findViewById(R.id.regularSize);
        boolean hasRegularSize = regularSizeCheckBox.isChecked();

        // Figure out if the user want large size pizza
        CheckBox largeSizeCheckBox = (CheckBox) findViewById(R.id.largeSize);
        boolean hasLargeSize = largeSizeCheckBox.isChecked();

        if(hasSmallSize|hasRegularSize|hasLargeSize){
            int price = calculatePrice(hasSmallSize, hasRegularSize,hasLargeSize);
            String priceMessage = createOrderSummary(price, hasSmallSize, hasRegularSize,hasLargeSize);

            // To submit the order through SMS.
            Uri uri = Uri.parse("smsto:03453235126");
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", priceMessage);
            startActivity(it);
        }else{
            Toast.makeText(this, "Kindly Select the Pizza Size...!", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculatePrice(boolean addSmallSize, boolean addRegularSize, boolean addLargeSize) {
        int basePrice = 0;

        if (addSmallSize) {
            basePrice += 440;
        }
        if (addRegularSize) {
            basePrice += 1200;
        }
        if (addLargeSize) {
            basePrice += 1800;
        }
        return quantity * basePrice;
    }

    private String createOrderSummary(int price, boolean addSmallSize, boolean addRegularSize, boolean addLargeSize) {

        if(addSmallSize){
            addSmallStatus="Yes";
        }else{
            addSmallStatus="No";
        }
        if(addRegularSize){
            addRegularStatus="Yes";
        }else{
            addRegularStatus="No";
        }if(addLargeSize){
            addLargeStatus="Yes";
        }else{
            addLargeStatus="No";
        }

        String priceMessage = "Name: " + userName;

        priceMessage += "\nAddress: " + userAddress ;
        priceMessage += "\nMobile Number: " + userNumber ;
        priceMessage += "\nFlavour: " + selectedFlavour;
        priceMessage += "\nAdd Small Size? " + addSmallStatus;
        priceMessage += "\nAdd Regular Size? " + addRegularStatus;
        priceMessage += "\nAdd Regular Size? " + addLargeStatus;
        priceMessage += "\n\nTotal: Rs." + price;
        priceMessage += "\nThank you!";
        return priceMessage;
    }

    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("address")))){ // if user is updated his profile then

                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveUserAddress = dataSnapshot.child("address").getValue().toString();
                            String retrieveUserNumber = dataSnapshot.child("number").getValue().toString();

                            // retrieveUserName will be shown to userName EditText again
                            userName = retrieveUserName ;
                            userAddress = retrieveUserAddress;
                            userNumber = retrieveUserNumber;
                        }else{ // if none of these exist
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void RetrieveFlavour() {

        RootRef.child("Users").child(currentUserID).child("flavours")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("flavour") )){ // if user is updated selected Flavour then

                            String retrieveFlavour = dataSnapshot.child("flavour").getValue().toString();
                            // retrieveFlavour will be shown to message withe the name selectedFlavour
                            selectedFlavour= retrieveFlavour;
                        }else{ // if none of these exist
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


}
