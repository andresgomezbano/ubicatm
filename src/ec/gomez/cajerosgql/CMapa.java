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
import android.widget.ImageView;
import android.widget.ProgressBar;

public class CMapa extends BaseActivity {
	 
	GoogleMap mapa = null;
	private Button btn_consultar;
	private ProgressBar progressBar;
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
    	
    	GPSTracker gpsTracker = new GPSTracker(this);
    	if (gpsTracker.canGetLocation())
        {
    		String latitud = String.valueOf(gpsTracker.getLatitude());
    		String longitud = String.valueOf(gpsTracker.getLongitude());    		
    		new RetrieveFeedTask(latitud,longitud,true,true).execute();
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
        	String latitud = String.valueOf(puntoCentro.latitude);
    		String longitud = String.valueOf(puntoCentro.longitude);    		
    		new RetrieveFeedTask(latitud,longitud,false,false).execute();
			
        }
      };
    
      public void aceptarBancos()
      {
    	  /*String latitud = String.valueOf(ultimoPunto.latitude);
    	  String longitud = String.valueOf(ultimoPunto.longitude);*/
    	LatLng puntoCentro = mapa.getCameraPosition().target;
      	String latitud = String.valueOf(puntoCentro.latitude);
      	String longitud = String.valueOf(puntoCentro.longitude);
      	new RetrieveFeedTask(latitud,longitud,false,false).execute();
      }
      
	class RetrieveFeedTask extends AsyncTask<Object, Object, Object> {
	    private String latitud;
	    private String longitud;
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
	    
	    public RetrieveFeedTask(String latitud, String longitud, Boolean cambiarZoom, Boolean cambiarPosicion)
	    {
	    	this.latitud = latitud;
	    	this.longitud = longitud;
	    	this.cambiarZoom = cambiarZoom;
	    	this.cambiarPosicion = cambiarPosicion;
	    	CMapa.this.mostrarBarra();
	    }
	    
		@Override
		protected Object doInBackground(Object... params) {
			//this.progressBar.setVisibility(View.VISIBLE);
			this.cajeros = Cajero.obtenerCercanos(CMapa.this, latitud, longitud, bancosElegidos);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
		    super.onPostExecute(result);
		    mapa.clear();
		    
		    LatLng puntoCentro = new LatLng(Double.parseDouble(this.latitud),Double.parseDouble(this.longitud));
		    //CMapa.this.ultimoPunto = puntoCentro;
		    
		    if(this.cambiarPosicion)
		    {
			    CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(puntoCentro);
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
		    	mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(puntoCentro,17), 2000, null);
		    	
		    }
		    CMapa.this.ocultarBarra();
	        //adaptador.notifyDataSetChanged();
		    //this.progressBar.setVisibility(View.GONE);
		}
	}
 
}