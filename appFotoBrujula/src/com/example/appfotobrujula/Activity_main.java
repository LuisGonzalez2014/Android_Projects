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
package com.example.appfotobrujula;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

/** Clase para la actividad principal que implementa el control de eventos
 * de los sensores
 * 
 * @author Luis Alejandro Gonzalez Borras
 * @author Jose Manuel Gomez Gonzalez
 * @version 1.0
 * @see Visitar www.github.com/LuisGonzalez2014/Android_Projects/tree/master/appFotoBrujula
 */
public class Activity_main extends Activity implements SensorEventListener{
	private RadioButton button_norte, button_sur, button_este, button_oeste;
	private RadioButton button_noreste, button_sureste, button_noroeste, button_suroeste;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
    // Sensores y Orientación
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz);
        
        button_norte = (RadioButton) findViewById(R.id.radioNorte);
        button_sur = (RadioButton) findViewById(R.id.radioSur);
        button_este = (RadioButton) findViewById(R.id.radioEste);
        button_oeste = (RadioButton) findViewById(R.id.radioOeste);
        button_noreste = (RadioButton) findViewById(R.id.radioNoreste);
        button_sureste = (RadioButton) findViewById(R.id.radioSureste);
        button_noroeste = (RadioButton) findViewById(R.id.radioNoroeste);
        button_suroeste = (RadioButton) findViewById(R.id.radioSuroeste);
        
        // Inicialización de los sensores
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }
    
    /**
     * Method called when the app resumes his activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Continue listening the orientation sensor
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Method called when the app pauses his activity
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Stop listening the sensor
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
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

    /**
     * @param event objeto para reportar cambio en el sensor
     */
	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];        // Devuelve los grados de rotación del telefono (0=Norte)
	    float error = 5;                              // Grados de error permitidos
        
	    if ((azimuth_angle >= 180-error && azimuth_angle <= 180+error) && button_sur.isChecked())
        {
            this.dispatchTakePictureIntent();
        } 
	    else if ((azimuth_angle >= 90-error && azimuth_angle <= 90+error) && button_este.isChecked())
        {
            this.dispatchTakePictureIntent();
        } 
	    else if ((azimuth_angle >= 270-error && azimuth_angle <= 270+error) && button_oeste.isChecked())
        {
            this.dispatchTakePictureIntent();
        } 
	    else if ((azimuth_angle >= 45-error && azimuth_angle <= 45+error) && button_noreste.isChecked())
	    {
            this.dispatchTakePictureIntent();
        } 
	    else if ((azimuth_angle >= 135-error && azimuth_angle <= 135+error) && button_sureste.isChecked())
        {
            this.dispatchTakePictureIntent();
        } 
	    else if ((azimuth_angle >= 315-error && azimuth_angle <= 315+error) && button_noroeste.isChecked())
        {
            this.dispatchTakePictureIntent();
        } 
	    else if ((azimuth_angle >= 225-error && azimuth_angle <= 225+error) && button_suroeste.isChecked())
        {
            this.dispatchTakePictureIntent();
        }
	    else if ((azimuth_angle >= 360-error || azimuth_angle <= error) && button_norte.isChecked())
	    {
            this.dispatchTakePictureIntent();
        }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	/** 
	 * Método para lanzar la aplicación de cámara del dispositivo
	 */
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
}
