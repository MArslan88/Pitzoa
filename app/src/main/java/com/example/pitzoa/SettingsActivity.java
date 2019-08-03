package com.example.pitzoa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSEttings;
    private EditText userName, userAddress, userNumber;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        userName.setVisibility(View.INVISIBLE);  //  when user provide his name once the Name field will be INVISIBLE for future.

        updateAccountSEttings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });

        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("address")))){

                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveUserAddress = dataSnapshot.child("address").getValue().toString();
                            String retrieveUserNumber = dataSnapshot.child("number").getValue().toString();

                            // retrieveUserName, userAddress and userNumber will be shown to EditText again
                            userName.setText(retrieveUserName);
                            userAddress.setText(retrieveUserAddress);
                            userNumber.setText(retrieveUserNumber);
                        }else{
                            // if none of these exist
                            userName.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingsActivity.this, "Please update your information...!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void UpdateSettings() {
        String setUserName = userName.getText().toString();
        String setUserAddress = userAddress.getText().toString();
        String setUserNumber = userNumber.getText().toString();

        if(TextUtils.isEmpty(setUserName)){
            Toast.makeText(this, "Please write your Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(setUserAddress)){
            Toast.makeText(this, "Please write your Address", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(setUserNumber)){
            Toast.makeText(this, "Please write your Number", Toast.LENGTH_SHORT).show();
        }else{ // if all the fields are not empty then we will send this data to the FireBase database by using HashMap
            HashMap<String,String> profileMap = new HashMap<>();
            profileMap.put("uid",currentUserID);
            profileMap.put("name",setUserName);
            profileMap.put("address",setUserAddress);
            profileMap.put("number",setUserNumber);

            RootRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                SendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Profile update successfully...!", Toast.LENGTH_SHORT).show();
                            }else{// if any Error occure
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    private void InitializeFields() {
        updateAccountSEttings=(Button)findViewById(R.id.update_setting_button);
        userName = (EditText)findViewById(R.id.set_user_name);
        userAddress = (EditText)findViewById(R.id.set_user_address);
        userNumber = (EditText)findViewById(R.id.set_user_number);
    }
}
