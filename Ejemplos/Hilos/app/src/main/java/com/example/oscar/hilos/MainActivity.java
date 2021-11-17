package com.example.oscar.hilos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private EditText entrada;
private TextView salida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrada=(EditText)findViewById(R.id.entrada);
        salida=(TextView)findViewById(R.id.salida);
        entrada.setText("5");
        salida.setText("");
    }
    public void  calculaOperacion(View view)
    {
        int n=Integer.parseInt(entrada.getText().toString());
        salida.append((n+"!= "));
        int res=factorial(n);
        salida.append(res+"\n");
    }
    public void  calculaOperacionthead(View view)
    {
        int n=Integer.parseInt(entrada.getText().toString());
        salida.append((n+"!= "));
        MiThread thread=new MiThread(n);
        thread.start();
    }
    public void  calucloAsyncTask(View view)
    {
        int n=Integer.parseInt(entrada.getText().toString());
        salida.append((n+"!= "));
        MiTarea tarea=new MiTarea();
        tarea.execute(n);
    }
    public int factorial(int n)
    {
        int res=1;
        for(int i=1;i<n;i++)
        {
           res*=i;
            SystemClock.sleep(1000);
        }
        return res;
    }
    class  MiThread extends Thread
    {
        private int n, res;
        public  MiThread(int n)
        {
            this.n=n;
        }
            @Override
        public void run()
            {
                res=factorial(n);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        salida.append(res+"\n");
                    }
                });
            }
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
            salida.append(res+"\n");
        }
        @Override
        protected void onCancelled()
        {
            //se llama si se cancela la tarea
            salida.append("Cancelado\n");
        }
    }
}
