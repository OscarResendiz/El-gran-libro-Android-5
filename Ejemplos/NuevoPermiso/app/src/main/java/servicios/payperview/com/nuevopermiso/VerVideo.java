package servicios.payperview.com.nuevopermiso;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class VerVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_video);
        Toast.makeText(this,"Reproduciendo video",Toast.LENGTH_SHORT).show();
    }
}
