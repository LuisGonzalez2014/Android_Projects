package com.example.appgpsvoz;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.speech.RecognizerIntent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Activity_main extends Activity {
	private final static String DEFAULT_LANG_MODEL = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM; 
	private String languageModel; 
	private static final String LOGTAG = "ASRBEGIN";
	private static int ASR_CODE;
	LocationManager lm; 
	Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz);
        
        languageModel = DEFAULT_LANG_MODEL;
        ASR_CODE = 123;
		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		setSpeakButton();
    }
    
    /**
	 * Initializes the speech recognizer and starts listening to the user input
	 */
	private void listen()  {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		// Specify language model
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel); 

		// Start listening
		startActivityForResult(intent, ASR_CODE);
    }
	
	/**
	 * Sets up the listener for the button that the user
	 * must click to start talking
	 */
	@SuppressLint("DefaultLocale")
	private void setSpeakButton() {
		//Gain reference to speak button
		Button speak = (Button) findViewById(R.id.iniciar);

		//Set up click listener
		speak.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Speech recognition does not currently work on simulated devices,
					//it the user is attempting to run the app in a simulated device
					//they will get a Toast
					if("generic".equals(Build.BRAND.toLowerCase())){
						Toast toast = Toast.makeText(getApplicationContext(),"ASR is not supported on virtual devices", Toast.LENGTH_SHORT);
						toast.show();
						Log.d(LOGTAG, "ASR attempt on virtual device");						
					}
					else{
						listen(); 				//Set up the recognizer with the parameters and start listening
					}
				}
			});
	}
	
	/**
	 *  Shows the formatted best of N best recognition results (N-best list) from
	 *  best to worst in the <code>ListView</code>. 
	 *  For each match, it will render the recognized phrase and the confidence with 
	 *  which it was recognized.
	 */
	@SuppressLint("InlinedApi")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ASR_CODE)  {
            if (resultCode == RESULT_OK)  {            	
            	if(data!=null) {
            		ArrayList<String> coincidenceList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					final String cadena = coincidenceList.get(0);
					location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					
					if (location != null)
					{
						final double latitud_origen = location.getLatitude();
						final double longitud_origen = location.getLongitude();
						
						Pattern p = Pattern.compile("latitud ((?:menos )?\\d+)(?: con | coma | punto )?(\\d+)? longitud ((?:menos )?\\d+)(?: con | coma | punto )?(\\d+)?");
				        Matcher m = p.matcher(cadena.toLowerCase());
				        double latitud_destino = 0, longitud_destino = 0;

				        if (m.find()) {
				            if (m.group(1).startsWith("menos ")) {
				                latitud_destino = - Double.parseDouble(m.group(1).substring(6));
				                if (m.group(2) != null) {
				                    latitud_destino -= Double.parseDouble("0."+m.group(2));
				                }
				            } else {
				                latitud_destino = Double.parseDouble(m.group(1));
				                if (m.group(2) != null) {
				                    latitud_destino += Double.parseDouble("0."+m.group(2));
				                }
				            }

				            if (m.group(3).startsWith("menos ")) {
				                longitud_destino = - Double.parseDouble(m.group(3).substring(6));
				                if (m.group(4) != null) {
				                    longitud_destino -= Double.parseDouble("0."+m.group(4));
				                }
				            } else {
				                longitud_destino = Double.parseDouble(m.group(3));
				                if (m.group(4) != null) {
				                    longitud_destino += Double.parseDouble("0."+m.group(4));
				                }
				            }

				            String url = "http://maps.google.com/maps?saddr="+latitud_origen+","+longitud_origen+ "&daddr="+latitud_destino+","+longitud_destino;
				            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
				            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				            startActivity(intent);
				        }
                	}
            	}
            }
            else {       	
	    		//Reports error in recognition error in log
	    		Log.e(LOGTAG, "Recognition was not successful");
            }
        }
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interfaz, menu);
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
}
