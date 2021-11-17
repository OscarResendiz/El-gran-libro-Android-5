package oscar.com.bluethooth2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    TextView textView1;
    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1=(TextView)findViewById(R.id.textView1);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        textView1.append("\nadaptador: "+bluetoothAdapter);
        checaBluethooth();
    }

    private void checaBluethooth() {
        if(bluetoothAdapter==null)
        {
            textView1.append("\n el dispositivo no soporta bluethooth");
            return;
        }
        else
        {
            if(bluetoothAdapter.isEnabled())
            {
                textView1.append("\n el bluethooth esta habilitado");
                textView1.append("\n los dispositivos enparejados son:");
                Set<BluetoothDevice> dispositivos=bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice dispositivo: dispositivos)
                {
                    textView1.append("\ndispositivo: "+dispositivo.getName()+" "+dispositivo);
                }
            }
            else
            {
                Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent,REQUEST_ENABLE_BT);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BT)
        {
            checaBluethooth();
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
