package com.example.pitzoa;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Pitzoa extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
