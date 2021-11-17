package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PropiedadesGrupo extends AppCompatActivity {

    private TextView tNombre;
    private int idGrupo;
    protected DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades_grupo);
        //asigno los objetos
        tNombre=findViewById(R.id.tNombre);
        dataBase=new DataBase(this);
        //me traigo los extras
        idGrupo=getIntent().getExtras().getInt("ID");
        if(idGrupo!=-1)
        {
            //me traigo la informacion
            Grupo grupo=dataBase.getGrupo(idGrupo);
            tNombre.setText(grupo.getNombre());
        }
    }
    public void guardar(View view)
    {
        String nombre=tNombre.getText().toString();
        if(nombre.equals(""))
        {
            Toast.makeText(this,"Falta el nombre",Toast.LENGTH_LONG);
            return;
        }
        try {
            if (idGrupo == -1) {
                //hay que agregar nuevo
                dataBase.insertGrupo(nombre);
            } else {
                dataBase.updateGrupo(idGrupo, nombre);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }
        finish();
    }
}
