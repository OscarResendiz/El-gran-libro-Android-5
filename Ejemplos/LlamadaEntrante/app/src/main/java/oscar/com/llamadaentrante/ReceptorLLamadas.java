package oscar.com.llamadaentrante;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ReceptorLLamadas extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
/*        //sacamos informacion de la intencion
        String estado ="";
        String numero="";
        Bundle extras=intent.getExtras();
        if(extras!=null)
        {
            estado=extras.getString(TelephonyManager.EXTRA_STATE);
            if(estado.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                numero=extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                String info=estado+" "+ numero;
                Log.d("ReceptorAnuncio", info+" intent= "+intent);
                // Creamos notificacion
                NotificationManager nm=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notificacion=new Notification(R.mipmap.ic_launcher,"Llamada entrante",System.currentTimeMillis());
                PendingIntent intencionPendiente=PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0);
//                notificacion.setLatestEventInfo(context,"Llamada entrante",info,intencionPendiente);
                nm.notify(1,notificacion);
            }
            */
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                // prevent any other broadcast receivers from receiving broadcast
                // abortBroadcast();
                // Creamos notificacion
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notificacion = new Notification(R.mipmap.ic_launcher, "Nuevo mensaje: " + message, System.currentTimeMillis());
                PendingIntent intencionPendiente = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
//                notificacion.setLatestEventInfo(context,"Llamada entrante",info,intencionPendiente);
                nm.notify(1, notificacion);
            }
        }
    }
}

