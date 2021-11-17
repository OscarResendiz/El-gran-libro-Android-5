package oscar.com.servicioweblibros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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

    private String resultadoSW(String palabras) throws Exception {
        String devuelve = "";
        String pagina = "";
        URL url = new URL("http://books.google.com/books/feeds/volumes?q=\"" + URLEncoder.encode(palabras, "UTF-8") + "\"");
        SAXParserFactory fabrica = SAXParserFactory.newInstance();
        SAXParser parser = fabrica.newSAXParser();
        XMLReader lector = parser.getXMLReader();
        ManejadorXML manejadorXML = new ManejadorXML();
        lector.setContentHandler(manejadorXML);
        lector.parse(new InputSource(url.openStream()));
        return manejadorXML.getTotalResults();
        //////////////////////////////////////////////////////////////////////////////
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
                return  resultadoSW(strings[0]);
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
    public class ManejadorXML extends DefaultHandler
    {
        private  String totalResults;
        private  StringBuilder cadena=new StringBuilder();

        public String getTotalResults() {
            return totalResults;
        }
        @Override
        public void startElement(String uri, String nombreLocal, String nombreCualif, Attributes atributos) throws SAXException
        {
            cadena.setLength(0);
        }
        @Override
        public void characters(char ch[], int comienzo, int lon)
        {
            cadena.append(ch,comienzo,lon);
        }
        @Override
        public void endElement(String uri, String nombreLocal, String nombreCualif) throws SAXException
        {
            if(nombreLocal.equals("totalResults"))
            {
                totalResults=cadena.toString();
            }
        }
    }
}
