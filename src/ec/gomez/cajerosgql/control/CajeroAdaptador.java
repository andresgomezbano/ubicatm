package ec.gomez.cajerosgql.control;

import com.fedorvlasov.lazylist.ImageLoader;

import ec.gomez.cajerosgql.R;
import ec.gomez.cajerosgql.models.Cajero;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CajeroAdaptador extends ArrayAdapter<Object> {
	LayoutInflater inflater = null;
    Cajero[] datos = null;
    ImageLoader imageLoader = null;
    
    public CajeroAdaptador(Activity context,Cajero[] datos) {
        super(context, R.layout.lista_maincajeros, datos);
        this.inflater = context.getLayoutInflater();
        this.datos = datos;
        imageLoader =new ImageLoader(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	    View item = inflater.inflate(R.layout.lista_maincajeros, null);
	
	    TextView lblTitulo = (TextView)item.findViewById(R.id.LblBanco);
	    lblTitulo.setText(datos[position].getNombreBanco());
	
	    TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblNombre);
	    lblSubtitulo.setText(datos[position].getDireccion());
	    //lblSubtitulo.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut risus lacinia, venenatis mi eu, mattis dui. Nam elit enim, tristique ut bibendum nec, pharetra id elit. Quisque accumsan massa neque, sit amet pharetra metus pellentesque et. Cras gravida tincidunt felis at pretium.");
	    
	    ImageView imagen = (ImageView)item.findViewById(R.id.imageView1);
	    imageLoader.DisplayImage("http://www.segurocanguro.com/cajeros/images/" + datos[position].getIdBanco() + ".jpg", imagen);
	    //new ImageDownloader(imagen).execute("http://www.segurocanguro.com/cajeros/images/" + datos[position].getIdBanco() + ".jpg");
	    
	    return(item);
    }
}
