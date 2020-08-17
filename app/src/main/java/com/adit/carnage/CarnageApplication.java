package com.adit.carnage;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.adit.carnage.services.SecretService;

public class CarnageApplication extends Application implements LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "App onCreated", Toast.LENGTH_SHORT).show();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded(){
        //this.startService(new Intent(this, SecretService.class));
        Toast.makeText(this, "App onSTOP event", Toast.LENGTH_SHORT).show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onAppDestroyed(){
//        this.startService(new Intent(this, SecretService.class));
        Toast.makeText(this, "App onDESTROY event", Toast.LENGTH_SHORT).show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded(){
        //this.stopService(new Intent(this, SecretService.class));
        Toast.makeText(this, "App onSTART event", Toast.LENGTH_SHORT).show();
    }
}
