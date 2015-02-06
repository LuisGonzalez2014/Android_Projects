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
package com.example.appmovimientosonido;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** Clase para la actividad principal que implementa el control de eventos
 * de los sensores
 * 
 * @author Luis Alejandro Gonzalez Borras
 * @author Jose Manuel Gomez Gonzalez
 * @version 2.0
 * @see Visitar www.github.com/LuisGonzalez2014/Android_Projects/tree/master/appMovimientoSonido
 */
public class Activity_main extends Activity implements SensorEventListener {
	public enum Estado { QUIET, ON_P1, ON_P2, ON_P3, ON_P4 };
	
    private SensorManager mSensor;
    private Vibrator vibracion;
    private TextView text;
    private MediaPlayer mp;
	private Estado state;
	private Integer acceleration;
	private float azimuth, pitch, roll;
	private float X_accel, Y_accel, Z_accel;
    private Button bAbout;
    
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz);
        
        bAbout= (Button) findViewById(R.id.buttonInstructions);
        bAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchInstructions();
				}
		});
        
        // Inicialización de variables
        acceleration = 8;
        text = (TextView) findViewById(R.id.texto);
        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor.registerListener(this,mSensor.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_UI);
        mSensor.registerListener(this,mSensor.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_UI);
        vibracion = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        state = Estado.QUIET;   // ESTADO
        // El dispositivo funcionará con la pantalla en vertical
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // Sonido
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mp = MediaPlayer.create(this, R.raw.laser);
        
        // Acelerómetro
        X_accel = 0;
		Y_accel = 0;
		Z_accel = 0;
        
        // Orientación
        azimuth = 0;
        pitch = 0;
        roll = 0;
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
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
		{
			X_accel = event.values[0];
			Y_accel = event.values[1];
			Z_accel = event.values[2];
		}
		else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
		{
	        azimuth = event.values[0];
	        pitch = event.values[1];
	        roll = event.values[2];
		}

        this.detectMovement();
	}

	/**
     * Función que se ejecutará para detectar la realización del movimiento
     */
	public void detectMovement(){
		//text.setText("ESTADO: " + state.toString());
		
		// Dispositivo con la pantalla apuntando hacia abajo
		if (state.equals(Estado.QUIET) && Math.abs(pitch)>=160 && Math.abs(roll)<=15)
		{
			vibracion.vibrate(100);
			state = Estado.ON_P1;
		}
		// Se ha efectuado una aceleración sobre el dispositivo con la pantalla hacia abajo
		else if (state.equals(Estado.ON_P1) && Math.abs(X_accel) >= acceleration
			&& Math.abs(pitch)>=160 && Math.abs(roll)<=15)
        {
           vibracion.vibrate(100);
           state = Estado.ON_P2;
        }
		// Se ha efectuado una aceleración sobre el dispositivo con la pantalla de costado
        else if (state.equals(Estado.ON_P2) && Math.abs(X_accel)+Math.abs(Z_accel) >= acceleration
        	&& Math.abs(pitch)<=15 && roll>30 && roll<60)
        {
           vibracion.vibrate(100);
           state = Estado.ON_P3;
        }
		// Se ha efectuado una aceleración sobre el dispositivo con la pantalla hacia abajo
        else if (state.equals(Estado.ON_P3) && Math.abs(X_accel) >= acceleration
    		&& Math.abs(pitch)>=160 && Math.abs(roll)<=15)
        {
           vibracion.vibrate(200);
           state = Estado.ON_P4;
           mp.start();
        }
	}
	
	/**
     * Función que se ejecuta al pulsar el botón de reseteo
     */
	public void resetear(View v){
		state = Estado.QUIET;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
 
    @Override
    protected void onPause() {
        super.onPause();
        mSensor.unregisterListener(this);
    }
 
    @SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
        super.onResume();
        mSensor.registerListener(this,mSensor.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_UI);
        mSensor.registerListener(this,mSensor.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_UI);
    }
    
    public void launchInstructions(){
    	Intent i= new Intent(this, Instructions.class);
    	startActivity(i);
    }
}
