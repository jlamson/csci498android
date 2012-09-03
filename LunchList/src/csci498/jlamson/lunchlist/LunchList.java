package csci498.jlamson.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LunchList extends Activity {
	List<Restaurant> restaurants = new ArrayList<Restaurant>();
	ArrayAdapter<Restaurant> restaurantAdapter = null;
	
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
        restaurantAdapter = new ArrayAdapter<Restaurant>( 	this,
        													android.R.layout.simple_list_item_1,
        													restaurants);
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
}
