package ec.gomez.cajerosgql;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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
	protected FrameLayout actContent = null;
	protected DrawerLayout mDrawerLayout = null;
	
    @Override
    public void setContentView(final int layoutResID) {
    	mDrawerLayout= (DrawerLayout) getLayoutInflater().inflate(R.layout.navigation_drawer, null); // Your base layout here
        actContent= (FrameLayout) mDrawerLayout.findViewById(R.id.frame_container);
        getLayoutInflater().inflate(layoutResID, actContent, true);
        super.setContentView(mDrawerLayout);
        // here you can get your drawer buttons and define how they should behave and what must they do, so you won't be needing to repeat it in every activity class
        
        ListView drawer = (ListView) findViewById(R.id.drawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		opciones = new String[]{"Opción 1", "Opción 2", "Opción 3"};
		drawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, opciones));
		
		drawer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
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
	
	//@Override
   // protected void onCreate(Bundle savedInstanceState) {
		//super.onCreate(savedInstanceState);
        //setContentView(R.layout.navigation_drawer);
		

	//}

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


}
