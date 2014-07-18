package ec.gomez.cajerosgql;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import ec.gomez.cajerosgql.control.*;
import ec.gomez.cajerosgql.external.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

public class Main extends Activity {

	private ListView lstOpciones;
	private Button btn_consultar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Resources res = getResources();
        
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
         
        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Cercanos",null);
        tabs.addTab(spec);
         
        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Búsqueda",null);
        tabs.addTab(spec);
         
        tabs.setCurrentTab(0);
        
        btn_consultar = (Button)findViewById(R.id.btn_consultar);
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        btn_consultar.setOnClickListener(myhandler1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View v) {
        	GPSTracker gpsTracker = new GPSTracker(Main.this);
        	if (gpsTracker.canGetLocation())
	        {
        		String latitud = String.valueOf(gpsTracker.getLatitude());
        		String longitud = String.valueOf(gpsTracker.getLongitude());
        		Log.i("latitud",latitud);
        		Log.i("longitud",longitud);
        		
        		new RetrieveFeedTask(latitud,longitud).execute();
	        }
        }
      };
      
  	class RetrieveFeedTask extends AsyncTask {

	    private CajeroManager[] cajeros = null;
	    private String latitud;
	    private String longitud;
	    
	    public RetrieveFeedTask(String latitud, String longitud)
	    {
	    	this.latitud = latitud;
	    	this.longitud = longitud;
	    }
	    
		@Override
		protected Object doInBackground(Object... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.segurocanguro.com/cajeros/services/cajero/cercanos/");
			post.setHeader("content-type", "application/json");
			
			try
			{
					JSONObject dato = new JSONObject();
					Log.i("latitud",this.latitud);
					Log.i("longitud",this.longitud);
					dato.put("latitud", String.valueOf(this.latitud));
					dato.put("longitud", String.valueOf(this.longitud));
						
					StringEntity entity = new StringEntity(dato.toString());
					post.setEntity(entity);
					
				    HttpResponse resp = httpClient.execute(post);
				    String respStr = EntityUtils.toString(resp.getEntity());
				        
				    JSONArray respJSON = new JSONArray(respStr);
				    cajeros = new CajeroManager[respJSON.length()];
				        //JSONObject obj = new JSONObject(respStr);
				       
				        
				    for(int i=0; i<respJSON.length(); i++)
				    {
				       	JSONObject obj = respJSON.getJSONObject(i);
				       	CajeroManager cajero = new CajeroManager(obj.getString("nombreBanco"), obj.getString("nombre"));
				        cajeros[i] = cajero;
				    }
			        
			 
			}
			catch(Exception ex)
			{
			        Log.e("ServicioRest","Error!", ex);
			}
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Object result) {
		    super.onPostExecute(result);
		    CajeroAdaptador adaptador = new CajeroAdaptador(Main.this,cajeros);
	        lstOpciones.setAdapter(adaptador);
	        //adaptador.notifyDataSetChanged();
		}
	}
}
