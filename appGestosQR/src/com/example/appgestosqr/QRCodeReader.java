package com.example.appgestosqr;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRCodeReader extends ARViewActivity {
    /**
     * Text view that will display bar code data
     */
    private TextView mText;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        mText = new TextView(this);
        mGUIView = mText;
    }

    @Override
    protected int getGUILayout() 
    {
        return 0;
    }

    /**
     * Display a text on screen
     * @param data String to be displayed
     */
    private void displayText(final String data)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {

                mText.setText(data);
            }

        });
    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() 
    {
        return new MetaioSDKCallbackHandler();
    }

    @Override
    protected void loadContents() 
    {   
        // set QR code reading configuration
        final boolean result = metaioSDK.setTrackingConfiguration("QRCODE");
        MetaioDebug.log("Tracking data loaded: " + result);
    }

    @Override
    protected void onGeometryTouched(final IGeometry geometry) 
    {

    }

    final class MetaioSDKCallbackHandler extends IMetaioSDKCallback
    {       
        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValues)
        {
            if (trackingValues.size() > 0)
            {
                final TrackingValues v = trackingValues.get(0);

                if (v.isTrackingState())
                {   
                    final String[] tokens = v.getAdditionalValues().split("::");
                    if (tokens.length > 1)
                    {
                    	Pattern p = Pattern.compile("LATITUD_(-?\\d+(?:\\.\\d+)?)_LONGITUD_(-?\\d+(?:\\.\\d+)?)");
                    	Matcher m = p.matcher(tokens[1]);
                    	
                    	if (m.find())
                    	{
                    		try
                    		{
                    			final double latitud = Double.parseDouble(m.group(1));
                    			final double longitud = Double.parseDouble(m.group(2));
                    			//displayText("Latitud: "+latitud+ " Longitud: "+longitud);
                    			runOnUiThread(new Runnable() {
                    				@Override
                    				 public void run() {
                    					Toast toast = Toast.makeText(getApplicationContext(), "Latitud: "+latitud+ "\nLongitud: "+longitud, Toast.LENGTH_LONG);
                            			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            			
                            			toast.show();
                    				}});
                    		}
                    		catch (Exception e) {}
                    	}
                    }
                }
            }
        }
    }
}
