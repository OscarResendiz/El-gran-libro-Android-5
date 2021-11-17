package oscar.com.intentservie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class ServicioOperacion extends Service {
    @Override
    public int onStartCommand(Intent i, int flags, int idArranque)
    {
        double n=i.getExtras().getDouble("numero");
        SystemClock.sleep(5000);
        MainActivity.salida.append(n*n+"\n");
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
}
