package oscar.com.clienteecho;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private TextView tsalida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tsalida=(TextView)findViewById(R.id.textView1);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        ejecutaCliente();
    }
    private void log(String string)
    {
        tsalida.append(string+"\n");
    }
    private void ejecutaCliente()
    {
        String ip="192.168.42.124";
        int puerto=5000;
        log(" socket "+ip+" puerto "+puerto);
        try
        {
            Socket sk=new Socket(ip,puerto);
            BufferedReader entrada=new BufferedReader( new InputStreamReader(sk.getInputStream()));
            PrintWriter salida=new PrintWriter(new OutputStreamWriter(sk.getOutputStream()));
            log("enviando hola mundo");
            salida.println("Hola mundo");
            log("Recibiendo: "+ entrada.readLine());
            sk.close();
        }
        catch (Exception e)
        {
            log("Error: "+e.toString());
        }
    }
}
