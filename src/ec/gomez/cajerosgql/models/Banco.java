package ec.gomez.cajerosgql.models;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import ec.gomez.cajerosgql.R;
import android.util.Log;

public class Banco {
	private int id;
	private String nombre;
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getNombre()
	{
		return this.nombre;
	}
	
	public static ArrayList<Banco> obtenerListado(Context context)
	{
		HttpClient httpClient = new DefaultHttpClient();
		Resources res = context.getResources();
		String url = res.getString(R.string.server_url) + res.getString(R.string.service_bancos);
		HttpGet get = new HttpGet(url);
		get.setHeader("content-type", "application/json");
		ArrayList<Banco> bancos = null;
		try
		{
			HttpResponse resp = httpClient.execute(get);
			String respStr = EntityUtils.toString(resp.getEntity());
			JSONArray respJSON = new JSONArray(respStr);
			bancos = new ArrayList<Banco>();
			for(int i=0; i<respJSON.length(); i++)
			{
			   	JSONObject obj = respJSON.getJSONObject(i);
			   	Banco banco = new Banco();
			   	banco.id = obj.getInt("id");
			   	banco.nombre = obj.getString("nombre");
			   	bancos.add(banco);
			}
			return bancos;

		}
		catch(Exception ex)
		{
		        Log.e("ServicioRest","Error!", ex);
		}
		return null;
	}
}
