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
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.appgpsvoz.InvalidSpeechException;

/** Clase para la actividad principal
 * 
 * @author Luis Alejandro Gonzalez Borras
 * @author Jose Manuel Gomez Gonzalez
 * @version 1.0
 * @see Visitar www.github.com/LuisGonzalez2014/Android_Projects/tree/master/appGPSVoz
 */
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
            		// Obtenemos las cadenas reconocidas
            		ArrayList<String> coincidenceList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					// Almacenamos la primera cadena de las reconocidas
            		final String cadena = coincidenceList.get(0);
            		
            		//final String cadena = "latitud 37 grados 11 minutos 48 con 4 segundos norte longitud 3 grados 37 minutos 28 con 8 segundos este";
            		
            		// Obtenemos la localización del dispositivo por medio de la Internet
					location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					
					if (location != null)
					{
						final double latitud_origen = location.getLatitude();
						final double longitud_origen = location.getLongitude();
						
						double latitud_destino = 0, longitud_destino = 0;

						// Expresiones regulares para la extracción de los datos
				        Pattern top = Pattern.compile("latitud (.+) longitud (.+)");
				        Pattern def1 = Pattern.compile("^((?:menos )?\\d+)(?: con | coma | punto )?(\\d+)?$");
				        Pattern def2 = Pattern.compile("^((?:menos )?\\d+) grados? ((?:menos )?\\d+) minutos? ((?:menos )?\\d+)(?: con | coma | punto )?(\\d+)? segundos? (norte|sur|este|oeste)$");
				        Matcher mTop = top.matcher(cadena.toLowerCase());
				        
				        /*
	            		Toast toast2 = Toast.makeText(getApplicationContext(),"Cadena: " + cadena, Toast.LENGTH_LONG);
						toast2.show();
						*/
				        try{
					        if (mTop.find()) {
					            Matcher mDef1 = def1.matcher(mTop.group(1));
					            Matcher mDef2 = def2.matcher(mTop.group(1));
	
					            latitud_destino = getCoordinateValue(mDef1, mDef2);
					            mDef1 = def1.matcher(mTop.group(2));
					            mDef2 = def2.matcher(mTop.group(2));
	
					            longitud_destino = getCoordinateValue(mDef1, mDef2);
					            System.out.println("Latitud: "+latitud_destino+" Longitud: "+longitud_destino);
					        } else {
					            throw new InvalidSpeechException("ERROR: No se ha detectado correctamente su voz");
					        }
				        }catch(InvalidSpeechException e){}

			            String url = "http://maps.google.com/maps?saddr="+latitud_origen+","+longitud_origen+ "&daddr="+latitud_destino+","+longitud_destino;
			            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
			            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
			            startActivity(intent);
                	}
					else
					{
						Toast toast = Toast.makeText(getApplicationContext(),"Not GPS signal", Toast.LENGTH_SHORT);
						toast.show();
					}
            	}
            }
            else
            {       	
	    		//Reports error in recognition error in log
	    		Log.e(LOGTAG, "Recognition was not successful");
            }
        }
	}
	
	/**
	 * 
	 * @param m1 coordenadas latitud
	 * @param m2 coordenadas longitud
	 * @return la conversión de grados, minutos y segundos a decimal
	 * @throws InvalidSpeechException
	 */
	private static double getCoordinateValue(Matcher m1, Matcher m2) throws InvalidSpeechException {
        double valor = 0;

        if (m2.find()) {
            if (m2.group(1).startsWith("menos ")) { // grados
                valor = - Double.parseDouble(m2.group(1).substring(6));
            } else {
                valor = Double.parseDouble(m2.group(1));
            }

            if (m2.group(2).startsWith("menos ")) { // minutos
                valor += - Double.parseDouble(m2.group(2).substring(6))/60.0;
            } else {
                valor += Double.parseDouble(m2.group(2))/60.0;
            }

            if (m2.group(3).startsWith("menos ")) { // segundos
                valor += - Double.parseDouble(m2.group(3).substring(6))/3600.0;
                if (m2.group(4) != null) {
                    valor -= Double.parseDouble("0."+m2.group(4))/3600.0;
                }
            } else {
                valor += Double.parseDouble(m2.group(3))/3600.0;
                if (m2.group(4) != null) {
                    valor += Double.parseDouble("0."+m2.group(4))/3600.0;
                }
            }
            
            if (m2.group(5).equals("sur") || m2.group(5).equals("este")) {
            	valor = -valor;
            }
        } else if (m1.find()) {
            if (m1.group(1).startsWith("menos ")) {
                valor = - Double.parseDouble(m1.group(1).substring(6));
                if (m1.group(2) != null) {
                    valor -= Double.parseDouble("0."+m1.group(2));
                }
            } else {
                valor = Double.parseDouble(m1.group(1));
                if (m1.group(2) != null) {
                    valor += Double.parseDouble("0."+m1.group(2));
                }
            }
        } else {
        	throw new InvalidSpeechException("");
        }

        return valor;
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
