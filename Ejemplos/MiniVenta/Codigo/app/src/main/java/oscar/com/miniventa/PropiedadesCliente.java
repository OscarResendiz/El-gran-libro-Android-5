package oscar.com.miniventa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class PropiedadesCliente extends AppCompatActivity {
    private TextView TNombre;
    private TextView TApellido;
    private RadioButton RBHombre;
    private RadioButton RBMujer;
    private TextView TTelefono;
    private TextView TEmail;
    private TextView TDireccion;
    private CalendarView ClFechaNacimiento;
    private int idCliente;
    protected DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades_cliente);
        dataBase=new DataBase(this);
        //instancio todos los objetos de la pantalla
         TNombre=(TextView)findViewById(R.id.TNombre);
         TApellido=(TextView)findViewById(R.id.TApellido);
         RBHombre=(RadioButton)findViewById(R.id.RBHombre);
         RBMujer=(RadioButton)findViewById(R.id.RBMujer);
         TTelefono=(TextView)findViewById(R.id.TTelefono);
         TEmail=(TextView)findViewById(R.id.TEmail);
         TDireccion=(TextView)findViewById(R.id.TDireccion);
         ClFechaNacimiento=(CalendarView)findViewById(R.id.ClFechaNacimiento);
        //me traigo los extras
        idCliente = getIntent().getExtras().getInt("ID");
        if (idCliente != -1) {
            //me traigo la informacion
            Cliente cliente = dataBase.getCliente(idCliente);
            TNombre.setText(cliente.getNombre());
            TApellido.setText(cliente.getApellido());
            TTelefono.setText(cliente.getTelefono());
            TEmail.setText(cliente.getEmail());
            TDireccion.setText(cliente.getDireccion());
            RBHombre.setChecked(cliente.esHombre());
            RBMujer.setChecked(cliente.esMujer());
            Date date=cliente.getFechaNaciiento();
            ClFechaNacimiento.setDate(date.getTime());
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_articulo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_guardar:
                guardar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void muestraMensaje(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_LONG);
    }
    private void guardar() {
        //me traigo los valores
        Cliente cliente=new Cliente();
        cliente.setNombre(TNombre.getText().toString());
        cliente.setApellido( TApellido.getText().toString());
        cliente.setTelefono( TTelefono.getText().toString());
        cliente.setEmail(TEmail.getText().toString());
        cliente.setDireccion(TDireccion.getText().toString());
        cliente.setSexoHombre(RBHombre.isChecked());
        Date d=new Date(ClFechaNacimiento.getDate());
        cliente.setFechaNaciiento(d);
        //ahora hago mis validaciones
        if(cliente.getNombre().trim().equals(""))
        {
            muestraMensaje("Falta el nomnre");
            return;
        }
        try
        {
            if (idCliente== -1) {
                //es nuevo
                dataBase.insertCliente(cliente.getNombre(),cliente.getApellido(),cliente.getSexo(),cliente.getDireccion(),cliente.getTelefono(),cliente.getEmail(),cliente.getFechaNaciiento());
            } else {
                //ya existe
                dataBase.updateCliente(idCliente,cliente.getNombre(),cliente.getApellido(),cliente.getSexo(),cliente.getDireccion(),cliente.getTelefono(),cliente.getEmail(),cliente.getFechaNaciiento());
            }
        }
        catch (Exception ex)
        {
            muestraMensaje(ex.getMessage());
        }
        finish();
    }
}
