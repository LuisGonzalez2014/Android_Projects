package com.example.appmovimientosonido;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
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
	public enum Estado {
        ON_P1, ON_P2, ON_P3, ON_P4
     };
	
	Button start;
    private SensorManager mSensor;
    private TextView text;
    private MediaPlayer mp;
	private Estado state;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz);
        
        start = (Button) findViewById(R.id.iniciar);
        text = (TextView) findViewById(R.id.texto);
        
        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor.registerListener(this,mSensor.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_UI);
        
        // ESTADO
        state = Estado.ON_P1;
        
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
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			float values[] = event.values;
			 
	        Integer eje_x = (int) values[0];
	        Integer eje_y = (int) values[1];
	        Integer eje_z = (int) values[2];
	        text.setText("ESTADO: " + state.toString());
			
			Integer tope = 5;
			
			if (state == Estado.ON_P1 && eje_x >= tope)
			{
				state = Estado.ON_P2;
			}
			else if (state == Estado.ON_P2 && eje_x <= -tope && eje_z <= -(tope/2))
			{
				state = Estado.ON_P3;
			}
			else if (state == Estado.ON_P3 && eje_x >= tope*2)
			{
				state = Estado.ON_P4;
				mp.start();
			}
        }
	}

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
