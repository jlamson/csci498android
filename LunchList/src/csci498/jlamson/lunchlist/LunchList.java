package csci498.jlamson.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
		
        //E.C for APT4
        //addSurplusRadioButtons();
        
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

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			Restaurant restaurant = new Restaurant();
			
			name = (EditText)findViewById(R.id.name);
			address = (EditText)findViewById(R.id.addr);
			types = (RadioGroup)findViewById(R.id.types);
			
			restaurant.setName(name.getText().toString());
			restaurant.setAddress(address.getText().toString());
			
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				restaurant.setType("sit_down");
				break;
			case R.id.take_out:
				restaurant.setType("take_out");
				break;
			case R.id.delivery:
				restaurant.setType("delivery");
				break;
			}
			
			restaurantAdapter.add(restaurant);
			addressAdapter.add(address.getText().toString());
		}
	};

	@SuppressWarnings("unused")
	private void addSurplusRadioButtons() {
		RadioGroup types = (RadioGroup)findViewById(R.id.types);
		
		int bigNumber = 20;
		for(int i=0; i < bigNumber; i++) {
			RadioButton rb = new RadioButton(this);
			rb.setText("fooblah");
			types.addView(rb);
		}
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
				
				row = inflater.inflate(R.layout.row_d, null);
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
				name.setTextColor(ll.getResources().getColor(R.color.red));
			} else if(r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
				name.setTextColor(ll.getResources().getColor(R.color.yellow));
			} else {
				icon.setImageResource(R.drawable.ball_green);
				name.setTextColor(ll.getResources().getColor(R.color.green));
			}
		}
	}
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(	AdapterView<?> parent,
									View view,
									int position,
									long id) {
			Restaurant r = restaurants.get(position);
			
			name.setText(r.getName());
			address.setText(r.getAddress());
			
			if(r.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			} else if(r.getType().equals("take_out")) {
				types.check(R.id.take_out);
			} else {
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(1);
		} 
	};
}
