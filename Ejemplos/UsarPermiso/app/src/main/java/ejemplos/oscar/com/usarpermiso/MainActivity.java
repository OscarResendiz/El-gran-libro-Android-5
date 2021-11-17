package ejemplos.oscar.com.usarpermiso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void verVideo(View view)
    {
        Intent i=new Intent();
        i.setClassName("servicios.payperview.com.nuevopermiso","servicios.payperview.com.nuevopermiso.VerVideo");
        startActivity(i);
    }
}
