package oscar.com.intentservie;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

public class IntentServiceOperacion extends IntentService {
    public  IntentServiceOperacion()
    {
        super("IntentServiceOperacion");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent i) {
        double n=i.getExtras().getDouble("numero");
        SystemClock.sleep(5000);
       // MainActivity.salida.append(n*n+"\n");
        Intent i2=new Intent();
        i2.setAction(MainActivity.ReceptorOperacion.ACCTION_RES);
        i2.addCategory(Intent.CATEGORY_DEFAULT);
        i2.putExtra("resultado",n*n);
        sendBroadcast(i2);
    }
}
