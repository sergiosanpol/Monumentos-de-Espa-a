package hlc.com.monumentosdeespaa.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hlc.com.monumentosdeespaa.R;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        //flecha de volver atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
