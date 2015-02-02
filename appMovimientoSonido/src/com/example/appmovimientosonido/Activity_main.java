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

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
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
 * @version 1.0
 * @see Visitar www.github.com/LuisGonzalez2014/Android_Projects/tree/master/appMovimientoSonido
 */
public class Activity_main extends Activity implements SensorEventListener {
	public enum Estado { ON_P1, ON_P2, ON_P3, ON_P4 };
	
	Button start;
    private SensorManager mSensor;
    private Vibrator vibracion;
    private TextView text;
    private MediaPlayer mp;
	private Estado state;
	private Integer tope;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz);
        
        // Inicialización de variables
        tope = 5;
        start = (Button) findViewById(R.id.reset);
        text = (TextView) findViewById(R.id.texto);
        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor.registerListener(this,mSensor.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_UI);
        vibracion = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        state = Estado.ON_P1;                   // ESTADO
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // El dispositivo funcionará con la pantalla en vertical
        // SONIDO
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mp = MediaPlayer.create(this, R.raw.laser);
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
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
		{
			float eje_X = event.values[0];
			float eje_Y = event.values[1];
			float eje_Z = event.values[2];
            
	        //text.setText("ESTADO: " + state.toString());
			
			if (state.equals(Estado.ON_P1) && eje_X >= tope && Math.abs(eje_Z) <= 2)
			{
				vibracion.vibrate(100);
				state = Estado.ON_P2;
			}
			else if (state.equals(Estado.ON_P2) && eje_X <= -tope && eje_Z <= -(tope/2))
			{
				vibracion.vibrate(100);
				state = Estado.ON_P3;
			}
			else if (state.equals(Estado.ON_P3) && eje_X >= tope && Math.abs(eje_Z) <= 2)
			{
				vibracion.vibrate(200);
				state = Estado.ON_P4;
				mp.start();
			}
		}
	}

	/**
     * Función que se ejecuta al pulsar el botón de reseteo
     */
	public void resetear(View v){
		state = Estado.ON_P1;
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
        mSensor.registerListener(this,mSensor.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_UI);
    }
}
