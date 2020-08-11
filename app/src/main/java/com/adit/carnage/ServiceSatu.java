package com.adit.carnage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceSatu extends Service {
    public ServiceSatu() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getLocation(){

    }
}
