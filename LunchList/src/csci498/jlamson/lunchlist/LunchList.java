package csci498.jlamson.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LunchList extends Activity {
	List<Restaurant> restaurants = new ArrayList<Restaurant>();
	RestaurantAdapter restaurantAdapter = null;
	
	List<String> previousAddresses = new ArrayList<String>();
	ArrayAdapter<String> addressAdapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        //E.C for APT4
        //addSurplusRadioButtons();
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        ListView restaurantListview = (ListView)findViewById(R.id.restaurants);
        restaurantAdapter = new RestaurantAdapter();
        restaurantListview.setAdapter(restaurantAdapter);
        
        AutoCompleteTextView addressField = (AutoCompleteTextView)findViewById(R.id.addr);
        addressAdapter = new ArrayAdapter<String>(	this,
        											android.R.layout.simple_dropdown_item_1line,
        											previousAddresses);
        addressField.setAdapter(addressAdapter);
        
    }

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			Restaurant restaurant = new Restaurant();
			
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			
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
				
				row = inflater.inflate(R.layout.row, null);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			} else {
				holder = (RestaurantHolder)row.getTag();
			}
			
			holder.populateFrom(restaurants.get(position));
			
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
		
		public void populateFrom(Restaurant r) {
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
}
