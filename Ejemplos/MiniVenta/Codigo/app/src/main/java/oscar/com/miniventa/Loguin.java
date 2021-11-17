package oscar.com.miniventa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Loguin extends AppCompatActivity {
    private DataBase DB;
    private TextView TUsuario;
    private TextView TPassword;
    private Empresa empresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loguin);
        //me traigo los datos del negocio
        DB=new DataBase(this);
        empresa=DB.getDatosEmpresa();
        if(empresa==null)
        {
            //es primera vez que se inicia la aplicacion, por lo que hay que pedir que se registre
            Intent i=new Intent(this,PantallaRegistro.class);
            startActivityForResult(i,1);
            return;
        }
        //me traigo los componentes
        TUsuario=(TextView)findViewById(R.id.TUsuario);
        TPassword=(TextView)findViewById(R.id.TPassword);
        TUsuario.setText(empresa.getUsuario());
    }
    protected void onActivityResult(int codigo,int resultado, Intent datos) {
        if (resultado == RESULT_OK) {
            Intent i=new Intent(this,Principal.class);
            startActivity(i);
        }
        finish();
    }
    public void ingresar(View view)
    {
        empresa=DB.getDatosEmpresa();
        String password=empresa.getPassword();
        if(!password.equals( TPassword.getText().toString()))
        {
            Toast.makeText(this,"Clave incorrecta",Toast.LENGTH_LONG).show();
            return;
        }
        Intent i=new Intent(this,Principal.class);
        startActivity(i);
        finish();
    }
}
