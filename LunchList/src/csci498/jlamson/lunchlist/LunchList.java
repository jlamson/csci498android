package csci498.jlamson.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class LunchList extends ListActivity {
	Cursor restaurantCursor;
	RestaurantAdapter restaurantAdapter = null;
	
	List<String> previousAddresses = new ArrayList<String>();
	ArrayAdapter<String> addressAdapter;
	
	EditText name;
    EditText address;
    RadioGroup types;
    EditText notes;
    
    RestaurantHelper helper;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initDatabaseAccess();
        initFormElements();
        initRestaurantListView();
        
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
         restaurantCursor = helper.getAll();
         startManagingCursor(restaurantCursor);
    	 restaurantAdapter = new RestaurantAdapter(restaurantCursor);
         restaurantListview.setAdapter(restaurantAdapter);
         restaurantListview.setOnItemClickListener(onListClick);
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
			RestaurantHolder holder = (RestaurantHolder) row.getTag();
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
		
		public void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));
			
			if (helper.getType(c).equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			} else if (helper.getType(c).equals("take_out")) {
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
			Intent i = new Intent(LunchList.this, DetailForm.class);
			startActivity(i);
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
			restaurantCursor.requery();
		}
	};

}
