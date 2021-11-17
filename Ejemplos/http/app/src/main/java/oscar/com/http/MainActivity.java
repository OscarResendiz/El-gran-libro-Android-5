package oscar.com.http;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private EditText entrada;
    private TextView salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrada = (EditText) findViewById(R.id.entrada);
        salida = (TextView) findViewById(R.id.salida);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    public void buscar(View view) {
        try {
            String palabras = entrada.getText().toString();
//            String resultado = resultadoGoogle(palabras);
            salida.append(palabras + "-- ");
            new BuscarGoogle().execute(palabras);
        } catch (Exception e) {
            salida.append("Error al conectar: " + e.getMessage());
        }
    }

    private String resultadoGoogle(String palabras) {
        String devuelve = "";
        String pagina = "";
        try {
            URL uri = new URL("http://www.google.es/search?hl=es&q=\"" + URLEncoder.encode(palabras, "UTF-8") + "\"");
            HttpURLConnection conexion = (HttpURLConnection) uri.openConnection();
            conexion.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 6.1)");
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                while (linea != null) {
                    pagina += linea;
                    linea = reader.readLine();
                }
                reader.close();
                int ini = pagina.indexOf("Aproximadamente");
                if (ini != -1) {
                    int fin = pagina.indexOf(" ", ini + 16);
                    devuelve = pagina.substring(ini + 16, fin);
                } else {
                    devuelve = "no encontrado";
                }
            } else {
                salida.append("ERROR: " + conexion.getResponseMessage() + "\n");
            }
            conexion.disconnect();
        } catch (Exception e) {
            devuelve = "Error al conectar: " + e.getMessage();
        }
        return devuelve;
    }
    class BuscarGoogle extends AsyncTask<String,Void,String>
    {
        private ProgressDialog progreso;
        @Override
        protected void onPreExecute()
        {
            progreso=new ProgressDialog(MainActivity.this);
            progreso.setProgressStyle((ProgressDialog.STYLE_SPINNER));
            progreso.setMessage("Accediendo a google");
            progreso.setCancelable(false);
            progreso.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            try
            {
                return  resultadoGoogle(strings[0]);
            }
            catch (Exception e)
            {
                cancel(true);
                Log.e("HTTP:" ,e.getMessage(),e);
                return  null;
            }
        }
        @Override
        protected void  onPostExecute(String res)
        {
            progreso.dismiss();
            salida.append(res+"\n");
        }
        @Override
        protected void  onCancelled()
        {
            progreso.dismiss();
            salida.append("Error al conectar\n");
        }
    }
}
