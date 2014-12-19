/*
 * MainActivity.java
 * 
 * Created on December 18, 2014
 */

package com.example.multitouchclose;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Switch;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.PointF;

/** Clase para la actividad principal
 * 
 * @author Luis Alejandro Gonzalez Borras
 * @author Jose Manuel Gomez Gonzalez
 * @version 1.0
 * @see Visitar www.github.com/LuisGonzalez2014/Android_Projects/tree/master/MultitouchClose
 */
public class MainActivity extends Activity {
	/** Enumerado con los estados posibles en la detección del gesto */
	private enum GestureDetectionState {
		DETECTED, IDLE
	}
	private GestureDetectionState gds;                                  // Estado de ejecución
	private double PERCENTAGE;                                          // Porcentaje necesario para determinar posición de los dedos
	private PointF initial_point_1, initial_point_2, initial_point_3;   // Coordenadas iniciales de los 3 dedos
	private PointF actual_point_1, actual_point_2, actual_point_3;      // Coordenadas actuales de los 3 dedos
	private double initial_dist_1, initial_dist_2, initial_dist_3;      // Distancias iniciales entre los dedos
	private double actual_dist_1, actual_dist_2, actual_dist_3;         // Distancias actuales entre los dedos
	TextView texto;
	Switch sw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.texto = (TextView) findViewById(R.id.text);
		this.sw = (Switch) findViewById(R.id.s1);
		texto.setText("¡ BIENVENID@ !");

		// Inicialización de las variables
		PERCENTAGE = 0.6;                      // 60%
		initial_point_1 = new PointF();
		initial_point_2 = new PointF();
		initial_point_3 = new PointF();
		actual_point_1 = new PointF();
		actual_point_2 = new PointF();
		actual_point_3 = new PointF();
		initial_dist_1 = Integer.MAX_VALUE;
		initial_dist_2 = Integer.MAX_VALUE;
		initial_dist_3 = Integer.MAX_VALUE;
		actual_dist_1 = Integer.MIN_VALUE;
		actual_dist_2 = Integer.MIN_VALUE;
		actual_dist_3 = Integer.MIN_VALUE;
		this.gds = GestureDetectionState.IDLE;
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
	

	/**
     * @param ev objeto para reportar movimiento
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
		texto.setText("Esperando gesto...");
		
		// Almacenamos el número de dedos en pantalla para evitar llamadas innecesarias
		int num_pointers = ev.getPointerCount();
		
		// Si hay 3 dedos sobre la pantalla y ya se detectaron sus posiciones y distancias iniciales...
    	if (num_pointers == 3 && this.gds == GestureDetectionState.DETECTED)
    	{
    		// Almacenamos las coordenadas actuales de los 3 dedos
			actual_point_1 = new PointF(ev.getX(ev.getPointerId(0)), ev.getY(ev.getPointerId(0)));
			actual_point_2 = new PointF(ev.getX(ev.getPointerId(1)), ev.getY(ev.getPointerId(1)));
			actual_point_3 = new PointF(ev.getX(ev.getPointerId(2)), ev.getY(ev.getPointerId(2)));
        	
			// Calculamos las distancias actuales entre los 3 dedos
    		actual_dist_1 = this.distance(actual_point_1, actual_point_2);
    		actual_dist_2 = this.distance(actual_point_1, actual_point_3);
    		actual_dist_3 = this.distance(actual_point_2, actual_point_3);
    		
    		// Si las distancias son un 60% de lo que eran al principio...
    		if (initial_dist_1 * PERCENTAGE >= actual_dist_1 &&
    		    initial_dist_2 * PERCENTAGE >= actual_dist_2 &&
    		    initial_dist_3 * PERCENTAGE >= actual_dist_3)
    		{
    			if (sw.isChecked())
    			{
    				// Matamos el proceso de la aplicación
        			android.os.Process.killProcess(android.os.Process.myPid());
    			}
    			else
    			{
    				texto.setText("Aplicación finalizada.");
    			}
    		}
    		// Si las coordenadas Y de los 3 dedos aumentan un 60%...
    		else if (initial_point_1.y <= actual_point_1.y * PERCENTAGE &&
    				 initial_point_2.y <= actual_point_2.y * PERCENTAGE &&
    				 initial_point_3.y <= actual_point_3.y * PERCENTAGE)
    		{
    			if (sw.isChecked())
    			{
    				// Volvemos al 'HOME' dejando la app en segundo plano
    				super.finish();
    			}
    			else
    			{
    				texto.setText("Aplicación en segundo plano.");
    			}
    		}
    	}
    	// Si hay 3 dedos sobre la pantalla y no se detectaron sus posiciones y distancias iniciales...
    	else if (num_pointers == 3 && this.gds == GestureDetectionState.IDLE)
    	{
    		// Cambiamos a estado DETECTADOS
    		this.gds = GestureDetectionState.DETECTED;
    		// Almacenamos las coordenadas iniciales de los 3 dedos
    		initial_point_1 = new PointF(ev.getX(ev.getPointerId(0)), ev.getY(ev.getPointerId(0)));
    		initial_point_2 = new PointF(ev.getX(ev.getPointerId(1)), ev.getY(ev.getPointerId(1)));
    		initial_point_3 = new PointF(ev.getX(ev.getPointerId(2)), ev.getY(ev.getPointerId(2)));
        	
    		// Calculamos las distancias iniciales entre los 3 dedos
        	initial_dist_1 = this.distance(initial_point_1, initial_point_2);
        	initial_dist_2 = this.distance(initial_point_1, initial_point_3);
        	initial_dist_3 = this.distance(initial_point_2, initial_point_3);
    	}
    	// Si no hay dedos sobre la pantalla...
    	else if (ev.getAction() == MotionEvent.ACTION_UP)
    	{
    		// Volvemos al estado inicial
    		this.gds = GestureDetectionState.IDLE;
    	}
        
        return true;
    }
    
    // Método para calcular la distancia entre dos puntos PointF.
	/**
     * @param p1 punto con las coordenadas x e y
     * @param p2 punto con las coordenadas x e y
     * 
     * @return distancia entre p1 y p2
     */
    private double distance(PointF p1, PointF p2) {
    	return Math.sqrt(Math.pow(p2.x-p1.x, 2) + Math.pow(p2.y-p1.y, 2));
    }
}
