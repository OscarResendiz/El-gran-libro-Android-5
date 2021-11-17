package oscar.com.contentcalllog;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[]TIPO_LLAMADA={"","entrante","saliente","perdida","mensaje de voz","cancelada","lista bloqueados"};
        TextView salida=(TextView)findViewById(R.id.salida);
        Uri llamadas=Uri.parse("content://call_log/calls");
        //consulta con el primer metodo
//        Cursor cursor=getContentResolver().query(llamadas,null,null,null,null);
        //consulta con el segundo
        String[] proyeccion=new String[]{Calls.DATE,Calls.DURATION,Calls.NUMBER,Calls.TYPE};
        String[] argSelec=new String[]{"1"};
        Cursor cursor=getContentResolver().query(
                llamadas // Uri con elcontenetprovider
                ,proyeccion //columnas que nos interesan (parte de select
                ,"type=?" //consulta where
                ,argSelec //´parametros de la consulta
                ,"date desc" //parte del order by
                );
        while (cursor.moveToNext())
        {
            java.util.Date d=new java.util.Date(cursor.getLong(cursor.getColumnIndex(Calls.DATE)));
            salida.append("\n"
                    + android.text.format.DateFormat.format( "dd/MM/yy k:mm (",d)
                    +cursor.getString(cursor.getColumnIndex(Calls.DURATION))+") "
                    +cursor.getString(cursor.getColumnIndex(Calls.NUMBER))+", "
                    +TIPO_LLAMADA[Integer.parseInt(cursor.getString(cursor.getColumnIndex(Calls.TYPE)))]
             );
        }
    }
    public void agregarLlamada(View view)
    {
        ContentValues valores=new ContentValues();
        valores.put(Calls.DATE,new Date().getTime());
        valores.put(Calls.NUMBER,"566666666");
        valores.put(Calls.DURATION,"55");
        valores.put(Calls.TYPE,Calls.INCOMING_TYPE);
        Uri nuevoElemento=getContentResolver().insert(Calls.CONTENT_URI,valores);
        }
}
