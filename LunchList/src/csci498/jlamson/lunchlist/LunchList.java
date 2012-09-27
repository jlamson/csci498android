package csci498.jlamson.lunchlist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class LunchList extends TabActivity {
	static final int DATE_DIALOG_ID = 0;
	static Calendar c = new GregorianCalendar();
	
	List<Restaurant> restaurants = new ArrayList<Restaurant>();
	RestaurantAdapter restaurantAdapter = null;
	
	List<String> previousAddresses = new ArrayList<String>();
	ArrayAdapter<String> addressAdapter = null;
	
	EditText name = null;
    EditText address = null;
    RadioGroup types = null;
    TextView lastVisit = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
		lastVisit = (TextView)findViewById(R.id.last_visit);
		
        //E.C for APT4
        //addSurplusRadioButtons();
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        Button datePickButton = (Button)findViewById(R.id.pick_date_button);
        datePickButton.setOnClickListener(onDatePick);
        
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
			lastVisit = (TextView)findViewById(R.id.last_visit);
			
			restaurant.setName(name.getText().toString());
			restaurant.setAddress(address.getText().toString());
			restaurant.setLastVisit(lastVisit.getText().toString());
			
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

	
	private View.OnClickListener onDatePick = new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
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
			
			holder.populateFrom(restaurants.get(position), LunchList.this);
			
			return row;
		}
	}
	
	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private TextView lastVisit = null;
		private ImageView icon = null;
	
		public RestaurantHolder(View row) {
			name = 		(TextView)row.findViewById(R.id.title);
			address = 	(TextView)row.findViewById(R.id.address);
			lastVisit =	(TextView)row.findViewById(R.id.last_visit_row);
			icon =		(ImageView)row.findViewById(R.id.icon);
		}
		
		public void populateFrom(Restaurant r, Context ll) {
			name.setText(r.getName());
			address.setText(r.getAddress());
			lastVisit.setText(r.getLastVisit());
			
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
			lastVisit.setText(r.getLastVisit());
			
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

	
	
	// the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int month, int day) {
            	lastVisit.setText(month + "/" + day + "/" + year);
            }
        };

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, 
					c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}
	
	
}
