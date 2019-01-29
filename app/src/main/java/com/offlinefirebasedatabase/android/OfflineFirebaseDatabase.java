package com.offlinefirebasedatabase.android;

import android.app.Application;

public class OfflineFirebaseDatabase extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        //For offline mode we need to set to true
        //When you enable disk persistence, your app writes the data locally to the device so your app can maintain state while offline, even if the user or operating system restarts the app.
        com.google.firebase.database.FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
