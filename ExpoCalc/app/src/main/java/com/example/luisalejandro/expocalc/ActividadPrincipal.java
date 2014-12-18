package com.example.luisalejandro.expocalc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import static java.lang.Math.exp;
import static java.lang.Math.pow;


public class ActividadPrincipal extends Activity {
    private EditText cuerpo, base, exponente;
    private TextView r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        this.cuerpo = (EditText) findViewById(R.id.text_K);
        this.base = (EditText) findViewById(R.id.text_base);
        this.exponente = (EditText) findViewById(R.id.text_exponent);
        this.r = (TextView) findViewById(R.id.result);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean potential(View view) {
        double k = Double.parseDouble(cuerpo.getText().toString());
        double b = Double.parseDouble(base.getText().toString());
        double expo = Double.parseDouble(exponente.getText().toString());
        double res;
        b = b%k;
        if ( b!=0 ) {
            int i = 1;
            while (Math.pow(b, i) % k != 1) {
                i++;
            }

            int modulo = (int) (expo % i);
            res = Math.pow(b, modulo) % k;

            r.setText(Double.toString(b) + "^" + Double.toString(expo) + " = " + Double.toString(res));
        }
        else
            r.setText(Double.toString(b).toString() + "^" + Double.toString(expo) + " = 0");

        return true;
    }
}
