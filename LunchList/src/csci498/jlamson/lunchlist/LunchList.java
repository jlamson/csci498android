package csci498.jlamson.lunchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class LunchList extends TabActivity {
	List<Restaurant> restaurants = new ArrayList<Restaurant>();
	RestaurantAdapter restaurantAdapter = null;
	
	List<String> previousAddresses = new ArrayList<String>();
	ArrayAdapter<String> addressAdapter = null;
	
	EditText name = null;
    EditText address = null;
    RadioGroup types = null;
    EditText notes = null;
    
    Restaurant current = null;
    
    int progress;
    
    AtomicBoolean isActive = new AtomicBoolean(true);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.main);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);
		
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        ListView restaurantListview = (ListView)findViewById(R.id.restaurants);
        restaurantAdapter = new RestaurantAdapter();
        restaurantListview.setAdapter(restaurantAdapter);
        restaurantListview.setOnItemClickListener(onListClick);
        
        AutoCompleteTextView addressField = (AutoCompleteTextView)findViewById(R.id.addr);
        addressAdapter = new ArrayAdapter<String>(	this,
        											android.R.layout.simple_dropdown_item_1line,
        											previousAddresses);
        addressField.setAdapter(addressAdapter);
        
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list_tab_icon));
        getTabHost().addTab(spec);
        
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant_tab_icon));
        getTabHost().addTab(spec);
        
        getTabHost().setCurrentTab(0);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	 isActive.set(false);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	isActive.set(true);
    	
    	if(progress > 0) {
    		startWork();
    	}
    }

    private void doSomeLongWork(final int incr) {
    	runOnUiThread( new Runnable() {
    		public void run() {
    			progress += incr;
    			setProgress(progress);
    		}
    	});
    	
    	SystemClock.sleep(250);
    }
    
    private Runnable longTask = new Runnable() {
    	public void run() {
    		for(int i=0; i<10000 && isActive.get(); i+=200) {
    			doSomeLongWork(200);
    		}
    		
    		if(isActive.get()) {
		    	runOnUiThread( new Runnable() {
		    		public void run() {
		    			setProgressBarVisibility(false);
		    			progress = 0;
		    		}
		    	});
    		}
    	}
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(this).inflate(R.menu.option, menu);
    	return(super.onCreateOptionsMenu(menu));
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.toast) {
    		String message = "No Restaurant Selected";
    		
    		if(current != null) {
    			message = current.getNotes();
    		}
    		
    		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    		
    		return true;
    	} else if (item.getItemId() == R.id.thread) {
    		startWork();
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    private void startWork() {
    	setProgressBarVisibility(true);
		new Thread(longTask).start();
	}

    class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		public RestaurantAdapter() {
			super(	LunchList.this,
					android.R.layout.simple_list_item_1,
					restaurants);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;
			
			if(row==null) {
				LayoutInflater inflater = getLayoutInflater();
				
				row = inflater.inflate(R.layout.row, null);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			} else {
				holder = (RestaurantHolder)row.getTag();
			}
			
			holder.populateFrom(restaurants.get(position), LunchList.this);
			
			return row;
		}
	}
	
	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;
	
		public RestaurantHolder(View row) {
			name = 		(TextView)row.findViewById(R.id.title);
			address = 	(TextView)row.findViewById(R.id.address);
			icon =		(ImageView)row.findViewById(R.id.icon);
		}
		
		public void populateFrom(Restaurant r, Context ll) {
			name.setText(r.getName());
			address.setText(r.getAddress());
			
			if(r.getType().equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			} else if(r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			} else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}
	}
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(	AdapterView<?> parent,
									View view,
									int position,
									long id) {
			current = restaurants.get(position);
			
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());
			
			if(current.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			} else if(current.getType().equals("take_out")) {
				types.check(R.id.take_out);
			} else {
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(1);
		} 
	};
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			current = new Restaurant();
			
			name = (EditText)findViewById(R.id.name);
			address = (EditText)findViewById(R.id.addr);
			types = (RadioGroup)findViewById(R.id.types);
			notes = (EditText)findViewById(R.id.notes);
			
			current.setName(name.getText().toString());
			current.setAddress(address.getText().toString());
			current.setNotes(notes.getText().toString());
			
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				current.setType("sit_down");
				break;
			case R.id.take_out:
				current.setType("take_out");
				break;
			case R.id.delivery:
				current.setType("delivery");
				break;
			}
			
			restaurantAdapter.add(current);
			addressAdapter.add(address.getText().toString());
		}
	};

}