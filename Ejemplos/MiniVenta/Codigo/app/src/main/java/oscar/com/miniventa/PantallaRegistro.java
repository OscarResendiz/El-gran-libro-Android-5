package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaRegistro extends AppCompatActivity {

    private TextView TUsuario;
    private TextView TNombre;
    private TextView TPassword;
    private TextView TConfirmar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_registro);
        TUsuario=(TextView)findViewById(R.id.TUsuario);
         TNombre=(TextView)findViewById(R.id.TNombre);
        TPassword=(TextView)findViewById(R.id.TPassword);
        TConfirmar=(TextView)findViewById(R.id.TConfirmar);
    }
    private void muestraMensaje(String mensaje)
    {
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
    }
    public void guardar(View view)
    {
        DataBase dataBase=new DataBase(this);
        String nombre=TNombre.getText().toString();
        String usuario=TUsuario.getText().toString();
        String password=TPassword.getText().toString();
        String password2=TConfirmar.getText().toString();
        if(nombre.trim().equals(""))
        {
            muestraMensaje("Falta el nombre del negocio");
            return;
        }
        if(usuario.trim().equals(""))
        {
            muestraMensaje("Falta el usuario");
            return;
        }
        if(password.trim().equals(""))
        {
            muestraMensaje("Nececita escribir una contraseña");
            return;
        }
        if(!password.trim().equals(password2))
        {
            muestraMensaje("Las contraseñas no coinciden");
            return;
        }
        dataBase.setDatosEmpresa(nombre, usuario, password);
        Intent i=new Intent();
        setResult(RESULT_OK,i);
        finish();

    }
}
