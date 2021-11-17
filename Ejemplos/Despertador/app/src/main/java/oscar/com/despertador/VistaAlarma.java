package oscar.com.despertador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class VistaAlarma extends AppCompatActivity {
    //controles
    TextView tNombre;
    CheckBox ChLunes;
    CheckBox ChMartes;
    CheckBox ChMiercoles;
    CheckBox ChJueves;
    CheckBox ChViernes;
    CheckBox ChSabado;
    CheckBox ChDomingo;
    CheckBox ChActivo;
    Button BLista;
    TextView LLista;
    private int idAlarma;
    private DataBase db;
    private int idLista;
    private TimePicker horaInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DataBase(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades_alarma);
        IncializaComponentes();
        Bundle extras = getIntent().getExtras();
        idAlarma = extras.getInt("idAlarma", -1);
        cargaInformacion();
    }

    private void IncializaComponentes() {
        tNombre = (TextView) findViewById(R.id.tNombre);
        horaInicio = (TimePicker) findViewById(R.id.horaInicio);
        ChLunes = (CheckBox) findViewById(R.id.ChLunes);
        ChMartes = (CheckBox) findViewById(R.id.ChMartes);
        ChMiercoles = (CheckBox) findViewById(R.id.ChMiercoles);
        ChJueves = (CheckBox) findViewById(R.id.ChJueves);
        ChViernes = (CheckBox) findViewById(R.id.ChViernes);
        ChSabado = (CheckBox) findViewById(R.id.ChSabado);
        ChDomingo = (CheckBox) findViewById(R.id.ChDomingo);
        ChActivo = (CheckBox) findViewById(R.id.ChActivo);
        BLista = (Button) findViewById(R.id.BLista);
        LLista = (TextView) findViewById(R.id.LLista);
    }

    private void cargaInformacion() {
        if (idAlarma != -1) {
            Alarma alarma = db.getAlarma(idAlarma);
            tNombre.setText(alarma.getNombre());
            horaInicio.setCurrentHour(alarma.getHora());
            horaInicio.setCurrentMinute(alarma.getMinuto());
            ChLunes.setChecked(alarma.isLunes());
            ChMartes.setChecked(alarma.isMartes());
            ChMiercoles.setChecked(alarma.isMiercoles());
            ChJueves.setChecked(alarma.isJueves());
            ChViernes.setChecked(alarma.isViernes());
            ChSabado.setChecked(alarma.isSabado());
            ChDomingo.setChecked(alarma.isDomingo());
            ChActivo.setChecked(alarma.isActiva());
            idLista=alarma.getIdLista();
            ListaReproduccion lista = db.getLista(idLista);
            if (lista != null) {
                LLista.setText(lista.getNombre());
            }
        } else {
            tNombre.setText("Nueva Alarma");
            horaInicio.setCurrentHour(6);
            horaInicio.setCurrentMinute(0);
            ChActivo.setChecked(true);
            LLista.setText("seleccione una lista de reproduccion");
            idLista=-1;
        }
    }
    private void MuestraMensaje(String mensaje)
    {
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();

    }
    public void Guardar(View view)
    {
        //hago validaciones
        if(tNombre.getText().toString().trim().equals(""))
        {
            MuestraMensaje("Falta el nombre");
            return;
        }
        int hora=horaInicio.getCurrentHour();
        int minutos=horaInicio.getCurrentMinute();
        if(minutos<0 || minutos>59)
        {
            MuestraMensaje("los minutos deben de estar entre 0 y 59");
            return;
        }
        if(ChLunes.isChecked()==false && ChMartes.isChecked()==false && ChMiercoles.isChecked()==false && ChJueves.isChecked()==false && ChViernes.isChecked()==false && ChSabado.isChecked()==false && ChDomingo.isChecked()==false)
        {
            MuestraMensaje("Seleccione los dias en que se activara la alarma");
            return;
        }
        boolean am=true;
        if(hora>12)
            am=false;
        if(idAlarma==-1)
        {
            //es una nueva alarma
            db.insertAlarma(tNombre.getText().toString(),hora,minutos,am,ChLunes.isChecked(), ChMartes.isChecked(),ChMiercoles.isChecked(), ChJueves.isChecked(), ChViernes.isChecked(), ChSabado.isChecked(), ChDomingo.isChecked(),ChActivo.isChecked());
        }
        else
        {
            db.updateAlarma(idAlarma,tNombre.getText().toString(),hora,minutos,am,ChLunes.isChecked(), ChMartes.isChecked(),ChMiercoles.isChecked(), ChJueves.isChecked(), ChViernes.isChecked(), ChSabado.isChecked(), ChDomingo.isChecked(),ChActivo.isChecked());
        }
        db.AsignaListaAlarma(idAlarma,idLista);
        Intent i=new Intent();
        setResult(RESULT_OK,i);
        finish();
    }
    public void SeleccionaLista(View view)
    {
        Intent i=new Intent(this,SeleccionarLista.class);
        startActivityForResult(i,147);

    }
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(resultado== RESULT_OK) {
            Bundle extras=            datos.getExtras();
            idLista=extras.getInt("idLista");
            ListaReproduccion lista=db.getLista(idLista);
            LLista.setText(lista.getNombre());
        }
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_alarma,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.guardar:
                Guardar(null);
                return true;
            case R.id.play:
                play();
                return true;
            case R.id.stop:
                stop();
                return true;
            case R.id.eliminar:
                eliminar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void play()
    {
        if(idAlarma!=-1) {
            HiloAlarma alarmas = HiloAlarma.getControlador();
            alarmas.Play(idAlarma);
        }
    }
    private void stop()
    {
        if(idAlarma!=-1) {
            HiloAlarma alarmas = HiloAlarma.getControlador();
            alarmas.Detener(idAlarma);
        }

    }
    private void eliminar()
    {
        stop();
        if(idAlarma!=-1)
        {
            db.deleteAlarma(idAlarma);
        }
        db.AsignaListaAlarma(idAlarma,idLista);
        Intent i=new Intent();
        setResult(RESULT_OK,i);
        finish();

    }
}
