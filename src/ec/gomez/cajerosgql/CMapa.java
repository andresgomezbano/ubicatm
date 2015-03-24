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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class CMapa extends BaseActivity {
	 
	GoogleMap mapa = null;
	private Button btn_consultar;
	private ProgressBar progressBar;
	LatLng puntoInicial;
	//private ImageView img_marker;
	//LatLng ultimoPunto;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.view_mapa);
    	btn_consultar = (Button)findViewById(R.id.btn_consultar);
    	//img_marker = (ImageView)findViewById(R.id.img_marker);
    	btn_consultar.setOnClickListener(handler_consultarCajeros);
    	progressBar = (ProgressBar) findViewById(R.id.progressBar1);
    	
    	mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    	puntoInicial = new LatLng(-2.18833, -79.8926);
    	mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoInicial,14.0f));
    	GPSTracker gpsTracker = new GPSTracker(this);
    	if (gpsTracker.canGetLocation())
        {
    		double latitud = gpsTracker.getLatitude();
    		double longitud = gpsTracker.getLongitude();
    		Log.i("info",String.valueOf(latitud));
    		Log.i("info",String.valueOf(longitud));
    		if(latitud != 0 && longitud != 0)this.puntoInicial = new LatLng(latitud, longitud);
    		new RetrieveFeedTask(this.puntoInicial,true,true).execute();
        }
    }
    
    public void ocultarBarra()
    {
    	this.progressBar.setVisibility(View.GONE);
    }
    
    public void mostrarBarra()
    {
    	this.progressBar.setVisibility(View.VISIBLE);
    }
    
    View.OnClickListener handler_consultarCajeros = new View.OnClickListener() {
        public void onClick(View v) {
        	LatLng puntoCentro = mapa.getCameraPosition().target;    		
    		new RetrieveFeedTask(puntoCentro,false,false).execute();
			
        }
      };
    
      public void aceptarBancos()
      {
    	  /*String latitud = String.valueOf(ultimoPunto.latitude);
    	  String longitud = String.valueOf(ultimoPunto.longitude);*/
    	LatLng puntoCentro = mapa.getCameraPosition().target;
      	//String latitud = String.valueOf(puntoCentro.latitude);
      	//String longitud = String.valueOf(puntoCentro.longitude);
      	//puntoInicial = new LatLng(latitud, longitud);
      	new RetrieveFeedTask(puntoCentro,false,false).execute();
      }
      
	class RetrieveFeedTask extends AsyncTask<Object, Object, Object> {
	    private LatLng puntoCentral;
	    private Boolean cambiarZoom;
	    private ArrayList<Cajero> cajeros = null;
	    private ProgressBar progressBar;
	    private Boolean cambiarPosicion;
	    
	    /*public RetrieveFeedTask(String latitud, String longitud)
	    {
	    	this.latitud = latitud;
	    	this.longitud = longitud;
	    	this.cambiarZoom = false;
	    	this.cambiarPosicion = true;
	    	progressBar = (ProgressBar) findViewById(R.id.progressBar1);
	    }
	    
	    public RetrieveFeedTask(String latitud, String longitud, Boolean cambiarZoom)
	    {
	    	this.latitud = latitud;
	    	this.longitud = longitud;
	    	this.cambiarZoom = cambiarZoom;
	    	this.cambiarPosicion = true;
	    }*/
	    
	    public RetrieveFeedTask(LatLng puntoCentral, Boolean cambiarZoom, Boolean cambiarPosicion)
	    {
	    	this.puntoCentral = puntoCentral;
	    	this.cambiarZoom = cambiarZoom;
	    	this.cambiarPosicion = cambiarPosicion;
	    	CMapa.this.mostrarBarra();
	    }
	    
		@Override
		protected Object doInBackground(Object... params) {
			//this.progressBar.setVisibility(View.VISIBLE);
			this.cajeros = Cajero.obtenerCercanos(CMapa.this, puntoCentral, bancosElegidos);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
		    super.onPostExecute(result);
		    mapa.clear();
		    
		    //LatLng puntoCentro = new LatLng(Double.parseDouble(this.latitud),Double.parseDouble(this.longitud));
		    //CMapa.this.ultimoPunto = puntoCentro;
		    
		    if(this.cambiarPosicion)
		    {
			    CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(this.puntoCentral);
			    mapa.moveCamera(camUpd1);
			    /*mapa.addMarker(new MarkerOptions()
			    				.position(puntoCentro)
			    				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));*/
		    }
		    
		    for(Cajero cajero: this.cajeros)
		    {
			    mapa.addMarker(new MarkerOptions()
		        .position(new LatLng(Double.parseDouble(cajero.getLatitud()),Double.parseDouble(cajero.getLongitud())))
		        .title("Nombre: " + cajero.getNombre() + " Banco: " + cajero.getNombreBanco())
		        .icon(BitmapDescriptorFactory.defaultMarker(Cajero.getBancoColor(cajero.getIdBanco()))));
		    }
		    if (this.cambiarZoom)
		    {
		    	mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(this.puntoCentral,17), 2000, null);
		    	
		    }
		    CMapa.this.ocultarBarra();
	        //adaptador.notifyDataSetChanged();
		    //this.progressBar.setVisibility(View.GONE);
		}
	}
 
}