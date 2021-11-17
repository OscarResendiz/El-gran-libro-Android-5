package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MessageBox extends AppCompatActivity {

    private TextView tMensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box);
        tMensaje=(TextView) findViewById(R.id.TMensaje);
        //me traigo el mensaje
        String mensaje=getIntent().getExtras().getString("MENSAJE");
        tMensaje.setText(mensaje);
    }
    public void aceptar(View view)
    {
        Intent i=new Intent();
        setResult(RESULT_OK,i);
        finish();

    }
    public void cancelar(View view)
    {
        Intent i=new Intent();
        setResult(RESULT_CANCELED,i);
        finish();
    }
}
