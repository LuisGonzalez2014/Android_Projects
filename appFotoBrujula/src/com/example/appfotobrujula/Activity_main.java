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


public class Activity_main extends Activity implements SensorEventListener{
	private RadioButton button_norte, button_sur, button_este, button_oeste;
	private RadioButton button_noreste, button_sureste, button_noroeste, button_suroeste;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
    // Sensors and location
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
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
}
