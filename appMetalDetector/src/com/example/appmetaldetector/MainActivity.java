/*
 *  Copyright (C) 2014, 2015 - Luis Alejandro González Borrás, Jose Manuel Gómez González>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.example.appmetaldetector;

import com.example.appsorpresa.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/** Clase para la actividad principal que implementa el control de eventos
 * de los sensores
 * 
 * @author Luis Alejandro Gonzalez Borras
 * @author Jose Manuel Gomez Gonzalez
 * @version 1.0
 * @see Visitar www.github.com/LuisGonzalez2014/Android_Projects/tree/master/appMetalDetector
 */
public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager mSensor;
	private TextView texto;
	private int sensor;
	private GeomagneticField campo;
	private LocationManager lm; 
	private Location location;
    private ImageView image;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageViewMetal);
        
        // El dispositivo funcionará con la pantalla en vertical
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
        sensor = Sensor.TYPE_MAGNETIC_FIELD;
        campo = new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(),
        		(float) location.getAltitude(), location.getTime());
        
        texto = (TextView) findViewById(R.id.text);
        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor.registerListener(this,mSensor.getDefaultSensor(sensor),SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == sensor)
		{
			float v0 = event.values[0];
			float v1 = event.values[1];
			float v2 = event.values[2];
			
			float v0_campo = campo.getX();
			float v1_campo = campo.getY();
			float v2_campo = campo.getX();
			
			double mag = Math.sqrt(Math.pow(v0, 2)+ Math.pow(v1, 2) + Math.pow(v2, 2));
			double mag_campo = Math.sqrt(Math.pow(v0_campo, 2)+ Math.pow(v1_campo, 2) + Math.pow(v2_campo, 2));
			
			if ((mag*1000 < mag_campo*0.5) || (mag*1000 > mag_campo*1.5))
			{
				texto.setTextColor(Color.rgb(55, 71, 79));
				texto.setTextSize(18);
				texto.setText("Inducción magnética de " + String.format("%.2f", mag) + " µT");
				image.setImageResource(R.drawable.mdy);
			}
			else
			{
				texto.setTextSize(12);
				texto.setText("Buscando metal...");
				image.setImageResource(R.drawable.mdn);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
 
    @Override
    protected void onPause() {
        super.onPause();
        mSensor.unregisterListener(this);
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        mSensor.registerListener(this,mSensor.getDefaultSensor(sensor),SensorManager.SENSOR_DELAY_NORMAL);
    }
}
