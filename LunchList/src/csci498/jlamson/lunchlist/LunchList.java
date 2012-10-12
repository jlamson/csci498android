package csci498.jlamson.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

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
    
    RestaurantHelper helper = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initDatabaseAccess();
        initFormElements();
        initRestaurantListView();
        initAddressAutoComplete();
        initTabs();
        
    }
    
    private void initDatabaseAccess() {
    	helper = new RestaurantHelper(this);
    }
    
    private void initFormElements() {
    	name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
    }
    
    private void initRestaurantListView() {
    	 ListView restaurantListview = (ListView)findViewById(R.id.restaurants);
         restaurantAdapter = new RestaurantAdapter();
         restaurantListview.setAdapter(restaurantAdapter);
         restaurantListview.setOnItemClickListener(onListClick);
    }
    
    private void initAddressAutoComplete() {
    	AutoCompleteTextView addressField = (AutoCompleteTextView)findViewById(R.id.addr);
        addressAdapter = new ArrayAdapter<String>(	this,
        											android.R.layout.simple_dropdown_item_1line,
        											previousAddresses);
        addressField.setAdapter(addressAdapter);
    }
    
    private void initTabs() {
    	
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

    public void onDestroy() {
    	super.onDestroy();
    	helper.close();
    }
    
    class RestaurantAdapter extends CursorAdapter {
		public RestaurantAdapter(Cursor c) {
			super(	LunchList.this, c);
		}

		@Override
		public void bindView(View row, Context context, Cursor c) {
			RestaurantHolder holder = (RestautantHolder) row.getTag();
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			LayoutInflater inflator = getLayoutInflater();
			View row = inflator.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			
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
			
			String type = "";
			
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				type = "sit_down";
				break;
			case R.id.take_out:
				type = "take_out";
				break;
			case R.id.delivery:
				type = "delivery";
				break;
			}
			
			helper.insert(name.getText().toString(),
						address.getText().toString(),
						type, 
						notes.getText().toString());
		}
	};

}
