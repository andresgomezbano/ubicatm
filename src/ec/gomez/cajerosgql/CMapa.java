package ec.gomez.cajerosgql;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ec.gomez.cajerosgql.external.GPSTracker;
import ec.gomez.cajerosgql.models.Cajero;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CMapa extends BaseActivity {
	 
	GoogleMap mapa = null;
	private Button btn_consultar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.view_mapa);
    	btn_consultar = (Button)findViewById(R.id.btn_consultar);
    	btn_consultar.setOnClickListener(handler_consultarCajeros);
    	
    	mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    	
    	GPSTracker gpsTracker = new GPSTracker(this);
    	if (gpsTracker.canGetLocation())
        {
    		String latitud = String.valueOf(gpsTracker.getLatitude());
    		String longitud = String.valueOf(gpsTracker.getLongitude());    		
    		new RetrieveFeedTask(latitud,longitud,true).execute();
        }
    	    	
    }
    
    View.OnClickListener handler_consultarCajeros = new View.OnClickListener() {
        public void onClick(View v) {
        	LatLng puntoCentro = mapa.getCameraPosition().target;
        	String latitud = String.valueOf(puntoCentro.latitude);
    		String longitud = String.valueOf(puntoCentro.longitude);    		
    		new RetrieveFeedTask(latitud,longitud).execute();
			
        }
      };
    
	class RetrieveFeedTask extends AsyncTask<Object, Object, Object> {
	    private String latitud;
	    private String longitud;
	    private Boolean cambiarZoom;
	    private ArrayList<Cajero> cajeros = null;
	    
	    public RetrieveFeedTask(String latitud, String longitud)
	    {
	    	this.latitud = latitud;
	    	this.longitud = longitud;
	    	this.cambiarZoom = false;
	    }
	    
	    public RetrieveFeedTask(String latitud, String longitud, Boolean cambiarZoom)
	    {
	    	this.latitud = latitud;
	    	this.longitud = longitud;
	    	this.cambiarZoom = cambiarZoom;
	    }
	    
		@Override
		protected Object doInBackground(Object... params) {
			this.cajeros = Cajero.obtenerCercanos(latitud, longitud);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
		    super.onPostExecute(result);
		    mapa.clear();
		    LatLng puntoCentro = new LatLng(Double.parseDouble(this.latitud),Double.parseDouble(this.longitud));
		    CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(puntoCentro);
		    mapa.moveCamera(camUpd1);
		    mapa.addMarker(new MarkerOptions()
		    				.position(puntoCentro)
		    				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		    for(Cajero cajero: this.cajeros)
		    {
			    mapa.addMarker(new MarkerOptions()
		        .position(new LatLng(Double.parseDouble(cajero.getLatitud()),Double.parseDouble(cajero.getLongitud())))
		        .title("Nombre: " + cajero.getNombre() + " Banco: " + cajero.getNombreBanco())
		        .icon(BitmapDescriptorFactory.defaultMarker(Cajero.getBancoColor(cajero.getIdBanco()))));
		    }
		    if (this.cambiarZoom)
		    {
		    	mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(puntoCentro,15), 2000, null);
		    	
		    }
	        //adaptador.notifyDataSetChanged();
		}
	}
 
}