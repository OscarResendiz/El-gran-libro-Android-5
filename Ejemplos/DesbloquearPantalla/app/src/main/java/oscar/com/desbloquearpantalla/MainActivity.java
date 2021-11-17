package oscar.com.desbloquearpantalla;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private Calendar hora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void prueba(View view)
    {
        hora=Calendar.getInstance();
        hora.setTime(new Date());
        //android.text.format.Time hora = new android.text.format.Time(android.text.format.Time.getCurrentTimezone());
        hora.set(Calendar.MINUTE,hora.get(Calendar.MINUTE)+1);
        MiTarea tarea=new MiTarea();
        tarea.execute(60);
    }
    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }
    class MiTarea extends AsyncTask<Integer, Integer, Integer>
    {
        private ProgressDialog progreso;
        @Override
        protected  void onPreExecute()
        {
            //se llama antes de ejecutar la tarea en segundo plano
            progreso=new ProgressDialog(MainActivity.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progreso.setMessage("Calculando....");
            progreso.setCancelable(true);
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MiTarea.this.cancel(true);
                }
            });
            progreso.setMax(100);
            progreso.setProgress(0);
            progreso.show();
        }
        @Override
        protected Integer doInBackground(Integer... n)
        {
            //Ejecuta la tarea en segundo plano
            //return factorial(n[0]);

            int res=1;
            for(int i=1;i<n[0] &&!isCancelled();i++)
            {
                res*=i;
                SystemClock.sleep(1000);
                publishProgress(i*100/n[0]);
            }
            return res;
        }
        @Override
        protected void onProgressUpdate(Integer... porc)
        {
            //se llama cuando hay que actualizar e informar del progreso de a tarea
            progreso.setProgress(porc[0]);
        }
        @Override
        protected void  onPostExecute(Integer res)
        {
            //se llama cuanto termina la tarea
            progreso.dismiss();
            unlockScreen();
            //aqui llamo a l encendicdo de la pantalla
//            salida.append(res+"\n");
        }
        @Override
        protected void onCancelled()
        {
            //se llama si se cancela la tarea
//            salida.append("Cancelado\n");
        }
    }
}
