package oscar.com.despertador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceptorArranque extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,ServicioMusica.class));
    }
}
