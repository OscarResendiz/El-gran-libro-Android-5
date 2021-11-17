package oscar.com.serviciomusica;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class Servicio extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate()
    {

    }
    @Override
    public int onStartCommand(Intent intencion, int flags, int idArran)
    {
        return  START_STICKY;
    }
    @Override
    public void onDestroy()
    {

    }
}
