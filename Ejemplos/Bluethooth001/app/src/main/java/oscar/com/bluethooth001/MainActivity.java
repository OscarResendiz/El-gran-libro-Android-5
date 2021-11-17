package oscar.com.bluethooth001;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BIT=0;
    private static final int REQUEST_DISCOVERABLE_BIT=0;
    TextView TMensaje;
    private BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TMensaje=(TextView)findViewById(R.id.TMensaje);
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter==null)
        {
            TMensaje.append("El dispositivo no soporta bluethooth");
        }
    }
    public void encenser(View view)
    {
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BIT);
        }
    }
    public void descubrible(View view)
    {
        if(!mBluetoothAdapter.isDiscovering())
        {
            Context context=getApplicationContext();
            CharSequence text="El dispositivo es visible";
            int duration= Toast.LENGTH_LONG;
            Toast toast=Toast.makeText(context,text,duration);
            toast.show();
            Intent enableIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(enableIntent,REQUEST_DISCOVERABLE_BIT);
        }
        else
        {
            Toast.makeText(this,"El dispositivo esta escaneando",Toast.LENGTH_LONG).show();
        }
    }
    public void Apagar(View view)
    {
        mBluetoothAdapter.disable();
        Context context=getApplicationContext();
        CharSequence text="apagando bluethooth";
        int duration=Toast.LENGTH_LONG;
        Toast toast=Toast.makeText(context,text,duration);
        toast.show();

    }
}
