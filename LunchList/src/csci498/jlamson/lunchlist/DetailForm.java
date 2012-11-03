package csci498.jlamson.lunchlist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	
	EditText name;
    EditText address;
    RadioGroup types;
    EditText notes;
    
    RestaurantHelper helper;
    
    String restaurantId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_form);
        
        initDatabaseAccess();
        initFormElements();
        initRestaurantId();
        
        if(restaurantId != null) {
        	loadRestaurant();
        }
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
    
    private void initRestaurantId() {
    	restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
    }

    private void loadRestaurant() {
    	
    	Cursor c = helper.getById(restaurantId);
    	c.moveToFirst();
    	
    	name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		
		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		} else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		} else {
			types.check(R.id.delivery);
		}
		
		c.close();
		
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_form, menu);
        return true;
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	helper.close();
    }
    
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
			
//			helper.insert(name.getText().toString(),
//						address.getText().toString(),
//						type, 
//						notes.getText().toString());
//			restaurantCursor.requery();
		}
	};

}
