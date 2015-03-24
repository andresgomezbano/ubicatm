package ec.gomez.cajerosgql;

import java.util.ArrayList;

import ec.gomez.cajerosgql.models.Banco;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class BaseActivity extends android.support.v4.app.FragmentActivity{
	
	String[] opciones = null;
	DrawerLayout drawerLayout = null;
	ActionBarDrawerToggle toggle = null;
	//protected FrameLayout actContent = null;
	//protected DrawerLayout mDrawerLayout = null;
	protected ArrayList<Banco> bancos = null;
	protected ArrayList<Integer> bancosElegidos = null;
	AlertDialog dialogoBancos = null;
		
    @Override
    public void setContentView(final int layoutResID) {
    	DrawerLayout mDrawerLayout= (DrawerLayout) getLayoutInflater().inflate(R.layout.navigation_drawer, null); // Your base layout here
    	FrameLayout actContent= (FrameLayout) mDrawerLayout.findViewById(R.id.frame_container);
        getLayoutInflater().inflate(layoutResID, actContent, true);
        super.setContentView(mDrawerLayout);
        // here you can get your drawer buttons and define how they should behave and what must they do, so you won't be needing to repeat it in every activity class
        bancosElegidos = new ArrayList<Integer>();
        bancosElegidos.add(-1);
        new ThreadBanco().execute();
        
        
        ListView drawer = (ListView) findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		opciones = new String[]{"Bancos", "Redes", "Opción 3"};
		drawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, opciones));
		
		drawer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	            dialogoBancos.show();
				Toast.makeText(BaseActivity.this, "Pulsado: " + opciones[arg2], Toast.LENGTH_SHORT).show();
				drawerLayout.closeDrawers();
			}
		});
		
		toggle = new ActionBarDrawerToggle(this, drawerLayout,R.drawable.ic_drawer, R.string.app_name, R.string.hello_world ){
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(getResources().getString(R.string.app_name));
				invalidateOptionsMenu();
			}
			
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Menu");
				invalidateOptionsMenu();
				}
		};
		drawerLayout.setDrawerListener(toggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }
	
    public void aceptarBancos()
    {
    	
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	toggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (toggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

     class ThreadBanco extends AsyncTask<Object, Object, Object>{
    	    	
    	@Override
    	protected Object doInBackground(Object... params) {
    		bancos = Banco.obtenerListado(BaseActivity.this);
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Object result)
    	{
        	final CharSequence[] items = new CharSequence[bancos.size() + 1];
    		final boolean[] marcados = new boolean[bancos.size() + 1];
    		int i = 1;
    		items[0] = "Todos";
    		marcados[0] = true;
    		for(Banco banco: bancos)
    		{
    			//bancosElegidos.add(banco.getId());
    			items[i] = banco.getNombre();
    			marcados[i] = true;
    			i++;
    		}
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
    		builder.setTitle("Seleccionar Bancos");
    		builder.setMultiChoiceItems(items, marcados,new DialogInterface.OnMultiChoiceClickListener() {
    		     @Override
    		     public void onClick(DialogInterface dialog, int indexSelected,boolean isChecked) {
    		    	 if(indexSelected == 0)
    		    	 {
    		    		 bancosElegidos.clear();
    		    		 if(isChecked)bancosElegidos.add(-1);
    		    		 ListView list = ((AlertDialog) dialog).getListView();
    		    		 for(int i = 1; i < items.length; i++)
    		    		 {
    		    			 list.setItemChecked(i, isChecked);
    		    			 marcados[i] = isChecked;
    		    			 //if(isChecked)bancosElegidos.add(bancos.get(i).getId());
    		    		 }
    		    		 /*if(!isChecked)bancosElegidos.clear();
    		    		 Log.i("data",bancosElegidos.toString());*/
    		    	 }
    		    	 else
    		    	 {
    		    		 if (isChecked) 
    		    		 {
    		    			 bancosElegidos.add(bancos.get(indexSelected -1).getId());
    		    		 } else if (bancosElegidos.contains(bancos.get(indexSelected-1).getId())) 
    		    		 {
    		    			 bancosElegidos.remove(Integer.valueOf(bancos.get(indexSelected-1).getId()));
    		    		 }
    		    	 }
    		     }
    		 })
    		 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    		 @Override
    		 public void onClick(DialogInterface dialog, int id) {
    			 	Log.i("data",bancosElegidos.toString());
    		     	aceptarBancos();
    		     }
    		 })
    		 .setNegativeButton("Cancel",null);
    		 dialogoBancos = builder.create();
    	}
    }
}
