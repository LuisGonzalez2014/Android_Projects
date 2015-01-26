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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Activity_main extends Activity implements SensorEventListener{
	private TextView mensaje;
	private EditText pCardinal;
	private boolean boton;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private String cardinal;
	
    // Sensors and location
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz);
        
        mensaje = (TextView) findViewById(R.id.textViewMensaje);
        pCardinal = (EditText) findViewById(R.id.editTextCardinal);
        
        boton = false;
        cardinal = new String();
        
        // Initialize sensors
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


	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];
	    float error = 5;
        
	    if (boton)
	    {
	        if ((azimuth_angle >= 180-error && azimuth_angle <= 180+error) && cardinal.equals("S"))
	        {
	            this.dispatchTakePictureIntent();
	        } 
		    else if ((azimuth_angle >= 90-error && azimuth_angle <= 90+error) && cardinal.equals("E"))
	        {
	            this.dispatchTakePictureIntent();
	        } 
		    else if ((azimuth_angle >= 270-error && azimuth_angle <= 270+error) && cardinal.equals("O"))
	        {
	            this.dispatchTakePictureIntent();
	        } 
		    else if ((azimuth_angle >= 45-error && azimuth_angle <= 45+error) && cardinal.equals("NE"))
		    {
	            this.dispatchTakePictureIntent();
	        } 
		    else if ((azimuth_angle >= 135-error && azimuth_angle <= 135+error) && cardinal.equals("SE"))
	        {
	            this.dispatchTakePictureIntent();
	        } 
		    else if ((azimuth_angle >= 315-error && azimuth_angle <= 315+error) && cardinal.equals("NO"))
	        {
	            this.dispatchTakePictureIntent();
	        } 
		    else if ((azimuth_angle >= 225-error && azimuth_angle <= 225+error) && cardinal.equals("SO"))
	        {
	            this.dispatchTakePictureIntent();
	        }
		    else if ((azimuth_angle >= 360-error || azimuth_angle <= error) && cardinal.equals("N"))
		    {
	            this.dispatchTakePictureIntent();
	        }
	    }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean aceptButton(View view){
		boton = true;
		cardinal = pCardinal.getText().toString().toUpperCase();

        return true;
	}
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
}
