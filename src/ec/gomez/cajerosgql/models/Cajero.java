package ec.gomez.cajerosgql.models;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import ec.gomez.cajerosgql.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


public class Cajero {
	private int id;
	private int idBanco;
	private String nombreBanco;
	private String latitud;
	private String longitud;
	private String nombre;
	private String direccion;
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setIdBanco(int idBanco)
	{
		this.idBanco = idBanco;
	}
	
	public void setNombreBanco(String nombreBanco)
	{
		this.nombreBanco = nombreBanco;
	}
	
	public void setLatitud(String latitud)
	{
		this.latitud = latitud;
	}
	
	public void setLongitud(String longitud)
	{
		this.longitud = longitud;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getIdBanco()
	{
		return this.idBanco;
	}
	
	public String getNombreBanco()
	{
		return this.nombreBanco;
	}
	
	public String getLatitud()
	{
		return this.latitud;
	}
	
	public String getLongitud()
	{
		return this.longitud;
	}
	
	public String getNombre()
	{
		return this.nombre;
	}
	
	public String getDireccion()
	{
		return this.direccion;
	}
	
	public static ArrayList<Cajero> obtenerCercanos(Context context, String latitud,String longitud, ArrayList<Integer> bancosElegidos)
	{
		HttpClient httpClient = new DefaultHttpClient();
		Resources res = context.getResources();
		String url = res.getString(R.string.server_url) + res.getString(R.string.service_cajeros);
		HttpPost post = new HttpPost(url);
		//HttpPost post = new HttpPost("http://192.168.0.21/cajeros/services/cajero/cercanos/");
		post.setHeader("content-type", "application/json");
		ArrayList<Cajero> cajeros = null;
		try
		{
			JSONObject dato = new JSONObject();
			dato.put("latitud", String.valueOf(latitud));
			dato.put("longitud", String.valueOf(longitud));
			String bancos = bancosElegidos.toString();
			dato.put("bancos",bancos.substring(1,bancos.length()-1));
			StringEntity entity = new StringEntity(dato.toString());
			post.setEntity(entity);
			
			HttpResponse resp = httpClient.execute(post);
			String respStr = EntityUtils.toString(resp.getEntity());
			JSONArray respJSON = new JSONArray(respStr);
			cajeros = new ArrayList<Cajero>();
			//JSONObject obj = new JSONObject(respStr);
			   
			for(int i=0; i<respJSON.length(); i++)
			{
			   	JSONObject obj = respJSON.getJSONObject(i);
			   	Cajero cajero = new Cajero();
			   	cajero.id = obj.getInt("id");
			   	cajero.idBanco = obj.getInt("idBanco");
			   	cajero.latitud = obj.getString("latitud");
			   	cajero.longitud = obj.getString("longitud");
			   	cajero.direccion = obj.getString("direccion");
			   	cajero.nombreBanco = obj.getString("nombreBanco");
			   	cajero.nombre = obj.getString("nombre");
			   	cajeros.add(cajero);
			}
			
			return cajeros;

		}
		catch(Exception ex)
		{
		        Log.e("ServicioRest","Error!", ex);
		}
		return null;
	}
	
	public static float getBancoColor(int idBanco)
	{
		switch(idBanco)
		{
			case 1: return BitmapDescriptorFactory.HUE_YELLOW;
			case 2: return BitmapDescriptorFactory.HUE_GREEN;
			case 3: return BitmapDescriptorFactory.HUE_BLUE;
			case 4: return BitmapDescriptorFactory.HUE_MAGENTA;
		}
		return BitmapDescriptorFactory.HUE_AZURE;
		
	}
}
