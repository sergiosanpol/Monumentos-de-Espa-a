package hlc.com.monumentosdeespaa.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import hlc.com.monumentosdeespaa.R;

public class MonumentosCercanos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monumentos_cercanos);

        if(getIntent().hasExtra("monumentos")){
            Toast.makeText(this, "Los tengo", Toast.LENGTH_LONG).show();
        }
    }
}
