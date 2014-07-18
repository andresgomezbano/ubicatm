package ec.gomez.cajerosgql.control;

import ec.gomez.cajerosgql.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CajeroAdaptador extends ArrayAdapter<Object> {
    Activity context;
    CajeroManager[] datos = null;
    
    public CajeroAdaptador(Activity context,CajeroManager[] datos) {
        super(context, R.layout.lista_maincajeros, datos);
        this.context = context;
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    View item = inflater.inflate(R.layout.lista_maincajeros, null);
	
	    TextView lblTitulo = (TextView)item.findViewById(R.id.LblBanco);
	    lblTitulo.setText(datos[position].getBanco());
	
	    TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblNombre);
	    lblSubtitulo.setText(datos[position].getNombre());
	
	    return(item);
    }
}
