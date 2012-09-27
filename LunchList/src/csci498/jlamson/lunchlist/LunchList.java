package csci498.jlamson.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class LunchList extends TabActivity {
	static final int TOAST_DIALOG_ID = 0;
	static final int ERROR_DIALOG_ID = 1;
	
	List<Restaurant> restaurants = new ArrayList<Restaurant>();
	RestaurantAdapter restaurantAdapter = null;
	
	List<String> previousAddresses = new ArrayList<String>();
	ArrayAdapter<String> addressAdapter = null;
	
	EditText name = null;
    EditText address = null;
    RadioGroup types = null;
    EditText notes = null;
    
    Restaurant current = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);
		
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        Button error = (Button)findViewById(R.id.error);
        error.setOnClickListener(onError);
        
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
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(this).inflate(R.menu.option, menu);
    	return(super.onCreateOptionsMenu(menu));
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.toast) {
    	    showDialog(TOAST_DIALOG_ID);
    		return true;
    	} else if (item.getItemId() == R.id.toggle_tab) {
    		if(getTabHost().getCurrentTab() == 0) {
    			getTabHost().setCurrentTab(1);
    		} else {
    			getTabHost().setCurrentTab(0);
    		}
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    		
    public Dialog onCreateDialog(int id) {
    	Dialog dialog;
    	AlertDialog.Builder builder;
    	switch (id) {
    	case TOAST_DIALOG_ID:
    		String message = "No Restaurant Selected";
    		
    		if(current != null) {
    			message = current.getNotes();
    		}
    		
    		builder = new AlertDialog.Builder(this);
    	    builder	.setTitle(current.getName())
    	    		.setMessage(message)
    	    		.setCancelable(true)
    	    		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
    	    		});
    	    dialog = builder.create();
    		break;
    	case ERROR_DIALOG_ID:
    		builder = new AlertDialog.Builder(this);
    		builder	.setTitle("Error")
    				.setMessage("We're sorry... It seems an unexpected error has occured")
    				.setCancelable(false)
    				.setNeutralButton("Close the app", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                LunchList.this.finish();
			           }
    	    		});
    		dialog = builder.create();
    		break;
    	default:
    		dialog = null;
    	}
    	
    	return dialog;
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
	
	
	private View.OnClickListener onError = new View.OnClickListener() {
		public void onClick(View v) {
			try {
				int x = 63746/0;
			} catch (Exception e) {
				Log.e("onError", "OH CRAP, Divide by 0", e);
				showDialog(ERROR_DIALOG_ID);
			}
		}
	};
}
