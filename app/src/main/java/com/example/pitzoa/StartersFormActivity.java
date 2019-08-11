package com.example.pitzoa;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartersFormActivity extends AppCompatActivity {

    int quantity = 1;

    private Button submitOrder;
    String addSmallStatus,addRegularStatus,addLargeStatus;
    String selectedFlavour, userName,userAddress, userNumber;
    int selectedPrice;


    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starters_form);

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
        RetrievePrice(); // to retrieve user selected price according to Flavour;

    }


    private void CheckOut() {

        // Figure out if the user want one Piece
        CheckBox onePcsCheckBox = (CheckBox) findViewById(R.id.one_pcs);
        boolean hasOnePcs = onePcsCheckBox.isChecked();

        // Figure out if the user want one Pieces
        CheckBox twoPcsCheckBox = (CheckBox) findViewById(R.id.two_pcs);
        boolean hasTwoPcs = twoPcsCheckBox.isChecked();

        // Figure out if the user want one Pieces
        CheckBox fourPcsCheckBox = (CheckBox) findViewById(R.id.four_pcs);
        boolean hasFourPcs = fourPcsCheckBox.isChecked();

        if(hasOnePcs|hasTwoPcs|hasFourPcs){
            int price = calculatePrice(hasOnePcs, hasTwoPcs,hasFourPcs);
            int quantity = calculateQuantity(hasOnePcs,hasTwoPcs,hasFourPcs);
            String priceMessage = createOrderSummary(price, hasOnePcs, hasTwoPcs,hasFourPcs,quantity);

            // To submit the order through SMS.
            Uri uri = Uri.parse("smsto:03453235126");
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", priceMessage);
            startActivity(it);
        }else{
            Toast.makeText(this, "Kindly Select the No. of Pcs you want...!", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculatePrice(boolean addOnePcs, boolean addTwoPcs, boolean addFourPcs) {
        int basePrice = 0;
        //int newPrice = selectedPrice;


        if (addOnePcs) {
            basePrice += selectedPrice;
        }
        if (addTwoPcs) {
            basePrice = basePrice + (selectedPrice * 2);
        }
        if (addFourPcs) {
            basePrice = basePrice + (selectedPrice * 4);
        }
        return basePrice;
    }

    private int calculateQuantity(boolean addOnePcs, boolean addTwoPcs, boolean addFourPcs){
        int baseQuantity = 0;

        if (addOnePcs) {
            baseQuantity += 1;
        }
        if (addTwoPcs) {
            baseQuantity += 2;
        }
        if (addFourPcs) {
            baseQuantity += 4;
        }
        return quantity * baseQuantity;
    }

    private String createOrderSummary(int price, boolean addOnePcs, boolean addTwoPcs, boolean addFourPcs, int quantity) {

        if(addOnePcs){
            addSmallStatus="Yes";
        }else{
            addSmallStatus="No";
        }
        if(addTwoPcs){
            addRegularStatus="Yes";
        }else{
            addRegularStatus="No";
        }if(addFourPcs){
            addLargeStatus="Yes";
        }else{
            addLargeStatus="No";
        }


        String priceMessage = "Name: " + userName;

        priceMessage += "\nAddress: " + userAddress ;
        priceMessage += "\nMobile Number: " + userNumber ;
        priceMessage += "\nFlavour: " + selectedFlavour;
        priceMessage += "\nNo. of Pcs: " + quantity; // No. of pcs according to price should be here
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

    private void RetrievePrice() {

        RootRef.child("Users").child(currentUserID).child("prices")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("price") )){ // if user is updated selected Flavour then

                            String retrievePrice = dataSnapshot.child("price").getValue().toString();
                            // retrieveFlavour will be shown to message withe the name selectedFlavour
                            selectedPrice= Integer.valueOf(retrievePrice) ;
                        }else{ // if none of these exist
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
